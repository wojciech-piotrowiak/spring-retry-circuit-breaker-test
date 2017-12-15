import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
class ConnectionService {

    @CircuitBreaker(include = { RestClientException.class }, openTimeout = 10_000, resetTimeout = 20_000)
    String doUpload(String payload) {
        System.out.println("real service called");
        if(payload.contentEquals("FAIL")) {
            throw new RestClientException("");
        }
        return payload;
    }

    @Recover
    public String recover(String payload) {
        return "recovered";
    }
}