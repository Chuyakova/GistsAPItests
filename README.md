# _**Gists REST API Tests**_

GitHub Gists is a platform that allows sharing single files, parts of files, and full applications with other people.

### Tests cover

The CRUD operations and edge cases for authorized/unauthorized user based on Github Gists using the github API.

#### Cases for authorized user include getting of an access token for authorization and contain
- Create a public/secret gist
- Read a public/secret gist
- Star a gist
- Check that gist is starred
- Unstar a gist
- Check that gist is unstarred
- Update a public/secret gist
- Create a new gist file
- Delete a gist file
- Delete a public/secret gist 
- Fork gist
- Check list of gist forks
- Check that the user has a rate limit of 5000 calls
- Check that the user can increase rate limit to 5000 calls

#### Cases for authorized user contain
- Create a public gist
- Read a public gist
- Read a list of gists
- Star a gist
- Update a public gist
- Delete a public gist 
- Check that the user has a rate limit of 60 calls

### Configuration

API URIs, user credentials and paths for JSON files are located in `GistTestConfigurationConstants` class.

### Technologies
- Java

- REST-Assured

- Maven

- JUnit

- Hamcrest

### Run tests

To run test you need to have a Java JDK 1.8 installed on your machine. 

- Execute in the project root    
```./mvnw clean test```

### Documentation and links 
GitHub Gists https://gist.github.com/

GitHub Gist API documentation: https://developer.github.com/v3/gists/

REST Assured documentation:  https://github.com/rest-assured/rest-assured 
