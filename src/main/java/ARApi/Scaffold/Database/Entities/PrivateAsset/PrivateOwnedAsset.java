package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import javax.persistence.*;

/**
 * A custom asset that the user created and that is only visible to him.
 * Could be something like a book collection, old clocks, cars etc.
 */
@Entity
public class PrivateOwnedAsset extends BaseUserEntity {

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
