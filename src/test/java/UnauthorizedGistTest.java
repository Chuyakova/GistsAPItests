import config.GistTestConfiguration;
import config.GistTestConfigurationConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * This class contains tests of gists accessibility in authorized context
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class UnauthorizedGistTest {

    private GistTestConfiguration gistTestConfiguration;

    /**
     * Specifies that for all tests will be used one URI from variable GIST_GITHUB_API
     */
    @BeforeAll
    private void init() {
        RestAssured.baseURI = GistTestConfigurationConstants.GIST_GITHUB_API;
        gistTestConfiguration = new GistTestConfiguration();
    }

    /**
     * Checks that unauthorized user can't create new gist
     */
    @Test
    @Order(1)
    @DisplayName("unauthorized user creates public gist")
    void createGist() {
        File file = new File(GistTestConfigurationConstants.FILEPATH_CREATE_PUBLIC_GIST);

        given().
                contentType(ContentType.JSON).
                body(file).
                when().
                post().
                then().
                assertThat().statusCode(401);
    }

    /**
     * Checks that unauthorized user can read his public gist
     */
    @Test
    @Order(2)
    @DisplayName("unauthorized user reads public gist")
    void readGist() {
        given().
                when().
                get(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().
                statusCode(200).
                and().
                body("files.'newPublicGistFile.txt'.filename", equalTo("newPublicGistFile.txt")).
                body("files.'newPublicGistFile.txt'.language", equalTo("Text")).
                body("files.'newPublicGistFile.txt'.content", equalTo("The content is here"));
    }

    /**
     * Checks that unauthorized user can't read list of his starred gists
     */
    @Test
    @Order(3)
    @DisplayName("unauthorized user reads list of his starred gists")
    void readStarredGists() {
        given().
                when().
                get("/starred").
                then().
                assertThat().
                statusCode(401);
    }

    /**
     * Checks that unauthorized user can't edit his gist
     */
    @Test
    @Order(4)
    @DisplayName("unauthorized user updates public gist")
    void updateGist() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_UPDATE_SECRET_GIST);

        given().
                contentType(ContentType.JSON).
                body(JSON).
                when().
                patch(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().statusCode(404);
    }

    /**
     * Checks that unauthorized user can't delete his gist
     */
    @Test
    @Order(5)
    @DisplayName("unauthorized user deletes public gist")
    void givenUnauthorizedUser_whenDeletesGist_then404NotFound() {
        given().
                when().
                delete(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().statusCode(404);
    }

    /**
     * Checks that authorized user does't get access to the list of only his gists by going on the path /gists
     * Check that list doesn't include user's secret gist
     */
    @Test
    @Order(6)
    @DisplayName("unauthorized user reads list of his gists ")
    void readListOfGists() {
        given().
                when().
                get().
                then().
                assertThat().
                statusCode(200).
                and().
                // check that we don't receive our secret gist
                        body("files.'newSecretGistFile.txt'.filename[0]", equalTo(null));
    }

    /**
     * Checks that unauthorized user can make his public gist starred
     */
    @Test
    @Order(7)
    @DisplayName("unauthorized user stars his public gist")
    void starPublicGist() {
        given().
                when().
                put(gistTestConfiguration.getPublicGistId() + "/star").
                then().
                assertThat().statusCode(404);
    }

    /**
     * Checks that unauthorized user has an access to the list of gist forks of other user
     */
    @Test
    @Order(8)
    @DisplayName("unauthorized user checks gist forks")
    void checkGistForks() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get(GistTestConfigurationConstants.ID_OF_GIST_FOR_FORK + "/forks").
                then().
                assertThat().statusCode(200).
                body("id[0]", equalTo(gistTestConfiguration.getForkedGistId()));
    }

    /**
     * Checks that unauthorized user has a rate limit of 60 calls
     */
    @Test
    @Order(9)
    @DisplayName("unauthorized user has a rate limit of 60 calls")
    void checkRateLimiting() {
        given().
                when().
                get().
                then().
                assertThat().statusCode(200).
                assertThat().header("X-RateLimit-Limit", "60");
    }
}
