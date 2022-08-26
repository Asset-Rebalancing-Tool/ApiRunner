package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.OwnedAssetGrouping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OwnedAssetGroupingRepository extends JpaRepository<OwnedAssetGrouping, UUID> {

    @Query("select oag from OwnedAssetGrouping oag where oag.user_uuid = :user_uuid")
    List<OwnedAssetGrouping> GetByUserUuid(@Param("user_uuid") UUID userUuid);
}
