package controllers;

import models.Project;
import play.mvc.*;
import play.mvc.Http.*;

/**
 * Created by Jean Kahigiso on 4/5/14.
 */
public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }

    public static boolean isMemberOf(Long project) {
        return Project.isMember(
                project,
                Context.current().request().username()
        );
    }


}
