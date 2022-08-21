package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;
import ARApi.Scaffold.Database.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface PrivateCategoryRepository extends JpaRepository<PrivateCategory, UUID> {

    @Query("select pc from PrivateCategory pc where pc.user.uuid = :user_uuid")
    Collection<PrivateCategory> findByUserUuid(@Param("user_uuid") UUID userUuid);
}
