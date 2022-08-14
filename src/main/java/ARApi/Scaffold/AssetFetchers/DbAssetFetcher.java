package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

/**
 * Fetches assets only from a specific database
 */
public class DbAssetFetcher implements IPublicAssetFetcher {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    DriverManagerDataSource ds;

    @Override
    public List<PublicAsset> FetchViaSearchString(String searchString) {
        return null;
    }

    @Override
    public PublicAsset FetchViaIsin(String isin) {
        return null;
    }

}
