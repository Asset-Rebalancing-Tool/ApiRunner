package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.*;
import ARApi.Scaffold.Endpoints.Model.*;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedAssetGroupingRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPrivateAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPublicAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: rename everything to possession...
@RestController
@Component
@RequestMapping("ownership_api")
public class OwnershipApi {

    private final User user;

    private final PrivateCategoryRepository privateCategoryRepository;

    private final UserRepository userRepository;

    private final OwnedAssetGroupingRepository ownedAssetGroupingRepository;

    private final PublicOwnedAssetRepository publicOwnedAssetRepository;

    private final PrivateOwnedAssetRepository privateOwnedAssetRepository;

    private final PublicAssetRepository publicAssetRepository;

    @Autowired
    public OwnershipApi(PrivateCategoryRepository privateCategoryRepository, UserRepository userRepository, OwnedAssetGroupingRepository ownedAssetGroupingRepository, PublicOwnedAssetRepository publicOwnedAssetRepository, PrivateOwnedAssetRepository privateOwnedAssetRepository, PublicAssetRepository publicAssetRepository) {
        this.privateCategoryRepository = privateCategoryRepository;
        this.userRepository = userRepository;
        this.ownedAssetGroupingRepository = ownedAssetGroupingRepository;
        this.publicOwnedAssetRepository = publicOwnedAssetRepository;
        this.privateOwnedAssetRepository = privateOwnedAssetRepository;
        this.publicAssetRepository = publicAssetRepository;

        // create temp user if not exists
        var users = userRepository.findAll();
        if (users.isEmpty()) {
            var user = new User();
            users.add(user);
            userRepository.saveAndFlush(user);
        }
        user = users.get(0);
    }

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelPrivateCategory PostPrivateCategory(@RequestBody PrivateCategoryRequest privateCategoryRequest) {
        if (privateCategoryRequest.categoryName == null || privateCategoryRequest.categoryName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryName is null or blank");
        }

        var privateCategory = privateCategoryRepository.save(privateCategoryRequest.toPrivateCategory(user.uuid));

        return new ModelPrivateCategory(privateCategory);
    }

    @GetMapping("/category")
    public List<ModelPrivateCategory> GetPrivateCategories() {

        var categories = privateCategoryRepository.findByUserUuid(user.uuid);

        return categories.stream().map(ModelPrivateCategory::new).toList();
    }

    @DeleteMapping("/category/{uuidStr}")
    public HttpStatus DeletePrivateCategory(@PathVariable String uuidStr) {
        var uuid = UUID.fromString(uuidStr);
        if (!privateCategoryRepository.existsById(uuid)) {
            return HttpStatus.NOT_FOUND;
        }

        privateCategoryRepository.deleteById(uuid);
        return HttpStatus.OK;
    }

    @PostMapping("/owned_asset/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelPublicOwnedAsset PostOwnedPublicAsset(@RequestBody PostOwnedPublicAssetRequest postOwnedPublicAssetRequest) {
        var uuid = UUID.fromString(postOwnedPublicAssetRequest.publicAssetUuid);
        if (publicOwnedAssetRepository.existsByPublicAssetUuid(uuid, user.uuid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Public owned asset already exists for user");
        }

        var publicOwnedAsset = publicOwnedAssetRepository.save(
                postOwnedPublicAssetRequest.toPublicOwnedAsset(user.uuid,
                        publicOwnedAssetRepository,
                        publicAssetRepository,
                        privateOwnedAssetRepository)
        );

        return new ModelPublicOwnedAsset(publicOwnedAsset);
    }

    @GetMapping("/owned_asset/public")
    public Set<ModelPublicOwnedAsset> GetOwnedPublicAssets() {
        return publicOwnedAssetRepository.GetAssetsOfUser(user.uuid).stream().map(ModelPublicOwnedAsset::new).collect(Collectors.toSet());
    }

    @PostMapping("/owned_asset/private")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelOwnedPrivateAsset PostOwnedPrivateAsset(@RequestBody PostOwnedPrivateAssetRequest postOwnedPrivateAssetRequest) {
        var privateOwnedAsset = privateOwnedAssetRepository.save(postOwnedPrivateAssetRequest.toPrivateOwnedAsset(user.uuid, privateOwnedAssetRepository, publicOwnedAssetRepository));
        return new ModelOwnedPrivateAsset(privateOwnedAsset);
    }

    @GetMapping("/owned_asset/private")
    public Set<ModelOwnedPrivateAsset> GetOwnedPrivateAssets() {
        return privateOwnedAssetRepository.GetAssetsOfUser(user.uuid).stream().map(ModelOwnedPrivateAsset::new).collect(Collectors.toSet());
    }

    @PostMapping("/owned_asset_grouping")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelOwnedAssetGrouping PostOwnedAssetGrouping(@RequestBody PostOwnedAssetGroupingRequest postOwnedAssetGroupingRequest) {
        var assetGrouping = ownedAssetGroupingRepository.save(postOwnedAssetGroupingRequest.toOwnedAssetGrouping(user.uuid, publicOwnedAssetRepository, privateOwnedAssetRepository));
        return new ModelOwnedAssetGrouping(assetGrouping);
    }

    @GetMapping("/owned_asset_group")
    public Set<ModelOwnedAssetGrouping> GetOwnedAssetGroupings() {
        return ownedAssetGroupingRepository.GetByUserUuid(user.uuid).stream().map(ModelOwnedAssetGrouping::new).collect(Collectors.toSet());
    }

    @DeleteMapping("/owned_asset_group/{groupingUuid}")
    public HttpStatus DeleteOwnedAssetGrouping(@PathVariable String groupingUuid) {
        var assetGroupingUuid = UUID.fromString(groupingUuid);
        ownedAssetGroupingRepository.deleteById(assetGroupingUuid);
        return HttpStatus.OK;
    }
}
