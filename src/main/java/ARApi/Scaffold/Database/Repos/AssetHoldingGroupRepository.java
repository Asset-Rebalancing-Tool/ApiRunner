package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.AssetHoldingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssetHoldingGroupRepository extends JpaRepository<AssetHoldingGroup, UUID> {

    @Query("select oag from AssetHoldingGroup oag where oag.user.uuid = :user_uuid")
    List<AssetHoldingGroup> GetByUserUuid(@Param("user_uuid") UUID userUuid);
}
