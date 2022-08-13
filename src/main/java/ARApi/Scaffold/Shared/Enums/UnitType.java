package ARApi.Scaffold.Shared.Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UnitType {
    Piece, Ounce, Grams, Kilos, Litres, Unknown;

    public UnitType GetUnitTypeIfKnown(AssetType assetType){
        switch (assetType){
            case Etf, Fond, Stock, Crypto -> {
                return Piece;
            }
            default -> {
                return Unknown;
            }
        }
    }

    public UnitType[] GetConvertibleUnitTypes(UnitType unitType){
        UnitType[][] convertibleTypeGroups = new UnitType[][]{
                new UnitType[]{Ounce, Grams, Kilos}
        };

        return Arrays.stream(convertibleTypeGroups).filter(ut -> Arrays.asList(ut).contains(unitType)).findFirst().orElse(new UnitType[]{});
    }

    public List<UnitType> GetAvailableUnitTypes(AssetType assetType){
        switch (assetType){
            case Etf, Fond, Stock, Crypto -> {
                return List.of(Piece);
            }
            case PreciousMetals -> {
                return List.of(Grams, Ounce);
            }
            case Commodity -> {
                return List.of(Piece, Ounce, Grams, Kilos, Litres);
            }
            case Other -> {
                return List.of(UnitType.values());
            }
            default -> throw new IllegalStateException("No mapping for asset type: " + assetType.name() + " defined");
        }
    }
}
