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
    @ManyToOne
    public Project project;

    public static Finder<Long, Task> find = new Finder(Long.class,Task.class);

    public static List<Task> findTodoInvolving(String userEmail){
        return find
                .fetch("project")
                .where()
                .eq("project.members.email", userEmail)
                .eq("done", false)
                .findList();
    }

    public static Task create(Task task, Long project, String folder){
        task.project= Project.find.ref(project);
        task.folder = folder;
        task.save();
        return task;
    }
}
