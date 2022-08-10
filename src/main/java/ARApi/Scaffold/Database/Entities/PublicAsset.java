package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Shared.AssetType;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    public Set<AssetPriceRecord> AssetPriceRecords = new HashSet<>();

    @OneToMany(mappedBy= "Asset")
    public Set<PublicAssetInformation> AssetInformation= new HashSet<>();

    public long searchHitsTotal = 1;
}
