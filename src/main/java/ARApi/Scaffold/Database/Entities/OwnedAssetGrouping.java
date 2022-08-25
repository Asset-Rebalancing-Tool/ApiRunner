package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAsset;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;

import javax.persistence.*;
import java.util.List;

@Entity
public class OwnedAssetGrouping extends BaseUserEntity{

    @OneToMany
    public List<PublicOwnedAsset> OwnedPublicAssets;

    @OneToMany
    public List<PrivateAsset> OwnedPrivateAssets;

    public double target_percentage;

    public String group_name;
}
