package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Shared.Enums.AssetType;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PublicAsset extends BaseEntity {

    @Column(nullable = false)
    public String asset_name;

    @Enumerated(EnumType.STRING)
    public AssetType asset_type;

    @Column(unique = true)
    public String isin;

    public String symbol;

    // property of DbAssetPriceRecord => tells hibernate that this property is used for mapping
    @OneToMany(mappedBy= "Asset", cascade = CascadeType.ALL)
    public Set<PublicAssetPriceRecord> AssetPriceRecords = new HashSet<>();

    @OneToMany(mappedBy= "Asset", cascade = CascadeType.ALL)
    public Set<PublicAssetInformation> AssetInformation = new HashSet<>();

    public long searchHitsTotal = 1;
}
