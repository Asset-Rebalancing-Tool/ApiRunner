package ARApi.Scaffold.Endpoints.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ModelResponse<T extends BaseModel> extends ResponseEntity<T> {

    public final HttpStatus httpStatus;
    public final BaseModel presentResponse;

    public ModelResponse(T presentResponse, HttpStatus httpStatus) {
        super(presentResponse, httpStatus);
        this.httpStatus = httpStatus;
        this.presentResponse = presentResponse;
    }

    public ModelResponse(HttpStatus status){
        this((T) null, status);
    }

    public ModelResponse(String errorMesssageResponse, HttpStatus httpStatus) {
        super((T) new BaseModel(errorMesssageResponse), httpStatus);
        this.httpStatus = httpStatus;
        presentResponse = new BaseModel(errorMesssageResponse);
    }
}
