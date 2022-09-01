package ARApi.Scaffold.Database.Entities.PublicAsset;

import ARApi.Scaffold.Database.Entities.BaseEntity;
import ARApi.Scaffold.Shared.Enums.AssetInformationType;
import ARApi.Scaffold.Shared.Enums.Language;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Meta information of a publicly sold and registered asset.
 */
@Entity
public class AssetInformation extends BaseEntity {

    public AssetInformation(AssetInformationType assetInformationType){
        this.asset_information_type = assetInformationType;
    }

    public AssetInformation(AssetInformationType assetInformationType, Language language) {
        this.asset_information_type = assetInformationType;
        this.language = language;
    }

    public AssetInformation(){

    }

    public AssetInformation SetByteValueArray(Serializable serializable){

        byte_value_array = SerializationUtils.serialize(serializable);
        if(serializable == null){
            return null;
        }
        return this;
    }

    @ManyToOne
    @JoinColumn(name = "asset_uuid")
    public PublicAsset Asset;

    @Enumerated(EnumType.STRING)
    public AssetInformationType asset_information_type;

    public byte[] byte_value_array;

    @Enumerated(EnumType.STRING)
    public Language language;
}
