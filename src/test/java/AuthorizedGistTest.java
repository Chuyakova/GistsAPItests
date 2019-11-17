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
 * Test gist workflow in authorized context
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class AuthorizedGistTest {

    private GistTestConfiguration gistTestConfiguration;

    @BeforeAll
    private void init() {
        RestAssured.baseURI = GistTestConfigurationConstants.GIST_GITHUB_API;

        gistTestConfiguration = new GistTestConfiguration();
    }

    /**
     * Checks that authorized user can create public gist
     */
    @DisplayName("authorized user creates public gist")
    @Test
    @Order(1)
    void createPublicGist() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_CREATE_PUBLIC_GIST);

        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                post().
                then().
                assertThat().
                statusCode(201).
                and().
                body("files.'newPublicGistFile.txt'.filename", equalTo("newPublicGistFile.txt")).
                body("files.'newPublicGistFile.txt'.content", equalTo("The content is here")).
                body("public", equalTo(true));
    }

    /**
     * Checks that authorized user can create secret gist
     */
    @Test
    @Order(2)
    @DisplayName("authorized user creates secret gist")
    void createSecretGist() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_CREATE_SECRET_GIST);
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                post().
                then().
                assertThat().
                statusCode(201).
                and().
                body("files.'newSecretGistFile.txt'.filename", equalTo("newSecretGistFile.txt")).
                body("files.'newSecretGistFile.txt'.content", equalTo("The content is here")).
                body("public", equalTo(false));
    }

    /**
     * Checks that authorized user can read his public gist
     */
    @Test
    @Order(3)
    @DisplayName("authorized user reads his public gist")
    void readPublicGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
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
     * Checks that authorized user can read his secret gist
     */
    @Test
    @Order(4)
    @DisplayName("authorized user reads his secret gist")
    void readSecretGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get(gistTestConfiguration.getSecretGistId()).
                then().
                assertThat().
                statusCode(200).
                and().
                body("files.'newSecretGistFile.txt'.filename", equalTo("newSecretGistFile.txt")).
                body("files.'newSecretGistFile.txt'.language", equalTo("Text")).
                body("files.'newSecretGistFile.txt'.content", equalTo("The content is here")).
                body("id", equalTo(gistTestConfiguration.getSecretGistId()));
    }

    /**
     * Checks that authorized user gets access to the list of only his gists by simply going to the path /gists
     * Check that list includes user's secret gist
     */
    @Test
    @Order(5)
    @DisplayName("authorized user reads list of his gists")
    void readListOfGists() {

        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get().
                then().assertThat().statusCode(200).
                // check that we receive our secret gist
                body("files.'newSecretGistFile.txt'.filename[0]", equalTo("newSecretGistFile.txt")).
                body("files.'newSecretGistFile.txt'.language[0]", equalTo("Text")).
                body("public[0]", equalTo(false));
    }

    /**
     * Checks that authorized user can make his public gist starred
     */
    @Test
    @Order(6)
    @DisplayName("authorized user stars his public gist")
    void starPublicGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                put(gistTestConfiguration.getPublicGistId() + "/star").
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can make his secret gist starred
     */
    @Test
    @Order(7)
    @DisplayName("authorized user stars his secret gist")
    void starSecretGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                put(gistTestConfiguration.getSecretGistId() + "/star").
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can check that his public gist is starred
     */
    @Test
    @Order(8)
    @DisplayName("authorized user checks that his public gist is starred")
    void checkStarredPublicGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get(gistTestConfiguration.getPublicGistId() + "/star").
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can check that his secret gist is starred
     */
    @Test
    @Order(9)
    @DisplayName("authorized user checks that his secret gist is starred")
    void checkStarredSecretGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get(gistTestConfiguration.getSecretGistId() + "/star").
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can make his starred public gist unstarred
     */
    @Test
    @Order(10)
    @DisplayName("authorized user unstars his public gist")
    void unstarPublicGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                delete(gistTestConfiguration.getPublicGistId() + "/star").
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can make his starred secret gist unstarred
     */
    @Test
    @Order(11)
    @DisplayName("authorized user unstars his secret gist")
    void unstarSecretGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                delete(gistTestConfiguration.getSecretGistId() + "/star").
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can check that his public gist unstarred
     */
    @Test
    @Order(12)
    @DisplayName("authorized user checks that his public gist is unstarred")
    void checkUnstarredPublicGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get(gistTestConfiguration.getPublicGistId() + "/star").
                then().
                assertThat().statusCode(404);
    }

    /**
     * Checks that authorized user can check that his secret gist unstarred
     */
    @Test
    @Order(13)
    @DisplayName("authorized user checks that his secret gist is unstarred")
    void checkUnstarredSecretGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get(gistTestConfiguration.getSecretGistId() + "/star").
                then().
                assertThat().statusCode(404);
    }


    /**
     * Checks that authorized user can update his public gist by sending actual data: gist
     * description and name of file to change
     */
    @Test
    @Order(14)
    @DisplayName("authorized user updates his public gist")
    void updatePublicGistFirstTest() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_UPDATE_PUBLIC_GIST);

        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                patch(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().statusCode(200).
                body("url",
                        equalTo(GistTestConfigurationConstants.GIST_GITHUB_API + "/" + gistTestConfiguration
                                .getPublicGistId())).
                body("files.'updatedPublicGistFile.json'.filename",
                        equalTo("updatedPublicGistFile.json")).
                body("files.'updatedPublicGistFile.json'.content",
                        equalTo("The updated content is here"));
    }

    /**
     * Checks that authorized user can update his secret gist by sending actual data: gist
     * description and name of file to change
     */
    @Test
    @Order(15)
    @DisplayName("authorized user updates his secret gist")
    void updatesSecretGistFirstTest() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_UPDATE_SECRET_GIST);

        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                patch(gistTestConfiguration.getSecretGistId()).
                then().
                assertThat().statusCode(200).
                body("url",
                        equalTo(GistTestConfigurationConstants.GIST_GITHUB_API + "/" + gistTestConfiguration
                                .getSecretGistId())).
                body("files.'updatedSecretGistFile.json'.filename",
                        equalTo("updatedSecretGistFile.json")).
                body("files.'updatedSecretGistFile.json'.content",
                        equalTo("The updated content is here"));
    }

    /**
     * Checks that authorized user can update description and create new file on his public gist
     * sending nonexistent description and name of file to change
     */
    @Test
    @Order(16)
    @DisplayName("authorized user creates new " +
            "gist file with new description")
    void updatesGistSecondTest() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_CREATE_NEW_GIST_FILE);

        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                patch(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().statusCode(200).
                body("url",
                        equalTo(GistTestConfigurationConstants.GIST_GITHUB_API + "/" + gistTestConfiguration
                                .getPublicGistId())).
                body("files.'newFileName.json'.filename", equalTo("newFileName.json")).
                body("files.'newFileName.json'.content", equalTo("The content is here"));
    }

    /**
     * Checks that user can delete file from gist by sending null as name of file to change
     */
    @Test
    @Order(17)
    @DisplayName("authorized user deletes gist file")
    void deleteFileFromGist() {
        File JSON = new File(GistTestConfigurationConstants.FILEPATH_DELETE_GIST_FILE);

        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                contentType(ContentType.JSON).
                body(JSON).
                when().
                patch(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().statusCode(200);
    }

    /**
     * Checks that authorized user can delete his public gist
     */
    @Test
    @Order(18)
    @DisplayName("authorized user deletes his public gist")
    void deletePublicGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                delete(gistTestConfiguration.getPublicGistId()).
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can delete his public gist
     */
    @Test
    @Order(19)
    @DisplayName("authorized user deletes his secret gist")
    void deleteSecretGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                delete(gistTestConfiguration.getSecretGistId()).
                then().
                assertThat().statusCode(204);
    }

    /**
     * Checks that authorized user can fork other gist
     */
    @Test
    @Order(20)
    @DisplayName("authorized user forks gist")
    void forkGist() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                post(GistTestConfigurationConstants.ID_OF_GIST_FOR_FORK + "/forks").
                then().
                assertThat().statusCode(201).
                body("files.'starwars-graph-data.csv'.filename", equalTo("starwars-graph-data.csv")).
                body("public", equalTo(true));
    }

    /**
     * Checks that:
     * 1. Authorized user has an access to the list of gist forks of other user
     * 2. List of user's gist forks includes information of forked gist
     */
    @Test
    @Order(21)
    @DisplayName("authorized user checks gist forks")
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
     * Checks that the user has a rate limit of 5000 calls
     */
    @Test
    @Order(22)
    @DisplayName("authorized user has rate limiting of 5000 calls")
    void checkRateLimiting() {
        given().
                auth().
                oauth2(gistTestConfiguration.getAccessToken()).
                when().
                get().
                then().
                assertThat().statusCode(200).
                assertThat().header("X-RateLimit-Limit", "5000");
    }

    /**
     * Checks that the user can increase rate limit to 5000 calls
     */
    @Test
    @Order(23)
    @DisplayName("authorized user increases unauthenticated rate limiting to 5000 calls")
    void checkIncreasingOfRateLimiting() {
        given().
                when().
                params("client_id", GistTestConfigurationConstants.CLIENT_ID, "client_secret",
                        GistTestConfigurationConstants.CLIENT_SECRET).
                get().
                then().
                assertThat().statusCode(200).
                assertThat().header("X-RateLimit-Limit", "5000");
    }
}

