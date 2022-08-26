package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateOwnedAsset;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrivateOwnedAssetRepository extends JpaRepository<PrivateOwnedAsset, UUID> {

    @Query("select poa from PrivateOwnedAsset poa where poa.user_uuid = :user_uuid")
    List<PrivateOwnedAsset> GetAssetsOfUser(@Param("user_uuid") UUID user_uuid);

    @Query("select poa from PrivateOwnedAsset poa where poa.uuid in :uuids")
    List<PrivateOwnedAsset> FindByUuids(@Param("uuids") List<UUID> uuids);
}
