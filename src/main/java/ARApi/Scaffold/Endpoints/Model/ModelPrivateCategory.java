package ARApi.Scaffold.Endpoints.Model;

import java.util.UUID;

public class ModelPrivateCategory {

    public String categoryName;

    public UUID categoryUuid;

    public ModelPrivateCategory(String categoryName, UUID categoryUuid) {
        this.categoryName = categoryName;
        this.categoryUuid = categoryUuid;
    }
}
