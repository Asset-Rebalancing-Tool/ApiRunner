package ARApi.Scaffold.Database.Entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name="user_uuid")
    public User user;
}
