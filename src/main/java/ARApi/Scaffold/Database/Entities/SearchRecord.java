package ARApi.Scaffold.Database.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(indexes = {
        @Index(name = "cu_index", columnList = "context_uuid"),
        @Index(name = "s_index", columnList = "search")
})
public class SearchRecord extends BaseEntity {

    @Column(columnDefinition = "BINARY(16)")
    public UUID context_uuid;

    public String search;
}
