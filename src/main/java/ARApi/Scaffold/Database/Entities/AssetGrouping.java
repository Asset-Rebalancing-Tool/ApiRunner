package ARApi.Scaffold.Database.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class AssetGrouping extends BaseUserEntity{

    @OneToMany
    public List<OwnedPublicAsset> OwnedPublicAssets;

    @OneToMany
    public List<OwnedPrivateAsset> OwnedPrivateAssets;

    public double target_percentage;

    public String group_name;
}
