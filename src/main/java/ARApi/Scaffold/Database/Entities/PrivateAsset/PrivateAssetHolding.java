package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Shared.Enums.AssetType;

import javax.persistence.*;

/**
 * A custom asset that the user created and that is only visible to him.
 * Could be something like a book collection, old clocks, cars etc.
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_uuid", "title"})})
@Entity
public class PrivateAssetHolding extends BaseUserEntity {

    public String title;

    @Enumerated(EnumType.STRING)
    public AssetType asset_type;

    public double current_price;
}
