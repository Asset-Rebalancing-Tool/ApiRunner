package ARApi.Scaffold.Database.Entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    public UUID uuid;

    public Set<BaseEntity> GetChildEntities(){
        return new HashSet<>();// default no children
        // TODO: reflection to get oneToMany Annotated members and get the items from the list/set
    }
}
