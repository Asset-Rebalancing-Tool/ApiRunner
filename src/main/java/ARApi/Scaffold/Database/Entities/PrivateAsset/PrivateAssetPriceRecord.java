package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PrivateAssetPriceRecord extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "private_asset_uuid")
    public OwnedPrivateAsset PrivateAsset;
}
