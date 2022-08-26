package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.AssetHoldingGrouping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssetHoldingGroupingRepository extends JpaRepository<AssetHoldingGrouping, UUID> {

    @Query("select oag from AssetHoldingGrouping oag where oag.user.uuid = :user_uuid")
    List<AssetHoldingGrouping> GetByUserUuid(@Param("user_uuid") UUID userUuid);
}
