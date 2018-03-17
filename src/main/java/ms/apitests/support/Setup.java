package ms.apitests.support;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static ms.apitests.support.Constants.*;

public class Setup {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = URI;
    }
}
