package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublicAssetRepository extends JpaRepository<PublicAsset, UUID> {
}
