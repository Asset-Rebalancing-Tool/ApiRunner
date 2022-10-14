package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;
import ARApi.Scaffold.Database.Repos.UserRepository;

import java.util.UUID;

public class PrivateCategoryRequest {

    public String categoryName;

    public PrivateCategory toPrivateCategory(UUID user_uuid, UserRepository repository){
        PrivateCategory privateCategory = new PrivateCategory();
        privateCategory.SetUser(user_uuid, repository);
        privateCategory.category_name = categoryName;
        return privateCategory;
    }
}
