package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.AssetHoldingGroup;

import java.util.List;

public class ModelAssetHoldingGroup {

    public ModelAssetHoldingGroup(AssetHoldingGroup assetHoldingGroup){
        groupName = assetHoldingGroup.group_name;
        uuid = assetHoldingGroup.uuid.toString();
        targetPercentage = assetHoldingGroup.target_percentage;
        privateHoldings = assetHoldingGroup.privateHoldings.stream().map(ModelPrivateHolding::new).toList();
        publicHoldings = assetHoldingGroup.publicHoldings.stream().map(ModelPublicHolding::new).toList();
        targetPercentagesAddUp = assetHoldingGroup.InternalPercentagesMatch();
    }

    public ModelAssetHoldingGroup(){

    }

    public boolean targetPercentagesAddUp;

    public List<ModelPrivateHolding> privateHoldings;

    public List<ModelPublicHolding> publicHoldings;

    public String groupName;

    public double targetPercentage;

    public String uuid;
}
