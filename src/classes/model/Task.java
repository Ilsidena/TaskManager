package classes.model;

import java.util.*;

/**
 * @author anna
 */

public class Task {
    private String name;
    private String description;    
    private LinkedList <Note> notes;    
    private GregorianCalendar date;
    public boolean isDone = false;
    
    public Task (String name
                ,String description
                ,LinkedList<Note> notes
                ,GregorianCalendar date) {
        this.name = name;
        this.description = description;
        this.notes = notes;
        this.date = date;
    }

    public Task (){ }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedList<Note> getNotes() {
        return notes;
    }

    public void setNotes(LinkedList<Note> notes) {
        this.notes = notes;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public Task copy () {
        return new Task (this.name
                        ,this.description
                        ,this.notes
                        ,this.date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;
        
        if (getClass() != obj.getClass())
            return false;

        final Task other = (Task) obj;
        
        if (!Objects.equals(this.name, other.name))
            return false;

        if (!Objects.equals(this.description, other.description))
            return false;

        if (!Objects.equals(this.notes, other.notes))
            return false;
        
        if (!Objects.equals(this.date, other.date))
            return false;

        return true;
    }
}

