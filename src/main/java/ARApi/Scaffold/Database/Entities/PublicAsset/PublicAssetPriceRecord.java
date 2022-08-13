package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Shared.Enums.Currency;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
public class PublicAssetPriceRecord extends BaseEntity {

    @ManyToOne
    // set the column name where we store the uuid of the asset
    @JoinColumn(name = "asset_uuid")
    public PublicAsset Asset;

    public LocalDateTime ts_fetched = LocalDateTime.now();

    public LocalDateTime ts_price;

    public double price;

    @Enumerated(EnumType.STRING)
    public Currency currency;

    public LocalDateTime GetTimeOfPrice(){
        return ts_price;
    }

    public static Comparator<PublicAssetPriceRecord> GetComp(){
        return Comparator.comparing(PublicAssetPriceRecord::GetTimeOfPrice).reversed();
    }
}
