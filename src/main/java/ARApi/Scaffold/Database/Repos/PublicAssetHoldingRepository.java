package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.HoldingOrigin;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Repository
public interface PublicAssetHoldingRepository extends JpaRepository<PublicHolding, UUID> {

    @Query("select count(opa) > 0 from PublicHolding opa where opa.public_asset.uuid = :asset_uuid and opa.user.uuid = :user_uuid and opa.holding_origin = :holding_origin")
    boolean holdingExists(@Param("asset_uuid") UUID assetUuid, @Param("user_uuid") UUID userUuid, @Param("holding_origin") HoldingOrigin holdingOrigin);

    @Nullable
    @Query("select opa from PublicHolding opa where opa.public_asset.uuid = :asset_uuid and opa.user.uuid = :user_uuid")
    PublicHolding tryGet(@Param("asset_uuid") UUID assetUuid, @Param("user_uuid") UUID userUuid);

    @Query("select opa from PublicHolding opa where opa.user.uuid = :user_uuid")
    List<PublicHolding> GetAssetsOfUser(@Param("user_uuid") UUID uuid);

    @Query("select poa from PublicHolding poa where poa.uuid in :uuids")
    List<PublicHolding> FindByUuids(@Param("uuids") List<UUID> uuids);
}
