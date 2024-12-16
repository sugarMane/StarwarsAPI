package StarWarsLib;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SWAPIService {
    private static final String BASE_URL = "https://sw-api.starnavi.io/";

    public Response getResource(String resourceType, String id) {
        String url = String.format("%s%s/%s/", BASE_URL, resourceType, id);
        return RestAssured.get(url);
    }

    public Response searchResource(String resourceType, String query) {
        String url = String.format("%s%s/?search=%s", BASE_URL, resourceType, query);
        return RestAssured.get(url);
    }
}
