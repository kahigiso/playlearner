package controllers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import models.Project;
import org.junit.*;
import play.libs.Yaml;
import play.mvc.*;
import play.test.*;
import static play.test.Helpers.*;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Created by Jean Kahigiso on 4/5/14.
 */
public class ProjectsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void newProject() {
        Result result = callAction(
                controllers.routes.ref.Projects.add(),
                fakeRequest().withSession("email", "bob@example.com")
                        .withFormUrlEncodedBody(ImmutableMap.of("group", "Some Group"))
        );
        assertEquals(200, status(result));
        Project project = Project.find.where()
                .eq("folder", "Some Group").findUnique();
        assertNotNull(project);
        assertEquals("New Project", project.name);
        assertEquals(1, project.members.size());
        assertEquals("bob@example.com", project.members.get(0).email);
    }

    @Test
    public void renameProjectForbidden() {
        long id = Project.find.where()
                .eq("members.email", "bob@example.com")
                .eq("name", "Private").findUnique().id;
        Result result = callAction(
                controllers.routes.ref.Projects.rename(id),
                fakeRequest().withSession("email", "jeff@example.com")
                        .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
        );
        assertEquals(403, status(result));
        assertEquals("Private", Project.find.byId(id).name);
    }

}
