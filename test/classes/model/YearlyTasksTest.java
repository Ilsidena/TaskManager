package classes.model;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author anna
 */
public class YearlyTasksTest extends TestCase {
    YearlyTasks tasks;
    LinkedList <Note> notes;

    @Before
    @Override
    public void setUp() {
        tasks = new YearlyTasks ();
        notes = new LinkedList <> ();
        
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        
        tasks.add (new Task ("Go to the bank", "Pay the fine", notes
                    ,new GregorianCalendar (2018, 9, 11)));
    }

    /**
     * Test of add method, of class DailyTasks.
     * @throws java.lang.NoSuchFieldException
     */
    @Test
    public void testAdd() throws NoSuchFieldException {
        System.out.println ("add");
        
        Task taskTrue = new Task ("Visit Granny", "Buy cookies", null
                                    ,new GregorianCalendar (2018, 3, 7));
        Task taskFalse = new Task ("Go to the bank", "Pay the fine", notes
                                    ,new GregorianCalendar (2018, 9, 11));
        
        int size = tasks.size();
        tasks.add (taskTrue);
        assertTrue (tasks.size () > size);
        size = tasks.size ();
        tasks.add (taskFalse);
        assertTrue (tasks.size () > size);
    }

    /**
     * Test of delete method, of class DailyTasks.
     */
    @Test
    public void testDelete_Task() {
        System.out.println ("deleteTask");
        
        Task taskFalse = new Task ("Visit Granny", "Buy cookies", null
                                    ,new GregorianCalendar (2018, 3, 7));
        Task taskTrue = new Task ("Go to the bank", "Pay the fine", notes
                                    ,new GregorianCalendar (2018, 9, 11));
        
        assertTrue (tasks.delete (taskTrue));
        assertFalse (tasks.delete (taskFalse));
    }

    /**
     * Test of delete method, of class DailyTasks.
     */
    @Test
    public void testDelete_String() {
        System.out.println ("deleteString");
        
        assertTrue (tasks.delete ("Go to the bank"));
        assertFalse (tasks.delete ("Visit Granny"));
    }

    /**
     * Test of size method, of class DailyTasks.
     */
    @Test
    public void testSize() {
        System.out.println ("size");
        
        assertTrue (tasks.size () == 1);
        tasks.add (new Task ("Visit Granny", "Buy cookies", null
                    ,new GregorianCalendar (2018, 3, 7)));
        assertFalse (tasks.size () == 1);
        assertTrue (tasks.size () == 2);
        tasks.delete ("Visit Granny");
        assertFalse (tasks.size () == 2);
        assertTrue (tasks.size () == 1);
        
    }

    /**
     * Test of find method, of class DailyTasks.
     */
    @Test
    public void testFind_String() {
        System.out.println ("find");
        
        assertTrue (tasks.find ("Go to the bank") != null);
        assertFalse (tasks.find ("Visit Granny") != null);
        assertTrue (tasks.find ("Visit Granny") == null);
    }

    /**
     * Test of contains method, of class DailyTasks.
     */
    @Test
    public void testContains() {
        System.out.println ("contains");
        
        Task expected = new Task ("Go to the bank", "Pay the fine", notes
                                    ,new GregorianCalendar (2018, 9, 11));
        Task unexpected = new Task ("Visit Granny", "Buy cookies", null
                                    ,new GregorianCalendar (2018, 3, 7));
        
        assertTrue (tasks.contains(expected));
        assertTrue (!tasks.contains(unexpected));
        assertFalse (tasks.contains(unexpected));
    }

    /**
     * Test of find method, of class DailyTasks.
     */
    @Test
    public void testFind_String_GregorianCalendar() {
        System.out.println ("find");
        
        assertTrue (tasks.find ("Go to the bank"
                                ,new GregorianCalendar (2018, 9, 11)) != null);
        assertFalse (tasks.find ("Visit Granny"
                                ,new GregorianCalendar (2018, 9, 11)) != null);
        assertFalse (tasks.find ("Go to the bank"
                                ,new GregorianCalendar (1999, 7, 8)) != null);
    }

    /**
     * Test of getDateTasks method, of class DailyTasks.
     */
    @Test
    public void testGetDateTasks() {
        System.out.println ("getDateTasks");
        
        LinkedList <Task> list = new LinkedList <> ();
        list.add (new Task ("Go to the bank", "Pay the fine", notes
                    ,new GregorianCalendar (2018, 9, 11)));
        
        assertTrue (tasks.getDateTasks 
                            (new GregorianCalendar (2018, 9, 11)) != null);
        assertEquals (list
                    ,tasks.getDateTasks (new GregorianCalendar (2018, 9, 11)));
        assertFalse (!tasks.getDateTasks 
                            (new GregorianCalendar (1999, 1, 11)).isEmpty());
    }

    /**
     * Test of getTasks method, of class DailyTasks.
     */
    @Test
    public void testGetTasks() {
        System.out.println ("getTasks");
        
        assertTrue (tasks.getTasks ()!= null); 
        assertFalse (!tasks.getDateTasks 
                            (new GregorianCalendar (1999, 1, 11)).isEmpty());
    }    
}
