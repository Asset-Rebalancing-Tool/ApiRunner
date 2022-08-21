package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;
import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.PrivateCategoryRepository;
import ARApi.Scaffold.Database.Repos.PublicOwnedAssetRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Endpoints.Model.*;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedAssetGroupingRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPrivateAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPublicAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Component
@RequestMapping("user_api")
public class UserApi {

    private final User user;

    private final PrivateCategoryRepository privateCategoryRepository;

    private final UserRepository userRepository;

    private final PublicOwnedAssetRepository publicOwnedAssetRepository;

    @Autowired
    public UserApi(PrivateCategoryRepository privateCategoryRepository, UserRepository userRepository, PublicOwnedAssetRepository publicOwnedAssetRepository) {
        this.privateCategoryRepository = privateCategoryRepository;
        this.userRepository = userRepository;
        this.publicOwnedAssetRepository = publicOwnedAssetRepository;

        // create temp user if not exists
        var users = userRepository.findAll();
        if (users.isEmpty()){
            var user = new User();
            users.add(user);
            userRepository.saveAndFlush(user);
        }
        user = users.get(0);
    }

    @PostMapping("/category")
    public ModelResponse<ModelPrivateCategory> PostPrivateCategory(@RequestBody PrivateCategoryRequest privateCategoryRequest){
        if(privateCategoryRequest.categoryName == null || privateCategoryRequest.categoryName.isBlank()){
            return new ModelResponse<>("categoryName is null or blank", HttpStatus.BAD_REQUEST);
        }

        var privateCategory = new PrivateCategory();
        privateCategory.user = user;
        privateCategory.category_name = privateCategoryRequest.categoryName;

        privateCategoryRepository.saveAndFlush(privateCategory);

        return new ModelResponse<>(new ModelPrivateCategory(privateCategory), HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public ModelResponse<List<ModelPrivateCategory>> GetPrivateCategories(){

        var categories = privateCategoryRepository.findByUserUuid(user.uuid);

        return new ModelResponse<>(categories.stream().map(ModelPrivateCategory::new).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/category/{uuidStr}")
    public ResponseEntity<HttpStatus> DeletePrivateCategory(@PathVariable String uuidStr){
        var uuid = UUID.fromString(uuidStr);
        if(!privateCategoryRepository.existsById(uuid)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        privateCategoryRepository.deleteById(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/owned_asset/public")
    public ModelResponse<ModelOwnedPublicAsset> PostOwnedPublicAsset(@RequestBody PostOwnedPublicAssetRequest postOwnedPublicAssetRequest) {

        return new ModelResponse<>(HttpStatus.OK);
    }
    @GetMapping("/owned_asset/public")
    public ModelResponse<ModelOwnedPublicAsset[]> GetOwnedPublicAssets() {
        return new ModelResponse<>(HttpStatus.OK);
    }

    @PostMapping("/owned_asset/private")
    public ModelResponse<ModelOwnedPrivateAsset> PostPrivateAsset(@RequestBody PostOwnedPrivateAssetRequest postOwnedPrivateAssetRequest) {
        return new ModelResponse<>(HttpStatus.OK);
    }

    @GetMapping("/owned_asset/private")
    public ModelResponse<ModelOwnedPrivateAsset[]> GetPrivateAssets() {
        return new ModelResponse<>(HttpStatus.OK);
    }

    @PostMapping("/owned_asset_grouping")
    public ModelResponse<ModelOwnedAssetGrouping> PostOwnedAssetGrouping(@RequestBody PostOwnedAssetGroupingRequest postOwnedAssetGroupingRequest){
        // TODO: check if all assets, private aswell public have the same currency otherwise reject because calc wont work
        // only one currency is allowed
        return new ModelResponse<>(HttpStatus.OK);
    }

    @GetMapping("/owned_asset_group")
    public ModelResponse<ModelOwnedAssetGrouping[]> GetOwnedAssetGroupings(){
        return new ModelResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/owned_asset_group/{uuid}")
    public ResponseEntity<HttpStatus> DeleteOwnedAssetGrouping(@PathVariable String uuid){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
