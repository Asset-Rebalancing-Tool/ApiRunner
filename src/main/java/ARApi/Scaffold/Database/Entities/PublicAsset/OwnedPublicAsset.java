package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Entities.User;

import javax.persistence.*;

@Entity
public class OwnedPublicAsset extends BaseEntity {

    @OneToOne
    @JoinColumn(name="user_uuid")
    public User user;

    @OneToOne
    @JoinColumn(name="asset_uuid")
    public PublicAsset public_asset;

    public double target_percentage;

    public boolean broker_connected = false;

    public double owned_quantity;

    public String custom_name;

}
