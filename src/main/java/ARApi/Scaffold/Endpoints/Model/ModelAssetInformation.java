package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetInformation;
import ARApi.Scaffold.Shared.Enums.AssetInformationType;
import org.springframework.util.SerializationUtils;

import java.text.NumberFormat;
import java.util.Locale;

public class ModelAssetInformation {

    private String IsDouble(byte[] byteArray){
        return NumberFormat.getInstance(Locale.GERMAN).format((double)SerializationUtils.deserialize(byteArray));
    }
    private String IsString(byte[] byteArray){
        return (String) SerializationUtils.deserialize(byteArray);
    }
    private long IsLong(byte[] byteArray){
        return (long) SerializationUtils.deserialize(byteArray);
    }

    public ModelAssetInformation(PublicAssetInformation publicAssetInformation){
        assetInformationType = publicAssetInformation.asset_information_type;

        switch (assetInformationType){
            case MarketCapitalization -> stringValue = IsDouble(publicAssetInformation.byte_value_array);
            case Region, Sector, Industry -> stringValue = IsString(publicAssetInformation.byte_value_array);
            case NumberEmployees -> IsLong(publicAssetInformation.byte_value_array);
        }
    }
    public AssetInformationType assetInformationType;

    public String stringValue;
}
