package config;

public class GistTestConfigurationConstants {

    /**
     * API GitHub Gists API URI
     */
    public static final String GIST_GITHUB_API = "https://api.github.com/gists";

    /**
     * Client secret of OAuth app
     */
    public static final String CLIENT_SECRET = "c5cda6cddd3267332fb23e22bf3b98edd4b14d0a";

    /**
     * Client ID of OAuth app
     */
    public static final String CLIENT_ID = "7b2d6dc8969cd20669ff";

    /**
     * Id of gist to fork
     */
    public static final String ID_OF_GIST_FOR_FORK = "5979afb981e4e964abb517480692c929";

    /**
     * API GitHub API URI for authorization
     */
    static final String AUTH_GITHUB_API = "https://api.github.com/authorizations";

    /**
     * Username and password encoded with base64 for login in GitHub
     */
    static final String AUTH_ENCODED_CREDENTIALS = "TmF0YWxpZUNodXlha292YTpEb3lvdXJiZXN0MjAxOQ==";

    /**
     * Relative path to the directory with JSON-files which will be used as body in HTTP requests
     */
    private static final String SRC_TEST_RESOURCES = "src/test/resources/";

    /**
     * Relative path to the JSON-file for deleting of gist file
     */
    public static final String FILEPATH_DELETE_GIST_FILE =
        SRC_TEST_RESOURCES + "DeleteGistFile.json";

    /**
     * Relative path to the JSON-file for creating of new gist file
     */
    public static final String FILEPATH_CREATE_NEW_GIST_FILE =
        SRC_TEST_RESOURCES + "CreateNewGistFile.json";

    /**
     * Relative path to the JSON-file for updating of secret gist
     */
    public static final String FILEPATH_UPDATE_SECRET_GIST =
        SRC_TEST_RESOURCES + "UpdateSecretGist.json";

    /**
     * Relative path to the JSON-file for updating of public gist
     */
    public static final String FILEPATH_UPDATE_PUBLIC_GIST =
        SRC_TEST_RESOURCES + "UpdatePublicGist.json";

    /**
     * Relative path to the JSON-file for creating of secret gist
     */
    public static final String FILEPATH_CREATE_SECRET_GIST =
        SRC_TEST_RESOURCES + "CreateSecretGist.json";

    /**
     * Relative path to the JSON-file for creation of public gist
     */
    public static final String FILEPATH_CREATE_PUBLIC_GIST =
        SRC_TEST_RESOURCES + "CreatePublicGist.json";
}
