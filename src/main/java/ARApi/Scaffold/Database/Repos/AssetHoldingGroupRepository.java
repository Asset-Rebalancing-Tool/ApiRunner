package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.HoldingGroup;
import liquibase.pro.packaged.Q;
import org.checkerframework.checker.lock.qual.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetHoldingGroupRepository extends JpaRepository<HoldingGroup, UUID> {

    @Query("select oag from HoldingGroup oag left join oag.privateHoldings left join oag.publicHoldings where oag.user.uuid = :user_uuid")
    List<HoldingGroup> GetByUserUuid(@Param("user_uuid") UUID userUuid);

    @Query("select hg from HoldingGroup hg left join hg.publicHoldings ph left join hg.privateHoldings left join ph.public_asset where hg.uuid = :group_uuid")
    Optional<HoldingGroup> GetDeep(@Param("group_uuid") UUID groupUuid);
}
