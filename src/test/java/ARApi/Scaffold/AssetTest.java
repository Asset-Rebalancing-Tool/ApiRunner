package ARApi.Scaffold;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Services.AssetInserterService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.*;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class AssetTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    AssetInserterService inserterProvider;

    @Test void TestOverwrites(){
        var containsMap = new HashMap<PublicAsset, PublicAsset>();

        var asset1isinNull = new PublicAsset();
        var asset2isinNull = new PublicAsset();

        var asset3isin1234 = new PublicAsset();
        asset3isin1234.isin = "1234";
        var asset4isin1234 = new PublicAsset();
        asset4isin1234.isin = "1234";


        containsMap.put(asset1isinNull, asset1isinNull);
        Assert.isTrue(!containsMap.containsKey(asset2isinNull), "assets with both isin null should not match each other");

        containsMap.put(asset3isin1234, asset3isin1234);
        Assert.isTrue(containsMap.containsKey(asset4isin1234), "assets with same isin and both not null should match");
    }
}
