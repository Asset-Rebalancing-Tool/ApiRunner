package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository

public interface PublicAssetRepository extends JpaRepository<PublicAsset, UUID> {

    @Query("Select pa from PublicAsset pa " +
            "left join fetch pa.AssetPriceRecords apr " +
            "left join fetch pa.AssetInformation ai " +
            "where pa.isin is not null ")
    Collection<PublicAsset> GetFullIsinAssets();

    @Query("Select pa from PublicAsset pa " +
            "left join fetch pa.AssetPriceRecords apr " +
            "left join fetch pa.AssetInformation ai")
    Collection<PublicAsset> GetFullAssets();

    @EntityGraph(attributePaths = { "AssetPriceRecords", "AssetInformation"})
    PublicAsset findByIsin(String isin);

    @Modifying
    @Transactional
    @Query("Update PublicAsset p " +
            "set p.searchHitsTotal = p.searchHitsTotal + 1 " +
            "where p.uuid = :uuid")
    void IncreaseSearchHitCount(@Param("uuid") UUID uuid);
}
