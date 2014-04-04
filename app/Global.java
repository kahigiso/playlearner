import com.avaje.ebean.Ebean;
import models.*;
import play.Application;
import play.GlobalSettings;
import play.libs.*;
import java.util.*;

/**
 * Created by Jean Kahigiso on 4/4/14.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app){
        if(User.find.findRowCount() ==0){
            Ebean.save((List) Yaml.load("initial-data.yml"));
        }
    }
}
