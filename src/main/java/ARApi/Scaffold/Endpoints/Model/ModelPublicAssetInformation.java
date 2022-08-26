package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetInformation;
import ARApi.Scaffold.Shared.Enums.AssetInformationType;
import org.springframework.util.SerializationUtils;

import java.text.NumberFormat;
import java.util.Locale;

public class ModelPublicAssetInformation {

    public ModelPublicAssetInformation(PublicAssetInformation publicAssetInformation){
        assetInformationType = publicAssetInformation.asset_information_type;
        stringValue = assetInformationType.GetString(publicAssetInformation.byte_value_array);
    }

    public AssetInformationType assetInformationType;

    public String stringValue;
}
