package ARApi.Scaffold.Database.Entities.PrivateAsset;

import ARApi.Scaffold.Database.Entities.BaseUserEntity;

import javax.persistence.Entity;

/**
 * Private custom categorisation a user created
 */
@Entity
public class PrivateCategory extends BaseUserEntity {

    public String category_name;
}
