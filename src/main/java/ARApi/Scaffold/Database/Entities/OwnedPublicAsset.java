package ARApi.Scaffold.Database.Entities;

import javax.persistence.*;

@Entity
public class OwnedPublicAsset extends BaseEntity{

    @OneToOne
    @JoinColumn(name="user_uuid")
    public User user;

    @OneToOne
    @JoinColumn(name="asset_uuid")
    public PublicAsset asset;

    public double target_percentage;

}
