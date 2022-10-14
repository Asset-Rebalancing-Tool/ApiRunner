package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.HoldingGroup;

import java.util.List;

public class ModelHoldingGroup {

    public ModelHoldingGroup(HoldingGroup holdingGroup){
        groupName = holdingGroup.group_name;
        uuid = holdingGroup.uuid.toString();
        targetPercentage = holdingGroup.target_percentage;
        privateHoldings = holdingGroup.privateHoldings.stream().map(ModelPrivateHolding::new).toList();
        publicHoldings = holdingGroup.publicHoldings.stream().map(ModelPublicHolding::new).toList();
        targetPercentagesAddUp = holdingGroup.InternalPercentagesMatch();
    }

    public ModelHoldingGroup(){

    }

    public boolean targetPercentagesAddUp;

    public List<ModelPrivateHolding> privateHoldings;

    public List<ModelPublicHolding> publicHoldings;

    public String groupName;

    public double targetPercentage;

    public String uuid;
}
