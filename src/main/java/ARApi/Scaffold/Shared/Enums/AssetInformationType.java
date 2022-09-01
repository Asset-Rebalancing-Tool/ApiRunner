package ARApi.Scaffold.Shared.Enums;

import ARApi.Scaffold.Database.Entities.PublicAsset.AssetInformation;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAssetInformation;
import org.springframework.util.SerializationUtils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Special information type keys used in {@link AssetInformation} and {@link ModelPublicAssetInformation}
 */
public enum AssetInformationType {
    MarketCapitalization, Sector, Industry, Region, NumberEmployees;

    private static String IsDouble(byte[] byteArray){
        return NumberFormat.getInstance(Locale.GERMAN).format((double) SerializationUtils.deserialize(byteArray));
    }
    private static String IsString(byte[] byteArray){
        return (String) SerializationUtils.deserialize(byteArray);
    }
    private static long IsLong(byte[] byteArray){
        return (long) SerializationUtils.deserialize(byteArray);
    }

    public String GetString(byte[] byteArray){
        switch (this){
            case MarketCapitalization -> {
                return NumberFormat.getInstance(Locale.GERMAN).format((double) SerializationUtils.deserialize(byteArray));
            }
            case Region, Sector, Industry -> {
                return (String) SerializationUtils.deserialize(byteArray);
            }
            case NumberEmployees ->{
                return Long.toString((long) SerializationUtils.deserialize(byteArray));
            }
        }
        throw new IllegalArgumentException(this.toString() + " type not defined for enum");
    }
}
