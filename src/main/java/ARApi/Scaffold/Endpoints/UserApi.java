package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.Endpoints.Model.*;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedAssetGroupingRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPrivateAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPublicAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import io.swagger.models.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Component
@RequestMapping("user_api")
public class UserApi {

    private final UUID tempUserUuid = UUID.fromString("8ac5e9eb-a712-4b37-9cf1-4f1ff3c0a86c");

    @PostMapping("/category")
    public ModelResponse<ModelPrivateCategory> PostPrivateCategory(@RequestBody PrivateCategoryRequest privateCategoryRequest){
        return new ModelResponse<>(HttpStatus.OK);
    }

    @GetMapping("/category")
    public ModelResponse<ModelPrivateCategory[]> GetPrivateCategories(){
        return new ModelResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/category/{uuid}")
    public ResponseEntity<HttpStatus> DeletePrivateCategory(@PathVariable String uuid){
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
