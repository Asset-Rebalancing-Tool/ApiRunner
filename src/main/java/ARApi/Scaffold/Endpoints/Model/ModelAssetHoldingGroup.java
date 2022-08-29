package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.AssetHoldingGroup;

import java.util.List;

public class ModelAssetHoldingGroup {

    public ModelAssetHoldingGroup(AssetHoldingGroup assetHoldingGroup){
        groupName = assetHoldingGroup.group_name;
        uuid = assetHoldingGroup.uuid.toString();
        targetPercentage = assetHoldingGroup.target_percentage;
        privateAssetHoldings = assetHoldingGroup.PrivateAssetHoldings.stream().map(ModelPrivateAssetHolding::new).toList();
        publicAssetHoldings = assetHoldingGroup.PublicAssetHoldings.stream().map(ModelPublicAssetHolding::new).toList();
        targetPercentagesAddUp = assetHoldingGroup.InternalPercentagesMatch();
    }

    public ModelAssetHoldingGroup(){

    }

    public boolean targetPercentagesAddUp;

    public List<ModelPrivateAssetHolding> privateAssetHoldings;

    public List<ModelPublicAssetHolding> publicAssetHoldings;

    public String groupName;

    public double targetPercentage;

    public String uuid;
}
