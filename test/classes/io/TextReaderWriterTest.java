package classes.io;

import classes.model.Manager;
import classes.model.Note;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;

/**
 * @author anna
 */
public class TextReaderWriterTest extends TestCase {
    Manager manager;
    
    @After
    @Override
    protected void tearDown() throws Exception {
        manager = null;
    }
    
    /**
     * Test of read method, of class TextReaderWriter.
     * @throws java.lang.Exception
     */
    @Test
    public void testRead () throws Exception {
        System.out.println ("read");
        
        OutputStream output = new FileOutputStream ("test/resourses/testIn.txt");
        BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (output));        
        writer.write ("0\n0\n0\n0\n0\n0\n0\n"); 
        writer.close();
        
        manager = TextReaderWriter.read ("test/resourses/testIn.txt");
        
        assertTrue (manager.getNotes ().isEmpty ());
        assertTrue (manager.getDoneTasks ().isEmpty ());
        assertFalse (manager.getDateTask (new GregorianCalendar (2018, 6, 6)).size () == 5);
    }

    /**
     * Test of write method, of class TextReaderWriter.
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        System.out.println("write");
        
        manager = new Manager ();
        TextReaderWriter.write (manager, "test/resourses/testOut.txt");
        InputStream input = new FileInputStream ("test/resourses/testOut.txt");
        BufferedReader reader = new BufferedReader (new InputStreamReader (input));
        
        for (int i = 0; i < Manager.capacity + 1; ++i) {
            assertEquals ("0", reader.readLine ());
        }
        
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
        
        TextReaderWriter.write (manager, "test/resourses/testOut.txt");
        
        assertFalse ("0".equals (reader.readLine()));
        assertTrue ("Adress".equals (reader.readLine ()));
        
        reader.close ();
    }    
}
