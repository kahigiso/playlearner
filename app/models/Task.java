package models;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Jean Kahigiso on 4/4/14.
 */
@Entity
public class Task extends Model {
    @Id
    public Long id;
    public String title;
    public boolean done = false;
    public Date dueDate;
    @ManyToOne
    public User assignedTo;
    public String folder;
    @ManyToMany
    public Project project;

    public static Model.Finder<Long, Task> find = new Model.Finder(Long.class,Task.class);

    public static List<Task> findTodoInvolving(String user){
        return find.fetch("project")
                .where()
                .eq("done", false)
                .eq("project.members.email", user)
                .findList();
    }

    public static Task create(Task task, Long project, String folder){
        task.project= Project.find.ref(project);
        task.folder = folder;
        task.save();
        return task;
    }
}
