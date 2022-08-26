package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.AssetHoldingGrouping;

import java.util.List;

public class ModelAssetHoldingGrouping {

    public ModelAssetHoldingGrouping(AssetHoldingGrouping assetGrouping){
        groupName = assetGrouping.group_name;
        uuid = assetGrouping.uuid.toString();
        targetPercentage = assetGrouping.target_percentage;
        privateAssetHoldings = assetGrouping.PrivateAssetHoldings.stream().map(ModelPrivateAssetHolding::new).toList();
        publicAssetHoldings = assetGrouping.PublicAssetHoldings.stream().map(ModelPublicAssetHolding::new).toList();
    }

    public List<ModelPrivateAssetHolding> privateAssetHoldings;

    public List<ModelPublicAssetHolding> publicAssetHoldings;

    public String groupName;

    public double targetPercentage;

    public String uuid;
}
