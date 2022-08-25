package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;

import javax.persistence.*;

/**
 * Record of a user having X amount of a registered public asset
 */
@Entity
public class PublicOwnedAsset extends BaseUserEntity {

    @OneToOne
    @JoinColumn(name="asset_uuid")
    public PublicAsset public_asset;

    public double target_percentage;

    public boolean broker_connected = false;

    public double owned_quantity;

    public String custom_name;

    public boolean display_custom_name;

}
