package ARApi.Scaffold;

import ARApi.Scaffold.Services.SearchSupervisor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class SearchSupervisorTest {

    @Autowired
    SearchSupervisor searchSupervisor;

    @Test
    public void TestFetchPermission(){
        Assert.isTrue(searchSupervisor.fetchPermitted("testsearch"), "first search should be allowed");

        Assert.isTrue(!searchSupervisor.fetchPermitted("testsearch"), "second search should not be allowed");
    }
}
