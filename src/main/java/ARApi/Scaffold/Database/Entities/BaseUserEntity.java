package ARApi.Scaffold.Database.Entities;

import com.sun.istack.NotNull;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name="user_uuid")
    @NotNull
    public User user;

    public void SetUser(UUID user_uuid, JpaRepository<User, UUID> repository){
        user = repository.getReferenceById(user_uuid);
    }
}
