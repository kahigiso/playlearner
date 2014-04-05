package controllers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import org.junit.*;
import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;
import static play.test.Helpers.*;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Created by Jean Kahigiso on 4/5/14.
 */
public class LoginTest extends WithApplication{
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void authenticateSuccess() {
        Result result = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                        "email", "bob@example.com",
                        "password", "secret"))
        );
        assertEquals(303, status(result));
        assertEquals("bob@example.com", session(result).get("email"));
    }
    @Test
    public void authenticateFailurePassword() {
        Result result = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                        "email", "bob@example.com",
                        "password", "badpassword"))
        );
        assertEquals(400, status(result));
        assertNull(session(result).get("email"));
    }

    @Test
    public void authenticateFailureUsername() {
        Result result = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                        "email", "bob@exame.com",
                        "password", "secret"))
        );
        assertEquals(400, status(result));
        assertNull(session(result).get("email"));
    }

    @Test
    public void authenticated() {
        Result result = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest().withSession("email", "bob@example.com")
        );
        assertEquals(200, status(result));
    }

    @Test
    public void notAuthenticated() {
        Result result = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest()
        );
        assertEquals(303, status(result));
        assertEquals("/login", header("Location", result));
    }

}
