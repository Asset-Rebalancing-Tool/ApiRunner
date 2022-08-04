package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Shared.AssetType;

import javax.persistence.*;
import java.util.List;

@Entity
public class OwnedPrivateAsset extends BaseUserEntity{

    public String title;

    @Enumerated(EnumType.STRING)
    public AssetType asset_type;

    @OneToMany(mappedBy= "Asset")
    public List<AssetPriceRecord> PriceRecords;

    public double owned_quantity;

    public double target_percentage;


}
