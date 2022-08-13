package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inserts concurrent batch assets
 */
@Service
public class AssetInserterService {

    @Autowired
    public AssetInserterService(SessionFactory sessionFactory, StringProcessingService processingService) {
        this.sessionFactory = sessionFactory;
        this.processingService = processingService;

        var session = sessionFactory.openSession();
        session.createQuery("Select pa from PublicAsset pa " +
                "left join fetch pa.AssetPriceRecords apr " +
                "left join fetch pa.AssetInformation ai " +
                "where pa.isin is not null ", PublicAsset.class).stream().forEach(pa -> isinPublicAssetMap.put(pa.isin, pa));
    }

    private final SessionFactory sessionFactory;

    private final StringProcessingService processingService;

    private final Map<String, PublicAsset> isinPublicAssetMap = new HashMap<>();

    public synchronized List<PublicAsset> Insert(List<PublicAsset> assets){
        List<PublicAsset> assetsToReturn = new ArrayList<>();

        var session = sessionFactory.openSession();
        var tx = session.beginTransaction();
        for(var asset : assets){
            if(asset.isin != null){
                // check if asset with isin exists
                var processedIsin = processingService.Process(asset.isin);

                if(isinPublicAssetMap.containsKey(processedIsin)){
                    // return that if it exists
                    assetsToReturn.add(isinPublicAssetMap.get(processedIsin));
                    continue;
                }else{
                    // if not add this asset via isin, since it will now get saved
                    isinPublicAssetMap.put(processedIsin, asset);
                }
            }
            session.save(asset);
            // insert children aswell
            asset.AssetInformation.forEach(session::save);
            asset.AssetPriceRecords.forEach(session::save);

            assetsToReturn.add(asset);

        }
        tx.commit();
        session.close();

        return assetsToReturn;
    }
}
