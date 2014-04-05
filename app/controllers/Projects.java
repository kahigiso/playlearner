package controllers;

import models.*;
import play.mvc.*;
import static play.data.Form.form;
import views.html.projects.*;
import java.util.*;

/**
 * Created by Jean Kahigiso on 4/5/14.
 */
@Security.Authenticated(Secured.class)
public class Projects extends Controller {

    public static Result add(){
        Project newProject = Project.create(
            "New Project",
                form().bindFromRequest().get("group"),
                request().username()
        );
        return ok(item.render(newProject));
    }

    public static Result rename(Long project) {
        if (Secured.isMemberOf(project)) {
            return ok(
                    Project.rename(
                            project,
                            form().bindFromRequest().get("name")
                    )
            );
        } else {
            return forbidden();
        }
    }

    public static Result delete(Long project) {
        if(Secured.isMemberOf(project)) {
            Project.find.ref(project).delete();
            return ok();
        } else {
            return forbidden();
        }
    }

    public static Result addGroup() {
        return ok(
                group.render("New group", new ArrayList<Project>())
        );
    }

}
