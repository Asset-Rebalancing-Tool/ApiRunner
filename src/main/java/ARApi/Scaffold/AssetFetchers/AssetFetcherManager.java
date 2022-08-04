package ARApi.Scaffold.AssetFetchers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssetFetcherManager {
    private Set<IAssetFetcher> WorkingFetchers;

    @Autowired
    public AssetFetcherManager(List<IAssetFetcher> assetFetchers) {
        WorkingFetchers = new HashSet<IAssetFetcher>(assetFetchers);
    }

    public <T> T ExecuteWithFetcher (FetcherExecute<T> execute){
        while(!WorkingFetchers.isEmpty()){
            IAssetFetcher fetcher = GetFetcher();
            try{
                return execute.Execute(fetcher);
            }catch (Exception e){
                e.printStackTrace();
                WorkingFetchers.remove(fetcher);
                // TODO: CREATE BUG ITEM
            }
        }
        return null;
    }

    private IAssetFetcher GetFetcher(){
        if(WorkingFetchers.isEmpty()){
            return null;
        }
        return WorkingFetchers.stream().findAny().get();
    }
}
