package controllers;

import play.mvc.*;
import models.*;
import views.html.*;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Project.find.all(),Task.find.all()));
    }

}
