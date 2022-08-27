package ARApi.Scaffold;


import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.HashMap;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class PublicAssetTest {

    @Autowired
    PublicAssetRepository publicAssetRepository;

    @Test
    void TestSearchHitIncrement(){
        publicAssetRepository.save(new PublicAsset());

        var as = publicAssetRepository.findAll().stream().findFirst().get();
        publicAssetRepository.IncreaseSearchHitCount(as.uuid);

        var updated = publicAssetRepository.findById(as.uuid).get();

        Assert.isTrue(updated.searchHitsTotal > as.searchHitsTotal, "not larger");
    }
}
