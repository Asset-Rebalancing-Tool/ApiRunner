package ARApi.Scaffold.Shared.Enums;

import java.util.List;

public enum AssetType {
    Stock, Fond, Etf, Commodity, PreciousMetals, Crypto, Other;

    public static AssetType parse(String text) {
        for (AssetType b : AssetType.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return AssetType.Other;
    }


}
