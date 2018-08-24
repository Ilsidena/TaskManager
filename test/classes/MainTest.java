package classes;

import classes.io.*;
import classes.model.*;
import junit.textui.TestRunner;

public class MainTest {
    public static void main (String [] args) {
        TestRunner.run (CSVWriterTest.class);
        TestRunner.run (TextReaderWriterTest.class);
        
        TestRunner.run (DailyTasksTest.class);
        TestRunner.run (DoneTasksTest.class);
        TestRunner.run (ManagerTest.class);
        TestRunner.run (MonthlyTasksTest.class);
        TestRunner.run (NoteTest.class);
        TestRunner.run (OrdinaryTasksTest.class);
        TestRunner.run (TaskTest.class);
        TestRunner.run (WeeklyTasksTest.class);
        TestRunner.run (YearlyTasksTest.class);
    }
}