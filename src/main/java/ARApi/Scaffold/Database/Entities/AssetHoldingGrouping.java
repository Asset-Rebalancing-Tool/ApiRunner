package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AssetHoldingGrouping extends BaseUserEntity{

    @ManyToMany
    @JoinTable(
            name = "grouping_public_asset_holdings",
            joinColumns = { @JoinColumn(name = "fk_grouping") },
            inverseJoinColumns = { @JoinColumn(name = "fk_public_asset_holding") }
    )
    public Set<PublicAssetHolding> PublicAssetHoldings;

    @ManyToMany
    @JoinTable(
            name = "grouping_private_asset_holdings",
            joinColumns = { @JoinColumn(name = "fk_grouping") },
            inverseJoinColumns = { @JoinColumn(name = "fk_private_asset_holding") }
    )
    public Set<PrivateAssetHolding> PrivateAssetHoldings;

    public double target_percentage;

    public String group_name;
}
