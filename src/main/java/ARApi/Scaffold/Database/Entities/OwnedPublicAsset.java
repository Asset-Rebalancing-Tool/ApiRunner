package ARApi.Scaffold.Database.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class OwnedPublicAsset extends BaseEntity{

    @OneToOne
    public User user;

    @OneToOne
    public PublicAsset dbAsset;

    public double target_percentage;

}
