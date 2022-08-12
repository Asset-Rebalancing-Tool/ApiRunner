package ARApi.Scaffold.Database.Entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class PrivateAssetPriceRecord extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "private_asset_uuid")
    public OwnedPrivateAsset PrivateAsset;
}
