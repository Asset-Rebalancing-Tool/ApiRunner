package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Database.Entities.HoldingGroup;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import javax.persistence.*;

/**
 * A custom asset that the user created and that is only visible to him.
 * Could be something like a book collection, old clocks, cars etc.
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_uuid", "title"})})
@Entity
public class PrivateHolding extends BaseUserEntity {

    public String title;

    @Enumerated(EnumType.STRING)
    public AssetType asset_type;

    public double price_per_unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "holding_group_uuid")
    public HoldingGroup HoldingGroup;

    public double owned_quantity;

    public UnitType unit_type;

    public double target_percentage;

    @Enumerated(EnumType.STRING)
    public Currency currency;
}
