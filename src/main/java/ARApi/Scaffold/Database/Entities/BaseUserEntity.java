package ARApi.Scaffold.Database.Entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.util.UUID;

@MappedSuperclass
public class BaseUserEntity {

    @Id
    @GeneratedValue
    public UUID uuid;

    @OneToOne
    public User user;
}
