package ARApi.Scaffold;

import ARApi.Scaffold.Database.Repos.PublicOwnedAssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class OwnedAssetTest {


    @Autowired
    PublicOwnedAssetRepository publicOwnedAssetRepository;

    @Test
    void Test(){

    }
}
