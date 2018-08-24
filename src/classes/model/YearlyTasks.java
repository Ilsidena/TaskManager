package classes.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author anna
 */

public class YearlyTasks implements TaskList {
    private LinkedList <Task> [] list;
    private final int capacity = 366;
    private int size = 0;
    
    public YearlyTasks () {
        this.list = new LinkedList [capacity];
        
        for (int i = 0; i < capacity; ++i) {
            list[i] = new LinkedList <> ();
        }
    }

    @Override
    public int size () {
        return this.size;
    }

    @Override
    public LinkedList<Task> getDateTasks (GregorianCalendar date) {
        LinkedList <Task> result = new LinkedList <> ();
        LinkedList <Task> resource = new LinkedList <> ();
        
        if (date.isLeapYear (date.get (Calendar.YEAR))
            || date.get (Calendar.DAY_OF_YEAR) < 60)
            resource = this.list[date.get (Calendar.DAY_OF_YEAR) - 1];
        else 
            resource = this.list[date.get (Calendar.DAY_OF_YEAR)];
        
        for (Task itr : resource) {
            if (itr.getDate ().compareTo (date) <= 0)
                result.add (itr.copy ());
        }
        
        return result;
    }

    @Override
    public Task find (String name) {
        for (int i = 0; i < this.capacity; ++i) {
            for (Task itr : list[i]) {
                if (itr.getName ().equals (name))
                    return itr;
            }        
        }
        
        return null;
    }
    
    @Override
    public Task find (String name
                     ,GregorianCalendar date) {
        for (int i = 0; i < this.capacity; ++i) {
            for (Task itr : list[i]) {
                if (itr.getName ().equals (name) && itr.getDate ().equals (date))
                    return itr;
            }        
        }
        
        return null;
    }
    
    @Override
    public boolean contains (Task task) {
        for (int i = 0; i < this.capacity; ++i) {
            if (list[i].contains (task)) 
                return true;
        } 
        return false;
    }

    @Override
    public boolean delete (String name) {
        for (int i = 0; i < this.capacity; ++i) {
            if (list[i].remove (find (name))) {
                --size;
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean delete (Task task) {
        for (int i = 0; i < this.capacity; ++i) {
            if (list[i].remove (task)) {
                --size;
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void add (Task task) {
        if (task.getDate().isLeapYear (task.getDate().get(Calendar.YEAR)))
            this.list[task.getDate ().get(Calendar.DAY_OF_YEAR) - 1].add (task);
        else {
            if (task.getDate ().get (Calendar.DAY_OF_YEAR) < 60) 
                this.list[task.getDate ().get(Calendar.DAY_OF_YEAR) - 1].add (task);
            else
                this.list[task.getDate ().get(Calendar.DAY_OF_YEAR)].add (task);
        }
        
        ++size;
    }
    
    @Override
    public LinkedList <Task> getTasks () {
        LinkedList <Task> result = new LinkedList <> ();        
        for (int i = 0; i < this.capacity; ++i)
            result.addAll (list[i]);
        
        return result;
    }
}