package config;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * Contains additional constant data necessary for tests configuration
 */
public class GistTestConfiguration {

    /**
     * User's access token from GitHub API to test gists accessibility in authorized context
     */
    private String accessToken;

    /**
     * ID of generated public gist
     */
    private String publicGistId;

    /**
     * ID of generated secret gist
     */
    private String secretGistId;

    /**
     * ID of forked gist
     */
    private String forkedGistId;

    public GistTestConfiguration() {
        accessToken = generateAccessToken();
        publicGistId = createPublicGist();
        secretGistId = createSecretTestGist();
        forkedGistId = forkGist();
    }

    /**
     * Get an access token from GitHub API by sending user's encoded credentials
     *
     * @return user's access token
     */
    private String generateAccessToken() {
        File gettingTokenJson = new File("src/test/resources/GetAccessToken.json");

        Response response = given().
                header(new Header("Authorization",
                        "Basic " + GistTestConfigurationConstants.AUTH_ENCODED_CREDENTIALS)).
                contentType(ContentType.JSON)
                .body(gettingTokenJson).
                        when().
                        post(GistTestConfigurationConstants.AUTH_GITHUB_API).
                        then().
                        extract().response();

        return response.getBody().jsonPath().getString("token");
    }

    /**
     * Create a public gist
     *
     * @return ID of created public gist
     */
    private String createPublicGist() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_CREATE_PUBLIC_GIST);

        Response response = given().
                auth().
                oauth2(accessToken).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                post().
                then().extract().response();

        return response.getBody().jsonPath().getString("id");
    }

    /**
     * Create a secret gist
     *
     * @return ID of created secret gist
     */
    private String createSecretTestGist() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_CREATE_SECRET_GIST);

        Response response = given().
                auth().
                oauth2(accessToken).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                post().
                then().extract().response();

        return response.getBody().jsonPath().getString("id");
    }

    private String forkGist() {
        Response response = given().
                auth().
                oauth2(accessToken).
                when().
                post(GistTestConfigurationConstants.ID_OF_GIST_FOR_FORK + "/forks").
                then().extract().response();

        return response.getBody().jsonPath().getString("id");
    }

    public String getSecretGistId() {
        return secretGistId;
    }

    public String getPublicGistId() {
        return publicGistId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getForkedGistId() {
        return forkedGistId;
    }
}
