package ARApi.Scaffold.Database.Entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

    @OneToOne
    public User user;
}
