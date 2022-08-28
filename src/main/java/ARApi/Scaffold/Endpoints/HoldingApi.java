package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.*;
import ARApi.Scaffold.Endpoints.Model.*;
import ARApi.Scaffold.Endpoints.Requests.PostAssetHoldingGroupingRequest;
import ARApi.Scaffold.Endpoints.Requests.PostPrivateAssetHoldingRequest;
import ARApi.Scaffold.Endpoints.Requests.PostPublicAssetHoldingRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("holding_api")

public class HoldingApi {
    
    private final PrivateCategoryRepository privateCategoryRepository;

    private final UserRepository userRepository;

    private final AssetHoldingGroupingRepository assetHoldingGroupingRepository;

    private final PublicAssetHoldingRepository publicAssetHoldingRepository;

    private final PrivateAssetHoldingRepository privateAssetHoldingRepository;

    private final PublicAssetRepository publicAssetRepository;

    @Autowired
    public HoldingApi(PrivateCategoryRepository privateCategoryRepository, UserRepository userRepository, AssetHoldingGroupingRepository assetHoldingGroupingRepository, PublicAssetHoldingRepository publicAssetHoldingRepository, PrivateAssetHoldingRepository privateAssetHoldingRepository, PublicAssetRepository publicAssetRepository) {
        this.privateCategoryRepository = privateCategoryRepository;
        this.userRepository = userRepository;
        this.assetHoldingGroupingRepository = assetHoldingGroupingRepository;
        this.publicAssetHoldingRepository = publicAssetHoldingRepository;
        this.privateAssetHoldingRepository = privateAssetHoldingRepository;
        this.publicAssetRepository = publicAssetRepository;
    }  
    
    public UUID getUserUuid(){
        var auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return auth.uuid;
    }

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ModelPrivateCategory> PostPrivateCategory(@RequestBody PrivateCategoryRequest privateCategoryRequest) {
        if (privateCategoryRequest.categoryName == null || privateCategoryRequest.categoryName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryName is null or blank");
        }

        var privateCategory = privateCategoryRepository.save(privateCategoryRequest.toPrivateCategory(getUserUuid(), userRepository));

        return ResponseEntity.status(HttpStatus.CREATED).body(new ModelPrivateCategory(privateCategory));
    }

    @GetMapping("/category")
    public ModelPrivateCategory[] GetPrivateCategories() {

        var categories = privateCategoryRepository.findByUserUuid(getUserUuid());
        var arr = categories.stream().map(ModelPrivateCategory::new).toArray(ModelPrivateCategory[]::new);

        return arr;
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

    @PostMapping("/asset_holding/public")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelPublicAssetHolding PostPublicAssetHolding(@RequestBody PostPublicAssetHoldingRequest postPublicAssetHoldingRequest) {
        var uuid = UUID.fromString(postPublicAssetHoldingRequest.publicAssetUuid);
        if (publicAssetHoldingRepository.existsByPublicAssetUuid(uuid, getUserUuid())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Public asset holding already exists for user");
        }

        var publicAssetHolding = publicAssetHoldingRepository.save(
                postPublicAssetHoldingRequest.toPublicAssetHolding(getUserUuid(),
                        userRepository,
                        publicAssetHoldingRepository,
                        publicAssetRepository,
                        privateAssetHoldingRepository)
        );

        return new ModelPublicAssetHolding(publicAssetHolding);
    }

    @GetMapping("/asset_holding/public")
    public Set<ModelPublicAssetHolding> GetPublicAssetHoldings() {
        return publicAssetHoldingRepository.GetAssetsOfUser(getUserUuid()).stream().map(ModelPublicAssetHolding::new).collect(Collectors.toSet());
    }

    @PostMapping("/asset_holding/private")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelPrivateAssetHolding PostPrivateAssetHolding(@RequestBody PostPrivateAssetHoldingRequest postPrivateAssetHoldingRequest) {
        var privateAssetHolding = privateAssetHoldingRepository.save(postPrivateAssetHoldingRequest.toPrivateAssetHolding(getUserUuid(), userRepository, privateAssetHoldingRepository, publicAssetHoldingRepository));
        return new ModelPrivateAssetHolding(privateAssetHolding);
    }

    @GetMapping("/asset_holding/private")
    public Set<ModelPrivateAssetHolding> GetPrivateAssetsHoldings() {
        return privateAssetHoldingRepository.GetAssetsOfUser(getUserUuid()).stream().map(ModelPrivateAssetHolding::new).collect(Collectors.toSet());
    }

    @PostMapping("/asset_holding/grouping")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAssetHoldingGrouping PostAssetHoldingGrouping(@RequestBody PostAssetHoldingGroupingRequest postAssetHoldingGroupingRequest) {
        var assetGrouping = assetHoldingGroupingRepository.save(postAssetHoldingGroupingRequest.toAssetHoldingGrouping(getUserUuid(), userRepository, publicAssetHoldingRepository, privateAssetHoldingRepository));
        return new ModelAssetHoldingGrouping(assetGrouping);
    }

    @GetMapping("/asset_holding/grouping")
    public Set<ModelAssetHoldingGrouping> GetAssetHoldingGroupings() {
        return assetHoldingGroupingRepository.GetByUserUuid(getUserUuid()).stream().map(ModelAssetHoldingGrouping::new).collect(Collectors.toSet());
    }

    @DeleteMapping("/asset_holding/grouping/{groupingUuid}")
    public HttpStatus DeleteAssetHoldingGrouping(@PathVariable String groupingUuid) {
        var assetGroupingUuid = UUID.fromString(groupingUuid);
        assetHoldingGroupingRepository.deleteById(assetGroupingUuid);
        return HttpStatus.OK;
    }
}
