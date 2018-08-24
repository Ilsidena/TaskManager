package classes.model;

import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author anna
 */

public class DoneTasks implements TaskList{
    private LinkedList <Task> list;

    public DoneTasks () {
        list = new LinkedList <> ();
    }

    @Override
    public void add (Task task) {
        list.add (task);
    }

    @Override
    public boolean delete (Task task) {
        return list.remove (task);
    }
    
    @Override
    public boolean delete (String name) {
        return list.remove (this.find (name));
    }
    
    @Override
    public int size () {
        return list.size();
    }
    
    @Override
    public Task find (String name) {
        for (Task itr : list) {
            if (itr.getName ().equals (name))
                return itr;
        }        
        
        return null;
    }
    
    @Override
    public Task find (String name
                     ,GregorianCalendar date) {
        for (Task itr : list) {
            if (itr.getName ().equals (name) && itr.getDate ().equals (date))
                return itr;
        }        
        
        return null;
    }
    
    @Override
    public boolean contains (Task task) {
        return list.contains (task);
    }
    
    @Override
    public LinkedList <Task> getDateTasks (GregorianCalendar date) {
        LinkedList <Task> result = new LinkedList <> ();
        
        for (Task itr : list) {
            if (itr.getDate () == date)
                result.add (itr.copy ());
        }        
        
        return result;
    }
    
    @Override
    public LinkedList <Task> getTasks () {
        return list;
    }
}
