package ARApi.Scaffold.Database.Entities;

import com.sun.istack.NotNull;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name="user_uuid")
    @NotNull
    public User user;

    @NotNull
    public UUID user_uuid;
}
