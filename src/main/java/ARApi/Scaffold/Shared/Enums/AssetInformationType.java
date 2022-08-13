package ARApi.Scaffold.Shared.Enums;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetInformation;
import ARApi.Scaffold.Endpoints.Model.ModelAssetInformation;

/**
 * Special information type keys used in {@link PublicAssetInformation} and {@link ModelAssetInformation}
 */
public enum AssetInformationType {
    MarketCapitalization, Sector, Industry, Region, NumberEmployees;
}
