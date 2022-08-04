package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Shared.AssetType;
import ARApi.Scaffold.Shared.Currency;
import ARApi.Scaffold.Shared.PriceType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
public class AssetPriceRecord extends BaseEntity{

    @ManyToOne
    // set the column name where we store the uuid of the asset
    @JoinColumn(name = "asset_uuid")
    public PublicAsset Asset;

    @Enumerated(EnumType.STRING)
    public Currency currency;

    public LocalDateTime ts_fetched = LocalDateTime.now();

    public LocalDateTime ts_price;

    public double price;

    @Enumerated(EnumType.STRING)
    public PriceType price_type;

    public LocalDateTime GetTimeOfPrice(){
        return ts_price;
    }


    public static Comparator<AssetPriceRecord> GetComp(){
        return Comparator.comparing(AssetPriceRecord::GetTimeOfPrice).reversed();
    }
}
