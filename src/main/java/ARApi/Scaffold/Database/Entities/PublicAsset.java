package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Shared.AssetType;


import javax.persistence.*;
import java.util.List;

@Entity
public class PublicAsset extends BaseEntity {

    public String assetName;

    @Enumerated(EnumType.STRING)
    public AssetType assetType;

    @Column(unique = true)
    public String isin;

    public String symbol;

    // property of DbAssetPriceRecord => tells hibernate that this property is used for mapping
    @OneToMany(mappedBy= "Asset")
    public List<AssetPriceRecord> AssetPriceRecords;

    @OneToMany(mappedBy= "Asset")
    public List<PublicAssetInformation> AssetInformation;

}
