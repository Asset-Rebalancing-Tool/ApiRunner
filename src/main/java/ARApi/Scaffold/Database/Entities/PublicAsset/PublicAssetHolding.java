package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;
import ARApi.Scaffold.Shared.Enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.UUID;

/**
 * Record of a user having X amount of a registered public asset
 */
@Entity
public class PublicAssetHolding extends BaseUserEntity {

    @OneToOne
    @JoinColumn(name="asset_uuid")
    public PublicAsset public_asset;

    public double target_percentage;

    public double owned_quantity;

    public String custom_name;

    public boolean display_custom_name;

    public boolean broker_connected = false;

    public Currency currency;

    public void setPublicAsset(UUID asset_uuid, JpaRepository<PublicAsset, UUID> repository){
        public_asset = repository.getReferenceById(asset_uuid);
    }

}
