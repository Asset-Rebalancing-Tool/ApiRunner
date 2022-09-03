package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;

public class ModelPrivateCategory {

    public String categoryName;

    public String categoryUuid;

    public ModelPrivateCategory(PrivateCategory privateCategory){
        this.categoryName = privateCategory.category_name;
        this.categoryUuid = privateCategory.uuid.toString();
    }
    public ModelPrivateCategory(){

    }
}
