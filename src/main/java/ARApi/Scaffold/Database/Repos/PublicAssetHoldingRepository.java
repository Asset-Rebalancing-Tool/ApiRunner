package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Repository
public interface PublicAssetHoldingRepository extends JpaRepository<PublicAssetHolding, UUID> {

    @Query("select count(opa) > 0 from PublicAssetHolding opa where opa.public_asset.uuid = :asset_uuid and opa.user.uuid = :user_uuid")
    boolean existsByPublicAssetUuid(@Param("asset_uuid") UUID assetUuid, @Param("user_uuid") UUID userUuid);

    @Nullable
    @Query("select opa from PublicAssetHolding opa where opa.public_asset.uuid = :asset_uuid and opa.user.uuid = :user_uuid")
    PublicAssetHolding tryGet(@Param("asset_uuid") UUID assetUuid, @Param("user_uuid") UUID userUuid);

    @Query("select opa from PublicAssetHolding opa where opa.user.uuid = :user_uuid")
    List<PublicAssetHolding> GetAssetsOfUser(@Param("user_uuid") UUID uuid);

    @Query("select poa from PublicAssetHolding poa where poa.uuid in :uuids")
    List<PublicAssetHolding> FindByUuids(@Param("uuids") List<UUID> uuids);
}
