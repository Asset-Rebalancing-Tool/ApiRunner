package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Inserter<T extends BaseEntity> {

    private final Map<T, T> insertedBaseEntityByBaseEntityMap = new ConcurrentHashMap<>();

    private final SessionFactory sessionFactory;

    private final ReentrantLock lock;

    public Inserter(Class<T> type, String query, SessionFactory provider, ReentrantLock lock){
        this.lock = lock;
        this.sessionFactory = provider;
        var freshSession = provider.openSession();
        freshSession.beginTransaction();
        freshSession.createQuery(query, type).stream().distinct().toList().forEach(asset -> insertedBaseEntityByBaseEntityMap.put(asset, asset));
        freshSession.getTransaction().commit();
        freshSession.close();
    }

    public Inserter(Class<T> type, SessionFactory provider, ReentrantLock lock){
        this.sessionFactory = provider;
        this.lock = lock;
        var freshSession = provider.openSession();
        freshSession.beginTransaction();
        CriteriaBuilder builder = freshSession.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        freshSession.createQuery(criteria).getResultStream().forEach(be -> insertedBaseEntityByBaseEntityMap.put(be, be));
        freshSession.getTransaction().commit();
        freshSession.close();
    }

    public List<T> GetLoadedEntities(){
        return insertedBaseEntityByBaseEntityMap.keySet().stream().toList();
    }

    public List<T> Insert(List<T> entitiesToInsert){
        lock.lock();

        try(var freshSession = sessionFactory.openSession()){
            List<T> baseEntityReturn = new ArrayList<>();
            var transaction = freshSession.beginTransaction();

            for (var entityToInsert : entitiesToInsert){
                if(insertedBaseEntityByBaseEntityMap.containsKey(entityToInsert)){
                    // get the already inserted one
                    baseEntityReturn.add(insertedBaseEntityByBaseEntityMap.get(entityToInsert));
                    continue;
                }

                // insert it yourself
                freshSession.save(entityToInsert);
                // insert children aswell
                entityToInsert.GetChildEntities().forEach(freshSession::save);
                insertedBaseEntityByBaseEntityMap.put(entityToInsert, entityToInsert);
                baseEntityReturn.add(entityToInsert);

            }
            transaction.commit();

            return baseEntityReturn;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        throw new IllegalStateException("Exception while mapping and saving to db");
    }
}
