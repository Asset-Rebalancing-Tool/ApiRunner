package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.OwnedPublicAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublicOwnedAssetRepository extends JpaRepository<OwnedPublicAsset, UUID> {

    @Query("select count(opa) > 0 from OwnedPublicAsset opa where opa.public_asset.uuid = :asset_uuid")
    boolean existsByPublicAssetUuid(@Param("asset_uuid") UUID assetUuid);
}
