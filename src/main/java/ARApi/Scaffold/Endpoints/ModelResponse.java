package ARApi.Scaffold.Endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ModelResponse<T> extends ResponseEntity<Object> {

    public final HttpStatus httpStatus;
    public final Object presentResponse;

    public ModelResponse(T presentResponse, HttpStatus httpStatus) {
        super(presentResponse, httpStatus);
        this.httpStatus = httpStatus;
        this.presentResponse = presentResponse;
    }

    public ModelResponse(String errorMesssageResponse, HttpStatus httpStatus) {
        super(errorMesssageResponse, httpStatus);
        this.httpStatus = httpStatus;
        presentResponse = null;
    }
}
