package models;

import com.avaje.ebean.Ebean;
import org.junit.*;
import static org.junit.Assert.*;

import play.libs.Yaml;
import play.test.WithApplication;

import java.util.List;

import static play.test.Helpers.*;

/**
 * Created by Jean Kahigiso on 4/4/14.
 */
public class ModelsTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase()));
        Ebean.save((List) Yaml.load("test-data.yml"));

    }

    @Test
    public void createAndRetrieveUser(){
        new User("Jean Kahigiso","me@gmail.com","kahigisopass").save();
        User jean  = User.find.where().eq("email","me@gmail.com").findUnique();
        assertNotNull(jean);
        assertEquals("Jean Kahigiso",jean.name);
    }

    @Test
    public void tryAuthenticateUser(){
        new User("Jean Kahigiso","me@gmail.com","kahigisopass").save();
        assertNotNull(User.authenticate("me@gmail.com","kahigisopass"));
        assertNull(User.authenticate("me@gmail.com", "kasopass"));
        assertNull(User.authenticate("m@gmail.com", "kahigisopass"));
    }

    @Test
    public void findProjectsInvolving(){
        new User("Me one","meone@gmail.com","pass123").save();
        new User("Me two","metwo@gmail.com","pass321").save();

        Project.create("Play 2","play","meone@gmail.com");
        Project.create("Play 1","play","metwo@gmail.com");

        List<Project> results = Project.findInvolving("meone@gmail.com");
        assertEquals(1,results.size());
        assertEquals("Play 2",results.get(0).name);

    }

    @Test
    public void findTodoTasksInvolving() {
        User bob = new User("Bob","bob@gmail.com", "secret");
        bob.save();
        Project project = Project.create("Play 2", "play", "bob@gmail.com");

        Task t1 = new Task();
        t1.title = "Write tutorial";
        t1.assignedTo = bob;
        t1.done = true;
        t1.save();

        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();

        List<Task> results = Task.findTodoInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Release next version", results.get(0).title);
    }

    @Test
    public void fullTest() {
        // Count things
        assertEquals(3, User.find.findRowCount());
        assertEquals(7, Project.find.findRowCount());
        assertEquals(5, Task.find.findRowCount());

        // Try to authenticate as users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));
        assertNull(User.authenticate("tom@example.com", "secret"));

        // Find all Bob's projects
        List<Project> bobsProjects = Project.findInvolving("bob@example.com");
        assertEquals(5, bobsProjects.size());

        // Find all Jane's projects
        List<Project> janesProjects = Project.findInvolving("jane@example.com");
        assertEquals(3, janesProjects.size());

        // Find all Jane's projects
        List<Project> jeffsProjects = Project.findInvolving("jeff@example.com");
        assertEquals(3, jeffsProjects.size());

        // Find all Bob's todo tasks
        List<Task> bobTasks = Task.findTodoInvolving("bob@example.com");
        assertEquals(4, bobTasks.size());
    }
}
