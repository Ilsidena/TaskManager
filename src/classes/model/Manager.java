package classes.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author anna
 */

public class Manager {
    private TaskList [] tasks;    
    private LinkedList <Note> notes;
    
    public static final int capacity = 6;
    
    public enum REPEATS {NONE
                        ,DAILY
                        ,WEEKLY
                        ,MONTHLY
                        ,YEARLY}
    
    public Manager () {
        tasks = new TaskList[capacity];
        
        tasks[0] = new OrdinaryTasks ();
        tasks[1] = new DailyTasks ();
        tasks[2] = new WeeklyTasks ();
        tasks[3] = new MonthlyTasks ();
        tasks[4] = new YearlyTasks ();
        tasks[5] = new DoneTasks ();
        
        notes = new LinkedList <> ();
    }
    
    public boolean addTask (String name
                            ,String description
                            ,LinkedList<Note> note
                            ,GregorianCalendar date
                            ,REPEATS repeats) 
    {
        Task newTask = null;
        
        for (int i = 0; i < capacity - 1; ++i)
                if ((newTask = tasks[i].find (name, date)) != null)
                    break;
        
        if (newTask == null) {
            newTask = new Task (name
                                ,description
                                ,note
                                ,date);
            switch (repeats) {
                case NONE:
                    this.tasks[0].add (newTask);
                    break;
                case DAILY:
                    this.tasks[1].add (newTask);
                    break;
                case WEEKLY:
                    this.tasks[2].add (newTask);
                    break;
                case MONTHLY:
                    this.tasks[3].add (newTask);
                    break;
                case YEARLY:
                    this.tasks[4].add (newTask);
                    break;
            }
            
            return true;
        }
        
        return false;
    }
    
    public boolean editTask (String editedName
                            ,GregorianCalendar editedDate
                            ,boolean isDone
                            ,String name
                            ,String description
                            ,LinkedList<Note> note
                            ,GregorianCalendar date
                            ,REPEATS repeats) 
    {
        Task task = null;
        
        if (isDone) {
            task = tasks[5].find (editedName, editedDate);
            
            if (task != null) {
                tasks[5].delete (task.getName());
                task = new Task (name, description, note, date);
                tasks[5].add (task);
                return true;
            }
        }
        else {
            for (int i = 4; i > -1; --i)
                if ((task = tasks[i].find (editedName)) != null)
                    break;
            
            if (task != null) {
                this.deleteTask (editedName, editedDate, isDone);
                return this.addTask (name, description, note, date, repeats);
            }
        }
        
        return false;
    }
    
    public boolean setComplete (String name
                                ,GregorianCalendar date) {
        Task task = tasks[0].find (name, date);
        
        if (task != null) {
            tasks[this.capacity -1].add (new Task (name
                                                  ,task.getDescription()
                                                  ,task.getNotes()
                                                  ,date));
            //tasks[0].delete(task);
            return true;
        }
        
        task = this.findTask (name);
        
        if (task != null) {
            tasks[this.capacity -1].add (new Task (name
                                                  ,task.getDescription()
                                                  ,task.getNotes()
                                                  ,date));
            return true;
        }
        
        return false;
    }
    
    public boolean setUncomplete (String name
                                ,GregorianCalendar date) {
        Task task;
        
        for (int i = 1; i < this.capacity -1; ++i) {
            if ((task = tasks[i].find (name)) != null) {
                if (!((i == 1 && task.getDate().compareTo(date) <= 0)
                        || ((task.getDate().get (Calendar.DAY_OF_WEEK) 
                        == date.get (Calendar.DAY_OF_WEEK))
                        || (task.getDate().get (Calendar.DAY_OF_MONTH) 
                        == date.get (Calendar.DAY_OF_MONTH))
                        || ((task.getDate().get (Calendar.DAY_OF_MONTH) 
                        == date.get (Calendar.DAY_OF_MONTH))
                        && (task.getDate().get (Calendar.MONTH) 
                        == date.get (Calendar.MONTH)))))){
//                    task = task.copy ();
//                    task.setDate (date);
//                    tasks[0].add (task);
                        return false;
                }
            }
        }
        
        task = tasks[this.capacity -1].find (name, date);
        return tasks[this.capacity -1].delete (task);
    }
    
     public boolean deleteTask (String name
                                ,GregorianCalendar date
                                ,boolean isDone) 
     {    
        if (isDone) {
            tasks[this.capacity -1].delete (tasks[this.capacity -1].find(name, date));
            
            for (int i = 1; i < this.capacity -1; ++i)
                if (tasks[i].find (name) != null && tasks[0].find (name) != null)
                    tasks[0].delete (name);
            
            return true;
        }
        
        for (TaskList itr : tasks) {
            if (itr.delete (name))
                return true;
        }
        
        return false;
    }
    
    public boolean deleteTask (String name
                              ,GregorianCalendar date) 
     {    
        if (tasks[this.capacity -1].delete (tasks[this.capacity -1].find(name, date)) == true){            
            for (int i = 1; i < this.capacity -1; ++i) {
                Task task = tasks[i].find (name);
                if (task != null && !task.getDate().equals(date)) {
                    tasks[0].delete (name);
                    return true;
                } 
            }
        }
        
        for (TaskList itr : tasks) {
            if (itr.delete (name))
                return true;
        }
        
        return false;
    }
     
    public LinkedList <Task> getDateTask (GregorianCalendar date) {
        LinkedList <Task> dateTasks = new LinkedList ();
        
        for (int i = 0; i < this.capacity - 1; ++i)
            dateTasks.addAll (tasks[i].getDateTasks (date));
        
        for (Task itr : dateTasks)
            itr.setDate(date);
        
        for (Task itr : dateTasks) {
            if (itr.equals(tasks[this.capacity - 1].find(itr.getName(), date)))
                itr.isDone = true;
        }
        
        for (Task itr : tasks[this.capacity - 1].getTasks()) {
            if (itr.getDate ().equals (date) && !dateTasks.contains (itr)) {
                Task task = itr.copy();
                task.isDone = true;
                dateTasks.add (task);
            }
        }
        
        return dateTasks;
    }
    
    public LinkedList <Task> getDoneTasks () {
        return tasks[5].getTasks ();
    }
    
    public Task findTask (String name) {
        Task task = null;
        
        for (TaskList itr : tasks) {
            if ((task = itr.find (name)) != null)
                return task;
        }
        
        return task;
    }
    
    public REPEATS getRepeats (Task task) {
        for (int i = this.capacity - 1; i > -1; --i) {
            if (tasks[i].contains (task)) {
                switch (i) {
                    case 0:
                        return REPEATS.NONE;
                    case 1:
                        return REPEATS.DAILY;
                    case 2:
                        return REPEATS.WEEKLY;
                    case 3:
                        return REPEATS.MONTHLY;
                    case 4:
                        return REPEATS.YEARLY;
                }
            }
        }
        
        return REPEATS.NONE;
    }
    
    public boolean addNote (String name
                            ,String description) {
        if (this.findNote (name) == null) {
            Note newNote = new Note (name, description);        
            this.notes.addFirst (newNote);
            return true;
        }
        else return false;
    }

    public boolean editNote (String edited
                            ,String name
                            ,String description) {
        Note cur = this.findNote (edited);
        
        if (cur != null) {
            cur.setName (name);
            cur.setDescription (description);
            return true;
        }
        else return false;
    }
    
    public boolean deleteNote (String name) {
        Note cur = this.findNote (name);
        
        if (cur != null) {
            notes.remove (cur);
            
            for (TaskList itr : tasks) {
                for (Task task : itr.getTasks()) {
                    LinkedList <Note> list = task.getNotes();
                    for (Note note : list){
                        if (note.getName() == name)
                            list.remove (note);
                    }
                }
            }
            
            return true;
        }
        else return false;
    }
    
    public Note findNote (String name) {
        for (Note cur : notes){
            if (cur.getName ().equals (name))
                return cur;
        }
        
	return null;
    }

    public Note getNote (int index){
        if (index >= 0 && index < notes.size ())
            return notes.get (index);
        
        return null;
    }

    public LinkedList <Note> getNotes () {
        return notes;
    }
    
    @Override
    public String toString () {
        StringBuilder builder = new StringBuilder ();        
        builder.append (this.notesToString ());
        
        for (TaskList itr : tasks)
            builder.append (this.listToString (itr));
        
        return builder.toString ();
    }
    
    private String notesToString () {
        StringBuilder builder = new StringBuilder ();
        builder.append (this.notes.size () + "\n");
        
        for (Note itr : this.notes)
            builder.append (itr.toString () + "\n");
        
        String res = builder.toString ();
        
        return res;
    }
    
    private String listToString (TaskList list) {
        StringBuilder builder = new StringBuilder ();
        builder.append (list.size () + "\n");
        LinkedList <Task> result = list.getTasks ();
        
        for (Task itr : result) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String date = formatter.format(itr.getDate ().getTime());
            
            builder.append (itr.getName () 
                            + "\n" + (itr.getDescription () == null ? "" : itr.getDescription ())
                            + "\n" + date
                            + "\n{");
            
            if (itr.getNotes () != null) {
                for (Note note : itr.getNotes ())
                    builder.append (itr.getNotes ().indexOf (note) + " ");
            }
            
            builder.append ("}\n");
        }
        
        return builder.toString ();
    }
}