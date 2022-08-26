package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.OwnedAssetGrouping;

import java.util.List;

public class ModelOwnedAssetGrouping {

    public ModelOwnedAssetGrouping(OwnedAssetGrouping assetGrouping){
        groupName = assetGrouping.group_name;
        uuid = assetGrouping.uuid.toString();
        targetPercentage = assetGrouping.target_percentage;
        ownedPrivateAsset = assetGrouping.PrivateOwnedAssets.stream().map(ModelOwnedPrivateAsset::new).toList();
        ownedPublicAsset = assetGrouping.PublicOwnedAssets.stream().map(ModelPublicOwnedAsset::new).toList();
    }

    public List<ModelOwnedPrivateAsset> ownedPrivateAsset;

    public List<ModelPublicOwnedAsset> ownedPublicAsset;

    public String groupName;

    public double targetPercentage;

    public String uuid;
}
