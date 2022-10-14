package ARApi.Scaffold.Shared.Enums;

import java.util.Arrays;
import java.util.List;

public enum UnitType {
    Piece, Ounce, Grams, Kilos, Liters, Milliliters, Unknown;

    /**
     * Some unit types can be converted to other unit types
     * @return convertible types
     */
    public UnitType[] GetConvertibleUnitTypes(){
        UnitType[][] convertibleTypeGroups = new UnitType[][]{
                new UnitType[]{Ounce, Grams, Kilos},
                new UnitType[]{Liters, Milliliters}
        };

        return Arrays.stream(convertibleTypeGroups).filter(ut -> Arrays.asList(ut).contains(this)).findFirst().orElse(new UnitType[]{this});
    }

    /**
     * Different {@link AssetType} have different {@link UnitType}s available to
     * them based on their nature.
     * @param assetType
     * @return
     */
    public static List<UnitType> AvailableForAssetType(AssetType assetType){
        switch (assetType){
            case Etf, Fond, Stock, Crypto -> {
                return List.of(Piece);
            }
            case PreciousMetals -> {
                return List.of(Grams, Ounce);
            }
            case Commodity -> {
                return List.of(Piece, Kilos, Ounce, Grams, Liters, Milliliters);
            }
            case Other -> {
                return List.of(UnitType.values());
            }
            default -> throw new IllegalStateException("No mapping for asset type: " + assetType.name() + " defined");
        }
    }
}
