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
      priceRecords = dbAsset.AssetPriceRecords.stream().sorted(recordComp).map(ModelAssetPriceRecord::new).toList();

      assetInformations = dbAsset.AssetInformation.stream().map(ModelAssetInformation::new).filter(mai -> mai.stringValue != null).toList();
   }

   public List<ModelAssetPriceRecord> priceRecords = new ArrayList<>();

   public Map<Currency, List<ModelAssetPriceRecord>> currencyPriceRecordMap = new HashMap<>();

   public List<ModelAssetInformation> assetInformations = new ArrayList<>();

   public String uuid;

   public String assetName;

   public AssetType assetType;

   @Nullable
   public String isin;

   @Nullable
   public String symbol;

   public Currency currency;

   public UnitType priceType;

}
