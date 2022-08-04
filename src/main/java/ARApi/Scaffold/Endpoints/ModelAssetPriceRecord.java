package ARApi.Scaffold.Endpoints;

import ARApi.Scaffold.Database.Entities.AssetPriceRecord;
import ARApi.Scaffold.Shared.Currency;
import ARApi.Scaffold.Shared.PriceType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ModelAssetPriceRecord {

    public ModelAssetPriceRecord(AssetPriceRecord assetPriceRecord){
        Currency = assetPriceRecord.currency;
        tsPrice = assetPriceRecord.ts_price;
        price = assetPriceRecord.price;
        priceType = assetPriceRecord.price_type;
    }

    public Currency Currency;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime tsPrice;
    public double price;
    public PriceType priceType;
}
