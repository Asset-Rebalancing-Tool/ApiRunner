package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PrivateAsset.OwnedPrivateAsset;
import ARApi.Scaffold.Database.Entities.PublicAsset.OwnedPublicAsset;

import javax.persistence.*;
import java.util.List;

@Entity
public class OwnedAssetGrouping extends BaseUserEntity{

    @OneToMany
    public List<OwnedPublicAsset> OwnedPublicAssets;

    @OneToMany
    public List<OwnedPrivateAsset> OwnedPrivateAssets;

    public double target_percentage;

    public String group_name;
}
