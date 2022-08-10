package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import org.hibernate.SessionFactory;

import java.util.concurrent.locks.ReentrantLock;

public interface InstanceCreateAction<E extends BaseEntity, T extends AbstractInserter<E>> {

    T Create(SessionFactory sessionFactory, ReentrantLock lock, String query);
}
