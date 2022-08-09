package ARApi.Scaffold;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Database.Entities.PublicAsset;
import ARApi.Scaffold.Services.Inserter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class AssetTest {

    @Autowired
    SessionFactory sessionFactory;

    @Test void Test(){
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


        var inserter = new Inserter<>(PublicAsset.class, sessionFactory.openSession());

        var firstResult = inserter.Insert(Stream.of(asset3isin1234).toList()
                , sessionFactory.openSession()).get(0);

        var secondResult = inserter.Insert(Stream.of(asset4isin1234).toList()
                , sessionFactory.openSession()).get(0);

        Assert.isTrue(firstResult.uuid.equals(secondResult.uuid), "should be same uuid since inserted with same isin");



    }
}
