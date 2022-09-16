package ARApi.Scaffold.Services;


import ARApi.Scaffold.Database.Entities.SearchRecord;
import ARApi.Scaffold.Database.Repos.SearchRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Documents triggered searches from the asset api for example and determines
 * if the search should be rerun
 */
@Service
@Scope("prototype")
public class SearchSupervisor {

    private final UUID contextUuid = UUID.randomUUID();

    private final SearchRecordRepository searchRecordRepository;

    @Autowired
    public SearchSupervisor(SearchRecordRepository searchRecordRepository) {
        this.searchRecordRepository = searchRecordRepository;
    }

    public boolean fetchPermitted(String searchString){
        var lastSearchOpt =searchRecordRepository.TryGetMostRecent(contextUuid, searchString);

        // save search always
        var searchRecord = new SearchRecord();
        searchRecord.search = searchString;
        searchRecord.context_uuid = contextUuid;
        searchRecordRepository.save(searchRecord);

        // not searched yet => permit
        return lastSearchOpt.isEmpty();
    }
}
