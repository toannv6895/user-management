package org.toannguyen;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class UserManagementTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/C:/Program Files/Git/cognito-example")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

}