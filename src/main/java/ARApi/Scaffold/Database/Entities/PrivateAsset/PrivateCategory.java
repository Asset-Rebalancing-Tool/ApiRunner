package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;

import javax.persistence.Entity;

@Entity
public class PrivateCategory extends BaseUserEntity {

    public String category_name;
}
