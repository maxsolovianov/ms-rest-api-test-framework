package ms.apitests.tests;

import ms.apitests.runner.Order;
import ms.apitests.runner.OrderedRunner;
import ms.apitests.support.Setup;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static ms.apitests.support.Constants.*;

@RunWith(OrderedRunner.class)
public class RestApiTests extends Setup {

    @Test
    @Order(order = 1)
    public void find() {

        String linkToPeople =
                given().get(URI).then().
                        assertThat().statusCode(200).
                        assertThat().statusLine("HTTP/1.1 200 OK").
                        assertThat().contentType("application/json").
                        assertThat().body("people", equalTo(PEOPLE_ENDPOINT)).
                        extract().path("people");

        String linkToParticularPerson =
                given().param("search", PERSON_NAME).get(linkToPeople).then().
                        assertThat().statusCode(200).
                        assertThat().body("results", hasSize(greaterThan(0))).
                        assertThat().body("count", equalTo(1)).
                        assertThat().body("results.name", hasItem(PERSON_NAME)).
                        extract().jsonPath().getString("results[0].url");

        String linkToPlanet =
                given().get(linkToParticularPerson).then().
                        assertThat().statusCode(200).
                        assertThat().body("name", equalTo(PERSON_NAME)).
                        assertThat().body("homeworld", equalTo(LINK_TO_PLANET)).
                        extract().jsonPath().getString("homeworld");

        String linkToFirstFilm =
                given().get(linkToPlanet).then().
                        assertThat().statusCode(200).
                        assertThat().body("name", equalTo(PLANET_NAME)).
                        assertThat().body("population", equalTo(PLANET_POPULATION)).
                        extract().jsonPath().getString("films[0]");

        List listOfCharacters =
                given().get(linkToFirstFilm).then().
                        assertThat().statusCode(200).
                        assertThat().body("title", equalTo(FILM_NAME)).
                        assertThat().body("planets", hasItem(linkToPlanet)).
                        extract().jsonPath().getList("characters");
                        assertFalse(listOfCharacters.contains(LINK_TO_PERSON));
    }
}


