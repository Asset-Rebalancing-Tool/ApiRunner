package ARApi.Scaffold.Shared.Enums;

public class AssetTypeParser {
    public static AssetType parse(String text) {
        for (AssetType b : AssetType.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return AssetType.Other;
    }
}
