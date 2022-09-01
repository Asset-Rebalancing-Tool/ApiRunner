package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.AssetPriceRecord;
import ARApi.Scaffold.Shared.Enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ModelAssetPriceRecord {

    public ModelAssetPriceRecord(AssetPriceRecord assetPriceRecord) {
        tsPrice = assetPriceRecord.ts_price;
        price = assetPriceRecord.price;
        currency = assetPriceRecord.currency;
    }

    public ModelAssetPriceRecord(){

    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime tsPrice;

    public double price;

    public Currency currency;
}
