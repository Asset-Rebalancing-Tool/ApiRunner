package ARApi.Scaffold.Database.Entities;

import ARApi.Scaffold.Shared.AssetType;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Entity
public class PublicAsset extends BaseEntity {

    public String assetName;

    @Enumerated(EnumType.STRING)
    public AssetType assetType;

    @Column(unique = true)
    public String isin;

    public String symbol;

    // property of DbAssetPriceRecord => tells hibernate that this property is used for mapping
    @OneToMany(mappedBy= "Asset")
    public Set<AssetPriceRecord> AssetPriceRecords;

    @OneToMany(mappedBy= "Asset")
    public Set<PublicAssetInformation> AssetInformation;

    @Override
    public boolean equals(Object o){
        if(o instanceof PublicAsset && this.isin != null && ((PublicAsset) o).isin != null){
            var isin = ((PublicAsset) o).isin;
            return this.isin.toLowerCase(Locale.ROOT).equals(isin.toLowerCase(Locale.ROOT));
        }
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode(){
        if(isin != null){
            return isin.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public Set<BaseEntity> GetChildEntities(){
        Set<BaseEntity> children = new HashSet<>();
        if(AssetPriceRecords != null){
            children.addAll(AssetPriceRecords);
        }

        if(AssetInformation != null){
            children.addAll(AssetInformation);
        }
        return children;
    }

}
