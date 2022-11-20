package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.SearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SearchRecordRepository extends JpaRepository<SearchRecord, UUID> {

    @Query("select sr from SearchRecord sr where sr.context_uuid = :context_uuid and sr.search = :search order by sr.ts_created desc")
    List<SearchRecord> TryGetMostRecent(@Param("context_uuid") UUID contextUuid, @Param("search") String search);
}
