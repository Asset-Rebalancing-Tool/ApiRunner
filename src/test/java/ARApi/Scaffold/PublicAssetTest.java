package ARApi.Scaffold;


import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicAssetTest {

    @Autowired
    PublicAssetRepository publicAssetRepository;

    @Test
    void TestSearchHitIncrement(){
        var assets = publicAssetRepository.findAll();
        Assert.notEmpty(assets, "there should be assets in db");

        var as = assets.stream().findFirst().get();
        publicAssetRepository.IncreaseSearchHitCount(as.uuid);

        var updated = publicAssetRepository.findById(as.uuid).get();

        Assert.isTrue(updated.searchHitsTotal > as.searchHitsTotal, "not larger");
    }
}
