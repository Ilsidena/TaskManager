package classes.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author anna
 */
public class ManagerTest extends TestCase{
    Manager manager;

    @Before
    public void setUp() {
        manager = new Manager ();
    }
    
    /**
     * Test of addTask method, of class Manager.
     */
    @Test
    public void testAddTask() {
        System.out.println ("addTask");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        assertTrue (manager.addTask (name, description
                                         ,notes, date, repeats));
        assertFalse (manager.addTask (name, description
                                         ,notes, date, repeats));
        
    }

    /**
     * Test of editTask method, of class Manager.
     */
    @Test
    public void testEditTask() {
        System.out.println ("editTask");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        manager.addTask (name, description, notes, date, repeats);
        assertTrue (manager.editTask (name, date, false, "Go to the cafe", description
                                         ,notes, date, repeats));
        assertTrue (manager.editTask ("Go to the cafe", date, false, name, description
                                         ,notes, date, repeats));
        assertFalse (manager.editTask ("Go to the cafe", date, false, name, description
                                         ,notes, date, repeats));
    }

    /**
     * Test of setComplete method, of class Manager.
     */
    @Test
    public void testSetComplete() {
        System.out.println ("setComplete");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        manager.addTask (name, description, notes, date, repeats);
        assertTrue (manager.setComplete (name, date));
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        manager.addTask (name, description, notes, date, repeats);
        assertTrue (manager.setComplete (name, date));
        assertFalse (manager.getDoneTasks().size() < 2);
    }

    /**
     * Test of deleteTask method, of class Manager.
     */
    @Test
    public void testDeleteTask () {
        System.out.println("deleteTask");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        manager.addTask (name, description, notes, date, repeats);
        assertFalse (manager.deleteTask ("Cookies", date));
        assertTrue (manager.deleteTask (name, date));
    }

    /**
     * Test of getDateTask method, of class Manager.
     */
    @Test
    public void testGetDateTask() {
        System.out.println("getDateTask");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.NONE;
        
        manager.addTask (name, description, notes, date, repeats);
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        manager.addTask (name, description, notes, date, repeats);
        
        assertTrue (!manager.getDateTask (date).isEmpty ());
        date = new GregorianCalendar (2020, 9, 11);
        assertFalse (!manager.getDateTask (date).isEmpty ());
    }

    /**
     * Test of getDoneTasks method, of class Manager.
     */
    @Test
    public void testGetDoneTasks() {
        System.out.println("getDoneTasks");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        manager.addTask (name, description, notes, date, repeats);
        manager.setComplete (name, date);
        assertTrue (manager.getDoneTasks ().size() == 1);
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        manager.addTask (name, description, notes, date, repeats);        
        manager.setComplete (name, date);
        
        assertTrue (manager.getDoneTasks ().size() == 2);
        manager.setUncomplete (name, date);
        assertFalse (manager.getDoneTasks ().size() == 2);
    }

    /**
     * Test of findTask method, of class Manager.
     */
    @Test
    public void testFindTask() {
        System.out.println ("findTask");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        manager.addTask (name, description, notes, date, repeats);
        assertTrue (manager.findTask (name) != null);
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        manager.addTask (name, description, notes, date, repeats);        
        
        assertTrue (manager.findTask (name) != null);
        assertFalse (manager.findTask ("Cookies") != null);
    }

    /**
     * Test of getReapeats method, of class Manager.
     */
    @Test
    public void testGetRepeats() {
        System.out.println ("getReapeats");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        Task task = new Task (name, description, notes, date);
        
        manager.addTask (name, description, notes, date, repeats);
        assertTrue (manager.getRepeats (task) == Manager.REPEATS.DAILY);
        assertFalse (manager.getRepeats (task) == Manager.REPEATS.NONE);
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        task = new Task (name, description, notes, date);
        manager.addTask (name, description, notes, date, repeats);        
        
        assertTrue (manager.getRepeats (task) == Manager.REPEATS.NONE);
    }

    /**
     * Test of addNote method, of class Manager.
     */
    @Test
    public void testAddNote() {
        System.out.println ("addNote");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        assertTrue (manager.getNotes ().size() == 1);
        assertFalse (manager.getNotes ().size() > 1);
        
        name = "Adress";
        description = "1600 Amphitheatre Parkway, Mountain View, California, U.S.";
        manager.addNote (name, description);
        assertTrue (manager.getNotes ().size() == 2);
    }

    /**
     * Test of editNote method, of class Manager.
     */
    @Test
    public void testEditNote() {
        System.out.println ("editNote");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        
        assertTrue (manager.editNote (name, "Cookeis", "Receipe"));
        assertTrue (manager.editNote ("Cookeis", name, "Receipe"));
        assertFalse (manager.editNote ("Cookeis", name, "Receipe"));
    }

    /**
     * Test of deleteNote method, of class Manager.
     */
    @Test
    public void testDeleteNote() {
        System.out.println ("deleteNote");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        
        assertTrue (manager.deleteNote (name));
        assertFalse (manager.deleteNote ("Cookeis"));
        assertFalse (manager.deleteNote ("Receipe"));
    }

    /**
     * Test of findNote method, of class Manager.
     */
    @Test
    public void testFindNote() {
        System.out.println ("findNote");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        assertTrue (manager.findNote (name) != null);
        
        name = "Cookies";
        description = "Receipe";
        manager.addNote (name, description);
        assertTrue (manager.findNote (name) != null);
        assertFalse (manager.findNote ("Receipe") != null);
    }

    /**
     * Test of getNote method, of class Manager.
     */
    @Test
    public void testGetNote() {
        System.out.println ("getNote");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        
        name = "Cookies";
        description = "Receipe";
        manager.addNote (name, description);
        
        assertTrue (manager.getNote (0) != null);
        assertTrue (manager.getNote (1) != null);
        assertFalse (manager.getNote (2) != null);
    }

    /**
     * Test of getNotes method, of class Manager.
     */
    @Test
    public void testGetNotes() {
        System.out.println ("getNotes");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        assertTrue (manager.getNotes ().size() == 1);
        
        name = "Cookies";
        description = "Receipe";
        manager.addNote (name, description);
        
        assertTrue (manager.getNotes ().size() == 2);
        assertFalse (manager.getNotes () == null);
    }

    /**
     * Test of toString method, of class Manager.
     */
    @Test
    public void testToString() {
        System.out.println ("toString");
        
        assertEquals (manager.toString(), "0\n0\n0\n0\n0\n0\n0\n");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        manager.addNote (name, description);
        
        assertEquals (manager.toString(), "1\nGo to the bank\n"
                + "Pay the fine\n0\n0\n0\n0\n0\n0\n");
        assertFalse (manager.toString () == null);
    }

    /**
     * Test of setUncomplete method, of class Manager.
     */
    @Test
    public void testSetUncomplete() {
        System.out.println ("setUncomplete");
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        
        manager.addTask (name, description, notes, date, repeats);
        assertTrue (manager.getDoneTasks ().size() == 0);
        manager.setUncomplete (name, date);
        assertTrue (manager.getDoneTasks ().isEmpty());
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        manager.addTask (name, description, notes, date, repeats);  
        manager.setUncomplete (name, date);
        assertTrue (manager.getDoneTasks ().isEmpty());
        assertFalse (manager.getDoneTasks ().size() == 2);
    }    
}