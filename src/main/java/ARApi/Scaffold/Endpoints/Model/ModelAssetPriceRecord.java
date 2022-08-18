package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetPriceRecord;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ModelAssetPriceRecord {

    public ModelAssetPriceRecord(PublicAssetPriceRecord assetPriceRecord) {
        tsPrice = assetPriceRecord.ts_price;
        price = assetPriceRecord.price;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime tsPrice;

    public double price;


}
