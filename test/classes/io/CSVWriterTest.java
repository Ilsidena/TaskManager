package classes.io;

import classes.model.Manager;
import classes.model.Note;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * @author anna
 */
public class CSVWriterTest extends TestCase {
    Manager manager;
    
    @Before
    @Override
    public void setUp() {
        manager = new Manager ();
        
        String name = "Go to the bank";
        String description = "Pay the fine";
        LinkedList<Note> notes = new LinkedList <> ();
        notes.add (new Note ("Adress"
                            ,"1600 Amphitheatre Parkway, Mountain View, California, U.S."));
        notes.add (new Note ("Card", "0000 0000 1234 5678"));
        GregorianCalendar date = new GregorianCalendar (2018, 9, 11);
        Manager.REPEATS repeats = Manager.REPEATS.DAILY;
        manager.addTask (name, description, notes, date, repeats);
        
        name = "Go to the cafe";
        description = "At 3 p.m.";
        repeats = Manager.REPEATS.NONE;
        manager.addTask (name, description, notes, date, repeats);
        
        manager.addNote ("Adress"
                        ,"1600 Amphitheatre Parkway, Mountain View, California, U.S.");
        manager.addNote ("Card", "0000 0000 1234 5678");        
    }

    /**
     * Test of write method, of class CSVWriter.
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        System.out.println ("write");
        
        CSVWriter.write ("test/resourses/testCSV.txt"
                        ,CSVWriter.MODE.NOTES, manager, ',');
        
        InputStream input = new FileInputStream ("test/resourses/testCSV.txt");
        BufferedReader reader = new BufferedReader (new InputStreamReader (input));
        LinkedList<Note> notes = manager.getNotes ();
        String str = reader.readLine ();
        
        assertEquals (str
                    ,"\"Notes (" + notes.size() + "):\",\"Descriptions:\"");
        assertFalse (str.equals ("\nNotes\n"));
        assertEquals (reader.readLine()
                     ,"\"Card\",\"0000 0000 1234 5678\"");
        assertEquals (reader.readLine()
                    ,"\"Adress\"" 
                    + ",\"1600 Amphitheatre Parkway, Mountain View, California, U.S.\"");
        
        
        reader.close ();
    }   
}