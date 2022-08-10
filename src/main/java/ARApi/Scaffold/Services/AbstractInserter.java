package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

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
            var result = Insert(entitiesToInsert, session);
            session.getTransaction().commit();
            session.close();
            return result;
        }finally {
            querylock.unlock();
        }
    }

    protected abstract List<E> Insert(List<E> entitiesToInsert, Session autoCCOpenendSession);


}
