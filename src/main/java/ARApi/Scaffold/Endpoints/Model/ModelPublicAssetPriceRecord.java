package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetPriceRecord;
import ARApi.Scaffold.Shared.Enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ModelPublicAssetPriceRecord {

    public ModelPublicAssetPriceRecord(PublicAssetPriceRecord assetPriceRecord) {
        tsPrice = assetPriceRecord.ts_price;
        price = assetPriceRecord.price;
        currency = assetPriceRecord.currency;
    }

    public ModelPublicAssetPriceRecord(){

    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime tsPrice;

    public double price;

    public Currency currency;
}
