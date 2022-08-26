package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PublicOwnedAssetRepository extends JpaRepository<PublicOwnedAsset, UUID> {

    @Query("select count(opa) > 0 from PublicOwnedAsset opa where opa.public_asset.uuid = :asset_uuid and opa.user.uuid = :user_uuid")
    boolean existsByPublicAssetUuid(@Param("asset_uuid") UUID assetUuid, @Param("user_uuid") UUID userUuid);

    @Nullable
    @Query("select opa from PublicOwnedAsset opa where opa.public_asset.uuid = :asset_uuid and opa.user.uuid = :user_uuid")
    PublicOwnedAsset tryGet(@Param("asset_uuid") UUID assetUuid, @Param("user_uuid") UUID userUuid);

    @Query("select opa from PublicOwnedAsset opa where opa.user_uuid = :user_uuid")
    List<PublicOwnedAsset> GetAssetsOfUser(@Param("user_uuid") UUID uuid);

    @Query("select poa from PublicOwnedAsset poa where poa.uuid in :uuids")
    List<PublicOwnedAsset> FindByUuids(@Param("uuids") List<UUID> uuids);
}
