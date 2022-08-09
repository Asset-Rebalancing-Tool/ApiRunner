package ARApi.Scaffold;

import ARApi.Scaffold.Database.Entities.PublicAsset;
import ARApi.Scaffold.Services.Inserter;
import ARApi.Scaffold.Services.InserterProvider;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@ContextConfiguration("/test.xml")
@ExtendWith(SpringExtension.class)
public class AssetTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    InserterProvider inserterProvider;

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

    @Test void TestInserter() throws ExecutionException, InterruptedException {
        var exe = Executors.newFixedThreadPool(10);

        var isinQueue = new ArrayDeque<>(Arrays.asList("tes", "!3123123", "eafasd", "aedsdasd"));

        var inserter = new Inserter<>(PublicAsset.class, sessionFactory, inserterProvider.GetLock(PublicAsset.class));

        List<List<PublicAsset>> listofListsToInsert = new ArrayList<>();

        for(int i_total_lists = 0; i_total_lists < 100; i_total_lists++){
            var list = new ArrayList<PublicAsset>();
            for(int i_list = 0; i_list < 100; i_list++){
                var isin = isinQueue.removeFirst();
                var asset = new PublicAsset();
                asset.isin = isin;
                list.add(asset);
                isinQueue.addLast(isin);
            }
            listofListsToInsert.add(list);

        }

        var futures = listofListsToInsert.stream().map(list -> exe.submit(() -> inserter.Insert(list))).toList();

        for(var future : futures){
            var insertedAssets = future.get();
            Assert.isTrue(insertedAssets.stream().noneMatch(publicAsset -> publicAsset.uuid == null),
                    "all inserted assets need to return uuid");
        }

        Assert.isTrue(listofListsToInsert.stream().anyMatch(list -> list.stream().anyMatch(asset -> asset.uuid == null)),
                "some duplicates have to have no uuid");
    }
}
