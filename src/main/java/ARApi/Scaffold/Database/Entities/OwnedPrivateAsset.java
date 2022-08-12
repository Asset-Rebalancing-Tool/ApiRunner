package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Shared.Enums.AssetType;

import javax.persistence.*;
import java.util.List;

/**
 * Private assets are unique per account, meaning we can store the general
 * ownership information alongside the asset itself.
 */
@Entity
public class OwnedPrivateAsset extends BaseUserEntity{

    public String title;

    @Enumerated(EnumType.STRING)
    public AssetType asset_type;

    @OneToMany(mappedBy= "PrivateAsset")
    public List<PrivateAssetPriceRecord> PriceRecords;

    public double owned_quantity;

    public double target_percentage;


}
