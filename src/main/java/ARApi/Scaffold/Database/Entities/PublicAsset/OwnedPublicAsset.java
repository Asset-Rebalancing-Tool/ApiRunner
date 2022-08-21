package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Entities.User;

import javax.persistence.*;

/**
 * Record of a user having X amount of a registered public asset
 */
@Entity
public class OwnedPublicAsset extends BaseUserEntity {

    @OneToOne
    @JoinColumn(name="asset_uuid")
    public PublicAsset public_asset;

    public double target_percentage;

    public boolean broker_connected = false;

    public double owned_quantity;

    public String custom_name;

    public boolean display_custom_name;

}
