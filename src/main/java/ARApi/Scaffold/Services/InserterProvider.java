package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InserterProvider {

    @Autowired
    public InserterProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    private final Map<Class<?>, Inserter> entityTypeInserterMap = new HashMap<>();

    private final Map<Class<?>, ReentrantLock> inserterLockMap = new HashMap<>();

    public synchronized ReentrantLock GetLock(Class<? extends BaseEntity> clazz){
        if (!inserterLockMap.containsKey(clazz)){
            inserterLockMap.put(clazz, new ReentrantLock());
        }
        return inserterLockMap.get(clazz);
    }

    private <T extends BaseEntity> Inserter GetInstance(Class<T> clazz, String query){
        Inserter inserter;
        if(query == null){
            inserter = new Inserter<T>(clazz, sessionFactory, GetLock(clazz));
        }else{
            inserter = new Inserter<T>(clazz, query, sessionFactory, GetLock(clazz));
        }
        return inserter;
    }

    public <T extends BaseEntity> Inserter<T> GetInserter(Class<T> clazz){
        return GetInserter(clazz, null);
    }

    public synchronized  <T extends BaseEntity> Inserter<T> GetInserter(Class<T> clazz, String query){

        if(!entityTypeInserterMap.containsKey(clazz)){
            Inserter inserter = GetInstance(clazz, query);
            entityTypeInserterMap.put(clazz, inserter);
            return inserter;
        }

        Inserter inserter = entityTypeInserterMap.get(clazz);
        if(GetLock(clazz).tryLock()){
            try{
                // could lock, this means the inserter is not running,
                // thus we can reset and return new one
                entityTypeInserterMap.remove(clazz);

                inserter = GetInstance(clazz, query);
                entityTypeInserterMap.put(clazz, inserter);
                return inserter;
            }finally {
                GetLock(clazz).unlock();
            }
        }
        // means inserter is already running, return instance so normal run lock
        // functionality can have effect
        return entityTypeInserterMap.get(clazz);

    }


}
