package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetPriceRecord;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import javax.annotation.Nullable;
import java.util.*;


public class ModelPublicAsset {

   public ModelPublicAsset(PublicAsset dbAsset){
      uuid = dbAsset.uuid.toString();
      assetName = dbAsset.asset_name;
      assetType = dbAsset.asset_type;
      isin = dbAsset.isin;
      symbol = dbAsset.symbol;

      var recordComp = Comparator.comparing(PublicAssetPriceRecord::GetTimeOfPrice).reversed();

      // create map
      dbAsset.AssetPriceRecords.stream().sorted(recordComp).forEach(pr -> {
         var modelPr = new ModelPublicAssetPriceRecord(pr);
         if(!currencyPriceRecordMap.containsKey(pr.currency)){
            currencyPriceRecordMap.put(pr.currency, new ArrayList<>());
         }
         currencyPriceRecordMap.get(pr.currency).add(modelPr);

      });
      assetInformations = dbAsset.AssetInformation.stream().map(ModelPublicAssetInformation::new).filter(mai -> mai.stringValue != null).toList();
   }

   public Map<Currency, List<ModelPublicAssetPriceRecord>> currencyPriceRecordMap = new HashMap<>();

   public List<ModelPublicAssetInformation> assetInformations;

   public String uuid;

   public String assetName;

   public AssetType assetType;

   @Nullable
   public String isin;

   @Nullable
   public String symbol;
}
