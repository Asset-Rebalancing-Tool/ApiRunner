package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Database.Entities.PublicAsset;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Inserter<T extends BaseEntity> {

    private final Map<T, T> insertedBaseEntityByBaseEntityMap = new ConcurrentHashMap<>();

    public final ReentrantLock insertLock = new ReentrantLock();

    public Inserter(Class<T> type, Session freshSession){
        CriteriaBuilder builder = freshSession.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        freshSession.createQuery(criteria).getResultStream().forEach(be -> insertedBaseEntityByBaseEntityMap.put(be, be));
    }

    public List<T> GetLoadedEntities(){
        return insertedBaseEntityByBaseEntityMap.keySet().stream().toList();
    }

    public List<T> Insert(List<T> entitiesToInsert, Session freshSession){
        insertLock.lock();
        try{
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
                insertedBaseEntityByBaseEntityMap.put(entityToInsert, entityToInsert);
                baseEntityReturn.add(entityToInsert);

            }
            transaction.commit();
            freshSession.close();
            return baseEntityReturn;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            insertLock.unlock();
        }
        throw new IllegalStateException("Exception while mapping and saving to db");
    }
}
