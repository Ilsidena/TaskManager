package classes.io;

import java.io.*;
import classes.model.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * @author anna
 */

public class CSVWriter {
    public static enum MODE {NOTES
                            ,DAY
                            ,WEEK
                            ,MONTH}
    
    private static String CSVFilename;
    private static char separator = ';';
    private static Manager manager;
    
    
    private static final String [] DAYS = { "Sun"
                                            ,"Mon"
                                            ,"Tue"
                                            ,"Wed"
                                            ,"Thu"
                                            ,"Fri"
                                            ,"Sat"};
    
    private static final String [] MONTHS = {"January"
                                            ,"February"
                                            ,"March"
                                            ,"April"
                                            ,"May"
                                            ,"June"
                                            ,"July"
                                            ,"August"
                                            ,"September"
                                            ,"October"
                                            ,"November"
                                            ,"December"};
    
    /**
     * Write Manager parts to CSV file 
     * @param newCSVFilename - name of result file
     * @param mode - 4 modes (NOTES, DAY, WEEK, MONTH) for choosing of content
     * @param newManager - Manager for writing
     * @param newSeparator - choosing of separator (',', ';') for veiwer
     * @throws IOException
     */
    public static void write (String newCSVFilename
                             ,MODE mode
                             ,Manager newManager
                             ,char newSeparator) throws IOException 
    {
        CSVFilename = newCSVFilename;
        manager = newManager;
        separator = newSeparator;
        GregorianCalendar date = new GregorianCalendar ();
        GregorianCalendar TODAY = new GregorianCalendar 
                                            (date.get (Calendar.YEAR)
                                            ,date.get (Calendar.MONTH)
                                            ,date.get (Calendar.DATE));
        
        switch (mode) {
            case NOTES:
                writeNotes (TODAY);
                break;
            case DAY:
                writeDayTask (TODAY);
                break;
            case WEEK:
                writeWeekTask (TODAY);
                break;
            case MONTH:
                writeMonthTask (TODAY);
                break;
        }
    }    
    
    private static void writeNotes (GregorianCalendar TODAY) throws IOException {
	OutputStream output = new FileOutputStream (CSVFilename);
	BufferedWriter writer = new BufferedWriter 
                                (new OutputStreamWriter (output));
        
        StringBuilder builder = new StringBuilder ();
        builder.append ("\"Notes (" + (manager.getNotes ().size ()) + "):\"" 
                        + separator + "\"Descriptions:\"\n");
            
        for (Note itr : manager.getNotes()) {
            builder.append ("\"" + processString (itr.getName ())
                            + "\"" + separator + "\""
                            + processString (itr.getDescription ()) + "\"\n");
        }
        
        writer.write(builder.toString());
	writer.close ();
    }
    
    private static void writeDayTask (GregorianCalendar TODAY) throws IOException {
        OutputStream output = new FileOutputStream (CSVFilename);
	BufferedWriter writer = new BufferedWriter 
                                (new OutputStreamWriter (output));
        
        writer.write (dateToString (TODAY));
	writer.close ();
    }
    
    private static void writeWeekTask (GregorianCalendar TODAY) throws IOException {
        OutputStream output = new FileOutputStream (CSVFilename);
	BufferedWriter writer = new BufferedWriter 
                                (new OutputStreamWriter (output));
        
        StringBuilder builder = new StringBuilder ();
        TODAY.add (Calendar.DAY_OF_WEEK, 1 - TODAY.get (Calendar.DAY_OF_WEEK));
        
        builder.append ("Week " 
                        + TODAY.get (Calendar.WEEK_OF_YEAR)
                        + " (" + getStatistic (TODAY) + ")"
                        + separator + "\"\""
                        + separator + "\"\""
                        + "\n");
        
        for ( ; ; ) {
            builder.append (dateToString (TODAY) + "\"\"" + separator + "\"\"\n");
            
            if (TODAY.get (Calendar.DAY_OF_WEEK) 
                    == TODAY.getActualMaximum (Calendar.DAY_OF_WEEK)) break;
            
            TODAY.add (Calendar.DAY_OF_WEEK, 1);
        }
        
        writer.write (builder.toString());
	writer.close ();
    }
    
    private static void writeMonthTask (GregorianCalendar TODAY) throws IOException {
        OutputStream output = new FileOutputStream (CSVFilename);
	BufferedWriter writer = new BufferedWriter 
                                (new OutputStreamWriter (output));
        
        StringBuilder builder = new StringBuilder ();
        builder.append (MONTHS[TODAY.get (Calendar.MONTH)] 
                        + " / " + TODAY.get (Calendar.YEAR)
                        + separator + "\"\""
                        + separator + "\"\""
                        + separator + "\"\""
                        + separator + "\"\""
                        + separator + "\"\""
                        + separator + "\"\""
                        + separator + "\"\"");
        
        builder.append ("\nWeek (Statistic)");        
        for (String itr : DAYS)
            builder.append (separator + itr);        
        builder.append ("\n");
        
        TODAY.add (Calendar.DAY_OF_MONTH
                    ,1 - TODAY.get (Calendar.DAY_OF_MONTH));
        boolean isEnd = false;
        boolean isFirst = true;
        
        for (int i = 0; i < 6 && !isEnd; ++i) {
            builder.append (TODAY.get (Calendar.WEEK_OF_YEAR) 
                            + " (" + getStatistic (TODAY) + ")");
            int dayCounter = TODAY.getActualMaximum (Calendar.DAY_OF_WEEK);
            
            for (int j = 0; 
                j < dayCounter && !isEnd; 
                ++j) 
            {
                if (isFirst) {
                    for (int k = 1; k < TODAY.get (Calendar.DAY_OF_WEEK); ++k)
                        builder.append (separator + "\"\"");
                    
                    isFirst = false;
                    dayCounter -= TODAY.get (Calendar.DAY_OF_WEEK) - 1;
                }
                    
                builder.append (separator + dateToCell (TODAY));
                
                if (TODAY.get (Calendar.DAY_OF_MONTH) 
                == TODAY.getActualMaximum (Calendar.DAY_OF_MONTH)) isEnd = true;
                    
                TODAY.add (Calendar.DAY_OF_WEEK, 1);
            }
            
            builder.append ("\n");
        }
        
        writer.write (builder.toString());
	writer.close ();
    }
    
    private static String dateToString (GregorianCalendar TODAY) {
        StringBuilder builder = new StringBuilder ();
        
        builder.append ("Date:" 
                        + separator + "\"" 
                        + DAYS[TODAY.get (Calendar.DAY_OF_WEEK) - 1] + ", "
                        + TODAY.get (Calendar.DATE) + "." 
                        + ((TODAY.get (Calendar.MONTH) + 1) < 10 
                        ? "0" + (TODAY.get (Calendar.MONTH) + 1)
                        : (TODAY.get (Calendar.MONTH)) + 1));
        
        builder.append ("\"" + separator +"\"\"" + separator + "\"\"\n");
        
        builder.append ("\"Tasks (" + manager.getDateTask (TODAY).size () 
                        + "):\"" 
                        + separator + "\"Descriptions:\"" 
                        + separator + "\"Notes:\"\n");
            
        for (Task itr : manager.getDateTask (TODAY)) {
            builder.append ("\"" + processString (itr.getName ())
                            + "\"" + separator + "\""
                            + processString (itr.getDescription ()) 
                            + "\"" + separator + "\"");
            
            for (Note note : itr.getNotes()) {
                builder.append (processString (note.getName ()) 
                                + ": " + processString (note.getDescription ()) 
                                + "\n");
            }
            
            builder.append ("\"\n");
        }
        
        return builder.toString ();
    }
    
    private static String dateToCell (GregorianCalendar TODAY) {
        StringBuilder builder = new StringBuilder ();
        builder.append ("\"" + TODAY.get (Calendar.DAY_OF_MONTH) + "\n");
        boolean first = true;
        LinkedList <Task> list = manager.getDateTask (TODAY);
        
        for (Task itr : list) {
            for (Task done : manager.getDoneTasks()) {
                if (itr.getDate().equals(done.getDate())
                        && itr.getName().equals(done.getName()))
                    list.remove(itr);
            }
        }
        
        for (Task itr : list) {
            if (! first) 
                builder.append ("\n");
            
            first = false;
            builder.append (processString (itr.getName ()) + ":\n"
                            + processString (itr.getDescription ()) + "\n");
        }
        
        builder.append ("\"");
        return builder.toString ();
    }
    
    private static int getStatistic (GregorianCalendar TODAY) {
        int result = 0;
        int day = TODAY.get (Calendar.DAY_OF_WEEK);
        
        for ( ; ; ) {
            result += manager.getDateTask (TODAY).size();
            
            if (TODAY.get (Calendar.DAY_OF_WEEK) 
                == TODAY.getActualMaximum (Calendar.DAY_OF_WEEK)
                || TODAY.get (Calendar.DAY_OF_MONTH) 
                == TODAY.getActualMaximum (Calendar.DAY_OF_MONTH)) break;
            
            TODAY.add (Calendar.DAY_OF_WEEK, 1);
        }
        
        TODAY.set (Calendar.DAY_OF_WEEK, day);
        return result;
    }
    
    private static String processString (String str) {
        StringBuilder builder = new StringBuilder ();
        
        if (str != null) {
            char [] arr = str.toCharArray();
            boolean isOpened = false;

            for (char itr : arr) {
                if (itr == '"') {
                    if (isOpened)
                        builder.append ('»');          
                    else
                        builder.append ('«');

                    isOpened = !isOpened;
                    continue;
                }

                builder.append (itr);
            }
        }
        
        return builder.toString ();
    }
}