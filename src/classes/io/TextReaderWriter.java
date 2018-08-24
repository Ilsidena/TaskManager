package classes.io;

import java.io.*;
import classes.model.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author anna
 */

public class TextReaderWriter {
    public static Manager read (String filename) throws IOException {
        InputStream input = new FileInputStream (filename);
        BufferedReader reader = new BufferedReader (new InputStreamReader (input));
        
        Manager manager = new Manager ();
        readNotes (reader, manager);
        readTasks (reader, manager);
        readDoneTasks (reader, manager);
            
        reader.close ();
        return manager;
    }
    
    private static void readNotes (BufferedReader reader, Manager manager) 
            throws IOException 
    {
        int size = Integer.parseInt (reader.readLine ());
        String name;
        String description;
        
        for (int i = 0; i < size; ++i) {
            name = reader.readLine();
            description = reader.readLine();
            (manager.getNotes ()).add (new Note (name, description));
        }
    }
    
    private static void readTasks (BufferedReader reader, Manager manager) 
            throws IOException 
    {
        int size = 0;
        Manager.REPEATS repeats = Manager.REPEATS.NONE;
        String name;
        String description;
        GregorianCalendar date;        
        
        for (int h = 0; h < Manager.capacity - 1; ++h) {
            size = Integer.parseInt (reader.readLine ());
            
            switch (h) {
                case 1:
                    repeats = Manager.REPEATS.DAILY;
                    break;
                case 2:
                    repeats = Manager.REPEATS.WEEKLY;
                    break;
                case 3:
                    repeats = Manager.REPEATS.MONTHLY;
                    break;
                case 4:
                    repeats = Manager.REPEATS.YEARLY;
                    break;
            }

            for (int i = 0; i < size; i++) {
                name = reader.readLine ();
                description = reader.readLine ();

                Scanner scanner = new Scanner (reader.readLine ());
                scanner.useDelimiter ("/");
                int day = scanner.nextInt ();
                int month = scanner.nextInt ();
                int year = scanner.nextInt ();

                date = new GregorianCalendar (year, month - 1, day);

                char [] arr = reader.readLine ().toCharArray ();
                LinkedList <Note> notes = new LinkedList <> ();
                
                for (int j = 1; arr[j] != '}'; ++j) {
                    StringBuilder builder = new StringBuilder ();

                    for (; arr[j] != ' '; ++j)
                        builder.append (arr[j]);

                    notes.add (manager.getNote (Integer.parseInt (builder.toString ())));
                }

                manager.addTask (name
                                ,description
                                ,notes
                                ,date
                                ,repeats);
            }
        }
    }
    
    private static void readDoneTasks (BufferedReader reader, Manager manager) 
            throws IOException 
    {
        String name;
        String description;
        GregorianCalendar date;        
        LinkedList <Note> notes = new LinkedList <> ();
        int size = Integer.parseInt (reader.readLine ());
        
        for (int i = 0; i < size; i++) {
                name = reader.readLine ();
                description = reader.readLine ();

                Scanner scanner = new Scanner (reader.readLine ());
                scanner.useDelimiter ("/");
                int day = scanner.nextInt ();
                int month = scanner.nextInt ();
                int year = scanner.nextInt ();

                date = new GregorianCalendar (year, month - 1, day);

                char [] arr = reader.readLine ().toCharArray ();

                for (int j = 1; arr[j] != '}'; ++j) {
                    StringBuilder builder = new StringBuilder ();

                    for (; arr[j] != ' '; ++j)
                        builder.append (arr[j]);

                    notes.add (manager.getNote (Integer.parseInt (builder.toString ())));
                }
                
                manager.getDoneTasks().add (new Task (name
                                                    ,description
                                                    ,notes
                                                    ,date));
            }
    }
    
    public static void write (Manager manager, String filename) throws IOException {
        OutputStream output = new FileOutputStream (filename);
        BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (output));

        writer.write (manager.toString());
        writer.close ();
    }
}
