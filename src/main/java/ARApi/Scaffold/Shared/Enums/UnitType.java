package ARApi.Scaffold.Shared.Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UnitType {
    Piece, Ounce, Grams, Kilos, Liters, Milliliters, Unknown;

    // TODO: define properly
    public UnitType GetUnitTypeIfKnown(AssetType assetType){
        var availableTypes = GetAvailableUnitTypes(assetType);
        if(availableTypes.size() == 1){
            return availableTypes.get(0);
        }
        return Unknown;
    }

    /**
     * Some unit types can be converted to other unit types
     * @param unitType
     * @return
     */
    public static UnitType[] GetConvertibleUnitTypes(UnitType unitType){
        UnitType[][] convertibleTypeGroups = new UnitType[][]{
                new UnitType[]{Ounce, Grams, Kilos},
                new UnitType[]{Liters, Milliliters}
        };

        return Arrays.stream(convertibleTypeGroups).filter(ut -> Arrays.asList(ut).contains(unitType)).findFirst().orElse(new UnitType[]{});
    }

    /**
     * Different {@link AssetType} have different {@link UnitType}s available to
     * them based on their nature.
     * @param assetType
     * @return
     */
    public static List<UnitType> GetAvailableUnitTypes(AssetType assetType){
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
