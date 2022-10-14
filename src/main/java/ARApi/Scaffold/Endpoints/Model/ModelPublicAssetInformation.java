package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.AssetInformation;
import ARApi.Scaffold.Shared.Enums.AssetInformationType;

public class ModelPublicAssetInformation {

    public ModelPublicAssetInformation(AssetInformation assetInformation){
        assetInformationType = assetInformation.asset_information_type;
        stringValue = assetInformationType.GetString(assetInformation.byte_value_array);
    }

    public ModelPublicAssetInformation(){

    }

    public AssetInformationType assetInformationType;

    public String stringValue;
}
