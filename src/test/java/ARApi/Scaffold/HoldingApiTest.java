package ARApi.Scaffold;

import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import ARApi.Scaffold.Endpoints.HoldingApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class HoldingApiTest {

    @Autowired
    HoldingApi holdingApi;

    @Test
    public void testPostCategory(){
        var postRequest = new PrivateCategoryRequest();
        postRequest.categoryName = "testcat";

        var response = holdingApi.PostPrivateCategory(postRequest);
    }

    @Test
    public void fetchCategoriesOfUser(){
        testPostCategory();
        var response = holdingApi.GetPrivateCategories();
    }


}
