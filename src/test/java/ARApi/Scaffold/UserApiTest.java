package ARApi.Scaffold;

import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import ARApi.Scaffold.Endpoints.UserApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class UserApiTest {

    @Autowired
    UserApi userApi;

    @Test
    public void testPostCategory(){
        var postRequest = new PrivateCategoryRequest();
        postRequest.categoryName = "testcat";

        var response = userApi.PostPrivateCategory(postRequest);
    }

    @Test
    public void fetchCategoriesOfUser(){
        testPostCategory();
        var response = userApi.GetPrivateCategories();
    }
}
