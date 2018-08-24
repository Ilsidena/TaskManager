package classes.model;

import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author anna
 */

public interface TaskList {
    public void add (Task task); 
    
    public boolean delete (Task task);
    public boolean delete (String name);
    
    public Task find (String name);
    public Task find (String name, GregorianCalendar date); 
    
    public boolean contains (Task task);
    public LinkedList <Task> getDateTasks (GregorianCalendar date);    
    public int size ();
    public LinkedList <Task> getTasks ();
}

