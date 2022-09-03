package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;
import java.util.UUID;

@Entity
public class AssetHoldingGroup extends BaseUserEntity{

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

    public boolean InternalPercentagesMatch(){
        double percentageCounter = 0;
        percentageCounter+=PublicAssetHoldings.stream().mapToDouble(value -> value.target_percentage).sum();
        return percentageCounter == 100d;
    }

    public boolean ContainsHolding(UUID holdingUuid){
        return PublicAssetHoldings.stream().anyMatch(holding -> holding.uuid.equals(holdingUuid)) ||
                PrivateAssetHoldings.stream().anyMatch(holding -> holding.uuid.equals(holdingUuid));
    }
}
