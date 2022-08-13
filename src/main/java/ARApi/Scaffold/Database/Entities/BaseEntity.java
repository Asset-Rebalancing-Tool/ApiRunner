package ARApi.Scaffold.Database.Entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    public UUID uuid;

    public LocalDateTime ts_created = LocalDateTime.now();
}
