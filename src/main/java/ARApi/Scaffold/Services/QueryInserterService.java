package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class QueryInserterService {

    @Autowired
    public QueryInserterService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final SessionFactory sessionFactory;

    private final Map<String, AbstractInserter<?>> queryInserterMap = new ConcurrentHashMap<>();

    private final Map<String, ReentrantLock> queryLockMap = new ConcurrentHashMap<>();

    public synchronized ReentrantLock GetQueryLock(String queue) {
        if (!queryLockMap.containsKey(queue)) {
             queryLockMap.put(queue, new ReentrantLock());
        }
        return queryLockMap.get(queue);
    }

    private <E extends BaseEntity, T extends AbstractInserter<E>> T GetInstance(String query, InstanceCreateAction<E, T> createAction) {
        return createAction.Create(sessionFactory, GetQueryLock(query), query);
    }

    public synchronized <E extends BaseEntity, T extends AbstractInserter<E>> T GetInserter(InstanceCreateAction<E, T> createAction, String queryString) {

        // first request on query, create Inserter for it
        if (!queryInserterMap.containsKey(queryString)) {
            T inserter = GetInstance(queryString, createAction);
            queryInserterMap.put(queryString, inserter);
            return inserter;
        }

        var lock = GetQueryLock(queryString);
        if (lock.tryLock()) {
            try {
                // could lock, this means the inserter is not running,
                // thus we can reset and return new one
                queryInserterMap.remove(queryString);

                T inserter = GetInstance(queryString, createAction);
                queryInserterMap.put(queryString, inserter);
                return inserter;
            } finally {
                lock.unlock();
            }
        }

        // means inserter is already running, return instance so normal run lock
        // functionality can have effect
        return (T) queryInserterMap.get(queryString);
    }

}
