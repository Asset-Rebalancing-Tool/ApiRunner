package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;

import java.util.UUID;

public class ModelPrivateCategory {

    public String categoryName;

    public UUID categoryUuid;

    public ModelPrivateCategory(PrivateCategory privateCategory){
        this.categoryName = privateCategory.category_name;
        this.categoryUuid = privateCategory.uuid;
    }
}
