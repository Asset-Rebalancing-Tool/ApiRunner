package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;

import java.util.UUID;

public class PrivateCategoryRequest {

    public String categoryName;

    public PrivateCategory toPrivateCategory(UUID user_uuid){
        PrivateCategory privateCategory = new PrivateCategory();
        privateCategory.user_uuid = user_uuid;
        privateCategory.category_name = categoryName;
        return privateCategory;
    }
}
