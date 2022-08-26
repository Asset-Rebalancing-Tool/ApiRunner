package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateAssetHoldingRepository extends JpaRepository<PrivateAssetHolding, UUID> {

    @Query("select poa from PrivateAssetHolding poa where poa.user_uuid = :user_uuid")
    List<PrivateAssetHolding> GetAssetsOfUser(@Param("user_uuid") UUID user_uuid);

    @Query("select poa from PrivateAssetHolding poa where poa.uuid in :uuids")
    List<PrivateAssetHolding> FindByUuids(@Param("uuids") List<UUID> uuids);
}
