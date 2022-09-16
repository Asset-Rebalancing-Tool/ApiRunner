package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateAssetHoldingRepository extends JpaRepository<PrivateHolding, UUID> {

    @Query("select poa from PrivateHolding poa where poa.user.uuid = :user_uuid")
    List<PrivateHolding> GetAssetsOfUser(@Param("user_uuid") UUID user_uuid);

    @Query("select poa from PrivateHolding poa where poa.uuid in :uuids")
    List<PrivateHolding> FindByUuids(@Param("uuids") List<UUID> uuids);
}
