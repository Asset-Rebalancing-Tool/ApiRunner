package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InserterProvider {

    @Autowired
    public InserterProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    SessionFactory sessionFactory;

    private final Map<Class<?>, Inserter> entityTypeInserterMap = new HashMap<>();

    public synchronized <T extends BaseEntity> Inserter<T> GetInserter(Class<T> clazz){
        if(!entityTypeInserterMap.containsKey(clazz)){
            var inserter = new Inserter<T>(clazz, sessionFactory.openSession());
            entityTypeInserterMap.put(clazz, inserter);
            return inserter;
        }

        var inserter = entityTypeInserterMap.get(clazz);
        if(inserter.insertLock.tryLock()){
            try{
                // could lock, this means the inserter is not running,
                // thus we can reset and return new one
                entityTypeInserterMap.remove(clazz);

                inserter = new Inserter<T>(clazz, sessionFactory.openSession());
                entityTypeInserterMap.put(clazz, inserter);
                return inserter;
            }finally {
                inserter.insertLock.unlock();
            }
        }
        // means inserter is already running, return instance so normal run lock
        // functionality can have effect
        return entityTypeInserterMap.get(clazz);
    }

}
