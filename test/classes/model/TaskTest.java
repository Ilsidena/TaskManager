package classes.model;

import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author anna
 */
public class TaskTest extends TestCase {
    Task task;
    LinkedList <Note> notes;

    @Before
    public void setUp () {
        notes = new LinkedList <> ();
        
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        
        task = new Task ("Go to the bank"
                        ,"Pay the fine"
                        ,notes
                        ,new GregorianCalendar (2018, 9, 11));
    }

    /**
     * Test of getName method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetName() 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("getName");
        
        assertTrue ("Go to the bank".equals (task.getName ()));
        assertFalse ("Go to the cafe".equals (task.getName ()));
        
        Field field = task.getClass ().getDeclaredField ("name");
        field.setAccessible (true);
        field.set (task, "Go to the cafe");
        
        assertTrue ("Go to the cafe".equals (task.getName ()));
    }

    /**
     * Test of setName method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetName () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("setName");
        
        task.setName ("Go to the bank");

        Field field = task.getClass().getDeclaredField ("name");
        field.setAccessible (true);
        
        assertTrue ("Go to the bank".equals (field.get (task)));
        task.setName ("Go to the cafe");
        assertFalse ("Go to the bank".equals (field.get (task)));
        assertTrue ("Go to the cafe".equals (field.get (task)));
    }

    /**
     * Test of getDescription method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetDescription () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("getDescription");
        
        assertTrue ("Pay the fine".equals (task.getDescription ()));
        assertFalse ("Steal money".equals (task.getDescription ()));
        
        Field field = task.getClass ().getDeclaredField ("description");
        field.setAccessible (true);        
        field.set (task, "Steal money");
        
        assertTrue ("Steal money".equals (task.getDescription ()));
    }

    /**
     * Test of setDescription method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetDescription () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("setDescription");
        
        task.setDescription ("Pay the fine");
        
        Field field = task.getClass().getDeclaredField ("description");
        field.setAccessible (true);
        
        assertTrue ("Pay the fine".equals (field.get (task)));
        task.setDescription ("Steal money");
        assertFalse ("Pay the fine".equals (field.get (task)));
        assertTrue ("Steal money".equals (field.get (task)));
    }

    /**
     * Test of getNotes method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetNotes () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("getNotes");
        
        assertTrue (notes.equals (task.getNotes ()));
        assertFalse (task.getNotes ().equals (null));
        
        Field field = task.getClass().getDeclaredField ("notes");
        field.setAccessible (true);
        field.set (task, null);
        
        assertTrue (task.getNotes () == null);
    }

    /**
     * Test of setNotes method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetNotes () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("setNotes");
        
        task.setNotes (notes);
        
        Field field = task.getClass().getDeclaredField ("notes");
        field.setAccessible (true);
        
        assertTrue (field.get (task).equals(notes));
        task.setNotes (null);
        assertFalse (notes.equals (field.get (task)));
        assertTrue (field.get (task) == null);
    }

    /**
     * Test of getDate method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetDate () throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("getDate");
        
        assertTrue ((new GregorianCalendar (2018, 9, 11)).equals (
                                                            task.getDate ()));
        assertFalse ((new GregorianCalendar (1999, 9, 11)).equals (
                                                            task.getDate ()));
        
        Field field = task.getClass ().getDeclaredField ("date");
        field.setAccessible (true);
        field.set (task, new GregorianCalendar (1999, 9, 11));
        
        assertTrue ((new GregorianCalendar (1999, 9, 11)).equals (
                                                            task.getDate ()));
    }

    /**
     * Test of setDate method, of class Task.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetDate () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("setDate");
        
        task.setDate (new GregorianCalendar (2018, 9, 11));
        
        Field field = task.getClass().getDeclaredField ("date");
        field.setAccessible (true);
        
        assertTrue ((new GregorianCalendar (2018, 9, 11)).equals (
                                                            field.get (task)));
        task.setDate (new GregorianCalendar (1999, 9, 11));
        assertFalse (new GregorianCalendar (2018, 9, 11).equals (
                                                            field.get (task)));
        assertTrue (new GregorianCalendar (1999, 9, 11).equals (
                                                            field.get (task)));
    }

    /**
     * Test of copy method, of class Task.
     * @throws java.lang.NoSuchFieldException
     */
    @Test
    public void testCopy () throws NoSuchFieldException {
        System.out.println ("copy");
        
        Task copy = task.copy ();        
        assertFalse (copy == null);
        
        Field name = task.getClass ().getDeclaredField ("name");
        name.setAccessible (true);
        Field description = task.getClass ().getDeclaredField ("description");
        description.setAccessible (true);
        Field note = task.getClass ().getDeclaredField ("notes");
        note.setAccessible (true);
        Field date = task.getClass ().getDeclaredField ("date");
        date.setAccessible (true);
        
        Field copyName = copy.getClass ().getDeclaredField ("name");
        copyName.setAccessible (true);
        Field copyDescription = copy.getClass ().getDeclaredField ("description");
        copyDescription.setAccessible (true);
        Field copyNote = copy.getClass ().getDeclaredField ("notes");
        copyNote.setAccessible (true);
        Field copyDate = copy.getClass ().getDeclaredField ("date");
        copyDate.setAccessible (true); 
        
        assertTrue ((name.equals (copyName))
                    && (description.equals (copyDescription))
                    && (note.equals (copyNote))
                    && (date.equals (copyDate)));
    }

    /**
     * Test of equals method, of class Task.
     */
    @Test
    public void testEquals () {
        System.out.println("equals");
        
        Task expected = new Task ("Go to the bank"
                                    ,"Pay the fine"
                                    ,notes
                                    ,new GregorianCalendar (2018, 9, 11));
        
        assertTrue (task.equals (expected) == true);
        assertFalse (task.equals (null) == true);
        
    }   
}