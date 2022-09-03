package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A publicly sold and registered asset.
 */
@Entity
public class PublicAsset extends BaseEntity {

    @NotNull
    public String asset_name;

    @NotNull
    public UnitType unit_type;

    @Enumerated(EnumType.STRING)
    @NotNull
    public AssetType asset_type;

    @Column(unique = true)
    public String isin;

    public String symbol;

    @Lob
    public byte[] icon;

    // property of DbAssetPriceRecord => tells hibernate that this property is used for mapping
    // cascade is used when saving and deleting
    @OneToMany(mappedBy= "Asset", cascade = CascadeType.ALL)
    public Set<AssetPriceRecord> AssetPriceRecords = new HashSet<>();

    @OneToMany(mappedBy= "Asset", cascade = CascadeType.ALL)
    public Set<AssetInformation> AssetInformation = new HashSet<>();

    public long searchHitsTotal = 1;

    public Set<Currency> getAvailableCurrencies(){
        return AssetPriceRecords.stream().map(apr -> apr.currency).collect(Collectors.toSet());
    }
}
