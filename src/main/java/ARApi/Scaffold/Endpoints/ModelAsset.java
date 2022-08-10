package ARApi.Scaffold.Endpoints;

import ARApi.Scaffold.Database.Entities.AssetPriceRecord;
import ARApi.Scaffold.Database.Entities.PublicAsset;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;


public class ModelAsset {

   public ModelAsset(PublicAsset dbAsset){
      uuid = dbAsset.uuid.toString();
      assetName = dbAsset.assetName;
      assetType = dbAsset.assetType;
      isin = dbAsset.isin;
      symbol = dbAsset.symbol;

      var recordComp = Comparator.comparing(AssetPriceRecord::GetTimeOfPrice).reversed();
      priceRecords = dbAsset.AssetPriceRecords.stream().sorted(recordComp).map(ModelAssetPriceRecord::new).toList();

      assetInformations = dbAsset.AssetInformation.stream().map(ModelAssetInformation::new).filter(mai -> mai.stringValue != null).toList();

   }

   public List<ModelAssetPriceRecord> priceRecords;

   public List<ModelAssetInformation> assetInformations;

   public String uuid;

   public String assetName;

   public ARApi.Scaffold.Shared.AssetType assetType;

   @Nullable
   public String isin;

   @Nullable
   public String symbol;

}
