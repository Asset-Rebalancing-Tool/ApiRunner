package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Template for creating a db inserter that seamlessly deals with duplicates
 * even in batch executions.
 *
 * Does not scale between multiple different processes and same db. Either rewrite mechanism or only scale by
 * creating multiple monoliths (single process with lots of threads, per one db).
 * @param <E> Some implementation of {@link BaseEntity}
 */
public abstract class AbstractInserter<E extends BaseEntity> {

    final Set<E> insertedEntities = ConcurrentHashMap.newKeySet();

    final SessionFactory sessionFactory;

    private final ReentrantLock querylock;

    public AbstractInserter(Class<E> type, String query, SessionFactory provider, ReentrantLock lock){
        this.querylock = lock;
        this.sessionFactory = provider;
        var freshSession = provider.openSession();
        freshSession.beginTransaction();
        insertedEntities.addAll(freshSession.createQuery(query, type).stream().distinct().toList());
        freshSession.getTransaction().commit();
        freshSession.close();
    }

    public AbstractInserter(Class<E> type, SessionFactory provider, ReentrantLock lock){
        this.sessionFactory = provider;
        this.querylock = lock;
        var freshSession = provider.openSession();
        freshSession.beginTransaction();
        CriteriaBuilder builder = freshSession.getCriteriaBuilder();
        CriteriaQuery<E> criteria = builder.createQuery(type);
        criteria.from(type);
        freshSession.createQuery(criteria).getResultStream().forEach(insertedEntities::add);
        freshSession.getTransaction().commit();
        freshSession.close();
    }

    public Set<E> GetLoadedEntities(){
        return insertedEntities;
    }

    public List<E> InsertLocked(List<E> entitiesToInsert){
        querylock.lock();
        try{
            var session = sessionFactory.openSession();
            session.beginTransaction();
            var result = TryFetchOrInsert(entitiesToInsert, session);
            session.getTransaction().commit();
            session.close();
            return result;
        }finally {
            querylock.unlock();
        }
    }

    /**
     * This should only call {@link Session#save(Object)} and neither {@link Session#close()} nor {@link Transaction#commit()}
     * @param entitiesToInsert the base entities that need to be saved (also call save for all the children you want to persist)
     * @param autoCCOpenendSession the session to call save on
     * @return a mix of inserted and already present objects that map the values provided
     */
    protected abstract List<E> TryFetchOrInsert(List<E> entitiesToInsert, Session autoCCOpenendSession);
}
