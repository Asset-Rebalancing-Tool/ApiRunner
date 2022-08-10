package ARApi.Scaffold.Services;

import ARApi.Scaffold.Database.Entities.PublicAsset;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public class PublicAssetInserter extends AbstractInserter<PublicAsset> {

    public PublicAssetInserter(String query, SessionFactory provider, ReentrantLock lock) {
        super(PublicAsset.class, query, provider, lock);
    }

    public PublicAssetInserter(SessionFactory provider, ReentrantLock lock) {
        super(PublicAsset.class, provider, lock);
    }

    @Override
    protected List<PublicAsset> Insert(List<PublicAsset> entitiesToInsert, Session autoCCOpenendSession) {
        List<PublicAsset> baseEntityReturn = new ArrayList<>();

        for (var entityToInsert : entitiesToInsert){
            // check for isin match
            if(entityToInsert.isin != null){
                var existingAssetIsin = insertedEntities.stream().filter(ent -> ent.isin != null && ent.isin.toLowerCase(Locale.ROOT).trim().equals(
                        entityToInsert.isin.toLowerCase(Locale.ROOT).trim())).findAny();

                if(existingAssetIsin.isPresent()){
                    baseEntityReturn.add(existingAssetIsin.get());
                    continue;
                }
            }

            // insert it yourself
            autoCCOpenendSession.save(entityToInsert);
            // insert children aswell
            entityToInsert.AssetInformation.forEach(autoCCOpenendSession::save);
            entityToInsert.AssetPriceRecords.forEach(autoCCOpenendSession::save);
            insertedEntities.add(entityToInsert);
            baseEntityReturn.add(entityToInsert);
        }

        return baseEntityReturn;
    }
}
