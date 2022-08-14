package ARApi.Scaffold;


import ARApi.Scaffold.Database.Entities.PublicAssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class NamingTest {



    @Autowired
    PublicAssetRepository publicAssetRepository;

    @Test
    void Test(){
        int wait = 1;
        var as = publicAssetRepository.findByIsin("fb9f4b06-0d94-46b0-9114-007ea6c72aec");

        publicAssetRepository.IncreaseSearchHitCount(as.uuid);


    }
}
