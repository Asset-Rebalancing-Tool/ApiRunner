package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Database.Entities.HoldingGroup;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.UUID;

/**
 * Record of a user having X amount of a registered public asset
 */
@Entity
@Table(indexes = {@Index(columnList = "user_uuid")})
public class PublicHolding extends BaseUserEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="asset_uuid")
    public PublicAsset public_asset;

    public double target_percentage;

    public double owned_quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "holding_group_uuid")
    public HoldingGroup HoldingGroup;

    public String custom_name;

    public boolean display_custom_name;

    @Enumerated(EnumType.STRING)
    public HoldingOrigin holding_origin;

    public Currency selected_currency;

    public UnitType selected_unit_type;

    public void setPublicAsset(UUID asset_uuid, JpaRepository<PublicAsset, UUID> repository){
        public_asset = repository.getReferenceById(asset_uuid);
    }

}
