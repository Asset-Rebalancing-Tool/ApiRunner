package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateOwnedAsset;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;

import javax.persistence.*;
import java.util.Set;

@Entity
public class OwnedAssetGrouping extends BaseUserEntity{

    @ManyToMany
    @JoinTable(
            name = "grouping_owned_public_assets",
            joinColumns = { @JoinColumn(name = "fk_grouping") },
            inverseJoinColumns = { @JoinColumn(name = "fk_public_owned_asset") }
    )
    public Set<PublicOwnedAsset> PublicOwnedAssets;

    @ManyToMany
    @JoinTable(
            name = "grouping_owned_private_assets",
            joinColumns = { @JoinColumn(name = "fk_grouping") },
            inverseJoinColumns = { @JoinColumn(name = "fk_private_owned_asset") }
    )
    public Set<PrivateOwnedAsset> PrivateOwnedAssets;

    public double target_percentage;

    public String group_name;
}
