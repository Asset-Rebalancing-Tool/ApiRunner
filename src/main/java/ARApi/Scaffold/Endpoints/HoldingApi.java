package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Database.Entities.PublicAsset.HoldingOrigin;
import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.*;
import ARApi.Scaffold.Endpoints.Model.ModelHoldingGroup;
import ARApi.Scaffold.Endpoints.Model.ModelPrivateHolding;
import ARApi.Scaffold.Endpoints.Model.ModelPrivateCategory;
import ARApi.Scaffold.Endpoints.Model.ModelPublicHolding;
import ARApi.Scaffold.Endpoints.Requests.HoldingGroupRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateAssetHoldingRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import ARApi.Scaffold.Endpoints.Requests.PublicAssetHoldingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

// TODO: rename everything to possession...
@RestController
@Component
@RequestMapping("holding_api")

public class HoldingApi {
    
    private final PrivateCategoryRepository privateCategoryRepository;

    private final UserRepository userRepository;

    private final AssetHoldingGroupRepository assetHoldingGroupRepository;

    private final PublicAssetHoldingRepository publicAssetHoldingRepository;

    private final PrivateAssetHoldingRepository privateAssetHoldingRepository;

    private final PublicAssetRepository publicAssetRepository;

    @Autowired
    public HoldingApi(PrivateCategoryRepository privateCategoryRepository, UserRepository userRepository, AssetHoldingGroupRepository assetHoldingGroupRepository, PublicAssetHoldingRepository publicAssetHoldingRepository, PrivateAssetHoldingRepository privateAssetHoldingRepository, PublicAssetRepository publicAssetRepository) {
        this.privateCategoryRepository = privateCategoryRepository;
        this.userRepository = userRepository;
        this.assetHoldingGroupRepository = assetHoldingGroupRepository;
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

        var privateCategory = privateCategoryRepository.saveAndFlush(privateCategoryRequest.toPrivateCategory(getUserUuid(), userRepository));

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
    public ResponseEntity<ModelPublicHolding> PostPublicAssetHolding(@RequestBody PublicAssetHoldingRequest publicAssetHoldingRequest) {
        var uuid = UUID.fromString(publicAssetHoldingRequest.publicAssetUuid);

        if (publicAssetHoldingRepository.holdingExists(uuid, getUserUuid(), HoldingOrigin.ManualEntry)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "HOLDING_EXISTS");
        }

        var publicAssetHolding = publicAssetHoldingRepository.save(
                publicAssetHoldingRequest.toPublicAssetHolding(getUserUuid(),
                        userRepository,
                        publicAssetHoldingRepository,
                        publicAssetRepository,
                        privateAssetHoldingRepository)
        );

        var holding = new ModelPublicHolding(publicAssetHolding);
        return ResponseEntity.status(HttpStatus.CREATED).body(holding);
    }

    @GetMapping("/asset_holding/public")
    public ModelPublicHolding[] GetPublicAssetHoldings(@RequestParam Optional<Boolean> groupLess) {

        var userHoldings = publicAssetHoldingRepository.GetAssetsOfUser(getUserUuid());
        if(groupLess.isPresent() && groupLess.get()){
            var groupsOfUser = assetHoldingGroupRepository.GetByUserUuid(getUserUuid());
            userHoldings = userHoldings.stream().filter(holding -> groupsOfUser.stream().noneMatch(group -> group.ContainsHolding(holding.uuid))).toList();
        }

        var holdings = userHoldings.stream().map(ModelPublicHolding::new).toArray(ModelPublicHolding[]::new);

        return holdings;
    }

    @DeleteMapping("/asset_holding/public/{holdingUuid}")
    public HttpStatus DeletePublicAssetHolding(@PathVariable String holdingUuid) {

        // check if holding in group, remove relation first
        var ph = publicAssetHoldingRepository.findById(UUID.fromString(holdingUuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No holding found for uuid"));;

        if(ph.HoldingGroup != null){
            ph.HoldingGroup.publicHoldings.remove(ph);
            publicAssetHoldingRepository.saveAndFlush(ph);
        }

        publicAssetHoldingRepository.deleteById(UUID.fromString(holdingUuid));
        return HttpStatus.OK;
    }

    @PostMapping("/asset_holding/private")
    public ResponseEntity<ModelPrivateHolding> PostPrivateAssetHolding(@RequestBody PrivateAssetHoldingRequest privateAssetHoldingRequest) {
        try{
            var privateAssetHolding = privateAssetHoldingRepository.save(privateAssetHoldingRequest
                    .toPrivateAssetHolding(getUserUuid(), userRepository));
            return ResponseEntity.status(HttpStatus.CREATED).body(new ModelPrivateHolding(privateAssetHolding));
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "TITLE_ALREADY_EXISTS");
        }
    }

    @PatchMapping("/asset_holding/public/{holdingUuid}")
    public ResponseEntity<ModelPublicHolding> PatchPublicAssetHolding(@RequestBody PublicAssetHoldingRequest publicAssetHoldingRequest, @PathVariable String holdingUuid){
        var uuid = UUID.fromString(holdingUuid);
        var assetHolding = publicAssetHoldingRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No holding found for uuid"));

        assetHolding = publicAssetHoldingRepository.saveAndFlush(publicAssetHoldingRequest.patchAssetHolding(assetHolding));

        return ResponseEntity.ok(new ModelPublicHolding(assetHolding));
    }

    @PatchMapping("/asset_holding/private/{holdingUuid}")
    public ResponseEntity<ModelPrivateHolding> PatchPrivateAssetHolding(@RequestBody PrivateAssetHoldingRequest holdingRequest, @PathVariable String holdingUuid){
        var uuid = UUID.fromString(holdingUuid);
        var assetHolding = privateAssetHoldingRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No holding found for uuid"));

        assetHolding = privateAssetHoldingRepository.saveAndFlush(holdingRequest.patchPrivateAssetHolding(assetHolding));

        return ResponseEntity.ok(new ModelPrivateHolding(assetHolding));
    }


    @GetMapping("/asset_holding/private")
    public ModelPrivateHolding[] GetPrivateAssetHoldings(@RequestParam Optional<Boolean> groupLess) {
        var userHoldings = privateAssetHoldingRepository.GetAssetsOfUser(getUserUuid());
        if(groupLess.isPresent() && groupLess.get()){
            var groupsOfUser = assetHoldingGroupRepository.GetByUserUuid(getUserUuid());
            userHoldings = userHoldings.stream().filter(holding -> groupsOfUser.stream().noneMatch(group -> group.ContainsHolding(holding.uuid))).toList();
        }

        return userHoldings.stream().map(ModelPrivateHolding::new).toArray(ModelPrivateHolding[]::new);
    }

    @DeleteMapping("/asset_holding/private/{holdingUuid}")
    public HttpStatus DeletePrivateAssetHolding(@PathVariable String holdingUuid){
        // check if holding in group, remove relation first
        var ph = privateAssetHoldingRepository.findById(UUID.fromString(holdingUuid))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No holding found for uuid"));;

        if(ph.HoldingGroup != null){
            ph.HoldingGroup.publicHoldings.remove(ph);
            privateAssetHoldingRepository.saveAndFlush(ph);
        }

        privateAssetHoldingRepository.deleteById(UUID.fromString(holdingUuid));
        return HttpStatus.OK;
    }

    @PatchMapping("/asset_holding/group/{groupUuid}")
    public ResponseEntity<ModelHoldingGroup> PatchHoldingGroup(@RequestBody HoldingGroupRequest holdingGroupRequest, @PathVariable String groupUuid){
        var uuid = UUID.fromString(groupUuid);

        var holdingGroup = assetHoldingGroupRepository.findById(uuid).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No group holding found for uuid"));

        var patchedGroup = assetHoldingGroupRepository.saveAndFlush(holdingGroupRequest.patchHoldingGroup(holdingGroup, publicAssetHoldingRepository, privateAssetHoldingRepository));

        return ResponseEntity.ok(new ModelHoldingGroup(patchedGroup));
    }

    @PostMapping("/asset_holding/group")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelHoldingGroup PostAssetHoldingGroup(@RequestBody HoldingGroupRequest postAssetHoldingGroupRequest) {
        var groupingDb = postAssetHoldingGroupRequest.toAssetHoldingGrouping(getUserUuid(), userRepository, publicAssetHoldingRepository, privateAssetHoldingRepository);
        // save holding groups aswell
        groupingDb.publicHoldings.forEach(publicHolding -> {
            publicHolding.HoldingGroup = groupingDb;
        });
        groupingDb.privateHoldings.forEach(privateHolding -> {
            privateHolding.HoldingGroup = groupingDb;
        });
        var assetGrouping = assetHoldingGroupRepository.saveAndFlush(groupingDb);

        return new ModelHoldingGroup(assetGrouping);
    }

    @GetMapping("/asset_holding/group")
    public ModelHoldingGroup[] GetAssetHoldingGroups() {
        var holdingGroups = assetHoldingGroupRepository.GetByUserUuid(getUserUuid());
        return holdingGroups.stream().map(ModelHoldingGroup::new).toArray(ModelHoldingGroup[]::new);
    }

    @DeleteMapping("/asset_holding/group/{groupUuid}")
    public HttpStatus DeleteAssetHoldingGroup(@PathVariable String groupUuid) {
        var assetGroupingUuid = UUID.fromString(groupUuid);
        assetHoldingGroupRepository.deleteById(assetGroupingUuid);
        return HttpStatus.OK;
    }
}
