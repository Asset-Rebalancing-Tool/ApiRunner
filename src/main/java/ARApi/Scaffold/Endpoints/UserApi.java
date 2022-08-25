package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateCategory;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;
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
import org.springframework.web.server.ResponseStatusException;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ModelPrivateCategory PostPrivateCategory(@RequestBody PrivateCategoryRequest privateCategoryRequest){
        if(privateCategoryRequest.categoryName == null || privateCategoryRequest.categoryName.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryName is null or blank");
        }

        var privateCategory = new PrivateCategory();
        privateCategory.user = user;
        privateCategory.category_name = privateCategoryRequest.categoryName;

        privateCategoryRepository.saveAndFlush(privateCategory);

        return new ModelPrivateCategory(privateCategory);
    }

    @GetMapping("/category")
    public List<ModelPrivateCategory> GetPrivateCategories(){

        var categories = privateCategoryRepository.findByUserUuid(user.uuid);

        return categories.stream().map(ModelPrivateCategory::new).toList();
    }

    @DeleteMapping("/category/{uuidStr}")
    public HttpStatus DeletePrivateCategory(@PathVariable String uuidStr){
        var uuid = UUID.fromString(uuidStr);
        if(!privateCategoryRepository.existsById(uuid)){
            return HttpStatus.NOT_FOUND;
        }

        privateCategoryRepository.deleteById(uuid);
        return HttpStatus.OK;
    }

    @PostMapping("/owned_asset/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelOwnedPublicAsset PostOwnedPublicAsset(@RequestBody PostOwnedPublicAssetRequest postOwnedPublicAssetRequest) {
        var uuid = UUID.fromString(postOwnedPublicAssetRequest.publicAssetUuid);
        if(publicOwnedAssetRepository.existsByPublicAssetUuid(uuid, user.uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Public owned asset already exists for user");
        }

        var publicOwnedAsset = new PublicOwnedAsset();

        return new ModelOwnedPublicAsset();
    }
    @GetMapping("/owned_asset/public")
    public ModelOwnedPublicAsset[] GetOwnedPublicAssets() {
        return new ModelOwnedPublicAsset[]{};
    }

    @PostMapping("/owned_asset/private")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelOwnedPrivateAsset PostPrivateAsset(@RequestBody PostOwnedPrivateAssetRequest postOwnedPrivateAssetRequest) {

        return new ModelOwnedPrivateAsset();
    }

    @GetMapping("/owned_asset/private")
    public ModelOwnedPrivateAsset[] GetPrivateAssets() {
        return new ModelOwnedPrivateAsset[]{};
    }

    @PostMapping("/owned_asset_grouping")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelOwnedAssetGrouping PostOwnedAssetGrouping(@RequestBody PostOwnedAssetGroupingRequest postOwnedAssetGroupingRequest){
        // TODO: check if all assets, private aswell public have the same currency otherwise reject because calc wont work
        // only one currency is allowed
        return new ModelOwnedAssetGrouping();
    }

    @GetMapping("/owned_asset_group")
    public ModelOwnedAssetGrouping[] GetOwnedAssetGroupings(){
        return new ModelOwnedAssetGrouping[]{};
    }

    @DeleteMapping("/owned_asset_group/{uuid}")
    public HttpStatus DeleteOwnedAssetGrouping(@PathVariable String uuid){
        return HttpStatus.OK;
    }
}
