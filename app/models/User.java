package models;

import play.db.ebean.Model;
import javax.persistence.*;

/**
 * Created by Jean Kahigiso on 4/4/14.
 */
@Entity
public class User extends Model {

    @Id
    public String email;
    public String name;
    public String password;

    public User(String name, String email, String password){
       this.name= name;
       this.email = email;
       this.password = password;
    }

    public static Finder<String,User> find = new Finder<String, User>(String.class, User.class);

    public static User authenticate(String email, String password){
        return find.where().eq("email",email).eq("password",password).findUnique();
    }

}
