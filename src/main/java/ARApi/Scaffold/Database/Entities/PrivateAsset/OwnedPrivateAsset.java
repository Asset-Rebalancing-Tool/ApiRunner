package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import javax.persistence.*;
import java.util.List;

/**
 * Private assets are unique per account, meaning we can store the general
 * ownership information alongside the asset itself.
 */
@Entity
public class OwnedPrivateAsset extends BaseUserEntity {

    public String title;

    @Enumerated(EnumType.STRING)
    public AssetType asset_type;

    @Enumerated(EnumType.STRING)
    public UnitType unit_type;

    @Enumerated(EnumType.STRING)
    public Currency currency;

    public double price;

    public double owned_quantity;

    public double target_percentage;


}
