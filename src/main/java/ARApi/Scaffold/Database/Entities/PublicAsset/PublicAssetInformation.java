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
public class PublicAssetInformation extends BaseEntity {

    public PublicAssetInformation(AssetInformationType assetInformationType, Language language){
        asset_information_type = assetInformationType;
        this.language = language;
    }
    public PublicAssetInformation(AssetInformationType assetInformationType){
        asset_information_type = assetInformationType;
    }

    public PublicAssetInformation SetByteValueArray(Serializable serializable){

        byte_value_array = SerializationUtils.serialize(serializable);
        if(serializable == null){
            return null;
        }
        return this;
    }

    public PublicAssetInformation(){
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
