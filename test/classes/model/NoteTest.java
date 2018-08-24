package classes.model;

import java.lang.reflect.Field;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author anna
 */
public class NoteTest extends TestCase {
    Note note;

    @Before
    @Override
    public void setUp () {
        note = new Note ("Card", "0000 0000 1234 5678");
    }
    
    /**
     * Test of getName method, of class Note.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetName () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("getName");
        
        assertTrue ("Card".equals(note.getName()));
        assertFalse ("Adress".equals(note.getName()));
        
        Field field = note.getClass ().getDeclaredField ("name");
        field.setAccessible (true);
        field.set (note, "Adress");
        
        assertTrue ("Adress".equals (note.getName ()));
    }

    /**
     * Test of setName method, of class Note.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetName () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("setName");
        
        note.setName ("Card");

        Field field = note.getClass ().getDeclaredField ("name");
        field.setAccessible (true);
        
        assertTrue ("Card".equals (field.get (note)));
        note.setName ("Adress");
        assertFalse ("Card".equals (field.get (note)));
        assertTrue ("Adress".equals (field.get (note)));
    }

    /**
     * Test of getDescription method, of class Note.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetDescription () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("getDescription");
        
        assertTrue ("0000 0000 1234 5678".equals (note.getDescription ()));
        assertFalse ("Moscow".equals (note.getDescription ()));
        
        Field field = note.getClass ().getDeclaredField ("description");
        field.setAccessible (true);        
        field.set (note, "Moscow");
        
        assertTrue ("Moscow".equals (note.getDescription ()));
    }

    /**
     * Test of setDescription method, of class Note.
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetDescription () 
            throws NoSuchFieldException, IllegalAccessException {
        System.out.println ("setDescription");
        
        note.setDescription ("0000 0000 1234 5678");
        
        Field field = note.getClass().getDeclaredField ("description");
        field.setAccessible (true);
        
        assertTrue ("0000 0000 1234 5678".equals (field.get (note)));
        note.setDescription ("Moscow");
        assertFalse ("0000 0000 1234 5678".equals (field.get (note)));
        assertTrue ("Moscow".equals (field.get (note)));
    }

    /**
     * Test of equals method, of class Note.
     */
    @Test
    public void testEquals () {
        System.out.println ("equals");
        
        Note expected = new Note ("Card", "0000 0000 1234 5678");
        assertTrue (note.equals (expected) == true);
        assertFalse (note.equals (null) == true);
    }

    /**
     * Test of toString method, of class Note.
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.NoSuchFieldException
     */
    @Test
    public void testToString () 
            throws IllegalAccessException, NoSuchFieldException {
        System.out.println ("toString");
        
        Field name = note.getClass ().getDeclaredField ("name");
        name.setAccessible (true);
        Field description = note.getClass ().getDeclaredField ("description");
        description.setAccessible (true);
        
        assertTrue (note.toString ().equals (name.get(note) + "\n" 
                                            + description.get (note)));
        assertFalse (note.toString ().equals (null));
    }
    
}
