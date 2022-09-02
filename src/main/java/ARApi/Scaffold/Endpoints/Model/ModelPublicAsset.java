package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.AssetPriceRecord;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import javax.annotation.Nullable;
import java.util.*;


public class ModelPublicAsset {

   public ModelPublicAsset(){

   }

   public ModelPublicAsset(PublicAsset dbAsset){
      uuid = dbAsset.uuid.toString();
      assetName = dbAsset.asset_name;
      assetType = dbAsset.asset_type;
      isin = dbAsset.isin;
      symbol = dbAsset.symbol;

      var recordComp = Comparator.comparing(AssetPriceRecord::GetTimeOfPrice).reversed();

      // create map
      assetPriceRecords = dbAsset.AssetPriceRecords.stream().sorted(recordComp).map(ModelAssetPriceRecord::new).toList();
      assetInformation = dbAsset.AssetInformation.stream().map(ModelPublicAssetInformation::new).filter(mai -> mai.stringValue != null).toList();
      availableUnitTypes = dbAsset.unit_type.GetConvertibleUnitTypes();
      availableCurrencies = dbAsset.getAvailableCurrencies().toArray(Currency[]::new);
      iconBase64 = dbAsset.icon != null ? Base64.getEncoder().encodeToString(dbAsset.icon) : null;
   }

   public List<ModelAssetPriceRecord> assetPriceRecords;

   public List<ModelPublicAssetInformation> assetInformation;

   public String uuid;

   public String assetName;

   public AssetType assetType;

   @Nullable
   public String isin;

   @Nullable
   public String symbol;

   public UnitType[] availableUnitTypes;

   public Currency[] availableCurrencies;

   public String iconBase64;
}
