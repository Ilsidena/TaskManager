package classes.view;
import classes.io.CSVWriter;
import classes.io.TextReaderWriter;
import classes.model.Manager;
import classes.model.Note;
import classes.model.Task;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainPanel extends JPanel {

    private  Manager manager;
    private DayPanel dayPanel;
    private WeekPanel weekPanel;
    private final Date lowerLimit;
    private JPanel toolBar;
    private MonthPanel monthPanel;
    private NotePanel notePanel;
    private CompletedPanel completedPanel;
    private JComboBox scalingOptions;
    private JSpinner dateSpinner;
    private SpinnerListener spinnerListener;
    private JButton export;


    public MainPanel(Manager manager) {
        super();
        this.manager = manager;

        Date day = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(day);
        lowerLimit = toDate(date).getTime();


        this.setLayout(new BorderLayout());
        TaskActionListener taskActionListener = new TaskActionListener();
        
        export = new JButton("Export to CSV");
        export.addActionListener(new TaskActionListener());
        export.setActionCommand("Export");

        // Creating a tool bar
        /////////////////////////////////


        toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 250, 10));
        


        String[] options = {"Day", "Week", "Month", "Notes"};
        scalingOptions = new JComboBox(options);
        scalingOptions.setBackground(Color.white);
        scalingOptions.setSelectedIndex(0);
        scalingOptions.addActionListener(taskActionListener);
        toolBar.add(scalingOptions);

        Font standard = new Font("Helvetica", Font.PLAIN, 16);


        Date today = new Date();
        dateSpinner = new JSpinner(new SpinnerDateModel(today, lowerLimit, null,
                Calendar.DAY_OF_MONTH));
        dateSpinner.setFont(standard);
        dateSpinner.setPreferredSize(new Dimension(130, 30));
        JSpinner.DateEditor dailyEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dailyEditor);
        spinnerListener = new SpinnerListener();
        dateSpinner.addChangeListener(spinnerListener);
        toolBar.add(dateSpinner);
        toolBar.add(export);


        /////////////////////////////////


        dayPanel = new DayPanel(manager, getDate());
        weekPanel = new WeekPanel(manager);
        weekPanel.setDate(getDate().getTime());
        monthPanel = new MonthPanel(manager);
        monthPanel.setDate(getDate().getTime());
        completedPanel = new CompletedPanel(manager);
        //completedPanel.loadCompleted();
        notePanel = new NotePanel(manager);


        this.add(dayPanel, "Center");
        dayPanel.load();
        this.add(toolBar, "North");


    }



    private class SpinnerListener implements ChangeListener{

        private GregorianCalendar lastValue = null;
        private int loopCheck;

        public void stateChanged(ChangeEvent e){
            int option = scalingOptions.getSelectedIndex();
            switch (option){
                case 0:

                    dayPanel.load();
                    break;

                case 1:

                    skipWeek(getDate());
                    weekPanel.setDate(getDate().getTime());
                    weekPanel.loadWeek();
                    break;

                case 2:

                    monthPanel.setDate(getDate().getTime());
                    monthPanel.loadMonth();
                    break;
            }
        }



        private void skipWeek(GregorianCalendar curValue){

            if (curValue.equals(lastValue)) return;

            Date next = curValue.getTime();
            Date prev = lastValue.getTime();

            if (next.after(prev) && loopCheck < 6){

                loopCheck++;
                dateSpinner.setValue(dateSpinner.getNextValue());

            }

            else if (loopCheck < 6){

                loopCheck++;
                dateSpinner.setValue(dateSpinner.getPreviousValue());

            }

            else {

                lastValue = curValue;
                loopCheck = 0;

            }

        }

        public void setLastValue(GregorianCalendar value){ lastValue = value; }


    }




    private class TaskActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            Font standard = new Font("Helvetica", Font.PLAIN, 16);
            
            if (e.getActionCommand().equals("Export")){
                try{
                    CSVWriter.write("Manager.csv", CSVWriter.MODE.MONTH, manager, ',');
                }
                catch (IOException exception) {
                    JOptionPane.showConfirmDialog(null, exception.getMessage(), "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }


            // Change in scaling

            if (source.equals(scalingOptions)) {

                int option = scalingOptions.getSelectedIndex();
                Date today = new Date();

                if (option == 0) {


                    dateSpinner.setEnabled(true);
                    dateSpinner.setModel(new SpinnerDateModel(today, lowerLimit, null,
                            Calendar.DAY_OF_MONTH));
                    dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));



                    toolBar.getParent().remove(weekPanel);
                    toolBar.getParent().remove(completedPanel);
                    toolBar.getParent().remove(monthPanel);
                    toolBar.getParent().remove(notePanel);
                    toolBar.getParent().add(dayPanel);

                    toolBar.revalidate();

                    dayPanel.load();
                }

                else if (option == 1) {

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date = formatter.format(today);
                    GregorianCalendar calendar = toDate(date);
                    calendar.add(Calendar.DATE, -calendar.get(Calendar.DAY_OF_WEEK) + 1);
                    spinnerListener.setLastValue(calendar);


                    dateSpinner.setEnabled(true);
                    dateSpinner.setModel(new SpinnerDateModel(calendar.getTime(), calendar.getTime(), null,
                            Calendar.DAY_OF_MONTH));
                    dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));


                    toolBar.getParent().remove(dayPanel);
                    toolBar.getParent().remove(completedPanel);
                    toolBar.getParent().remove(monthPanel);
                    toolBar.getParent().remove(notePanel);
                    toolBar.getParent().add(weekPanel);

                    toolBar.revalidate();

                    weekPanel.loadWeek();
                }

                else if (option == 2) {

                    GregorianCalendar tmp = getDate();
                    tmp.set(Calendar.DATE, 1);
                    today = tmp.getTime();

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String date = formatter.format(lowerLimit);
                    tmp = toDate(date);
                    tmp.set(Calendar.DATE, 1);



                    dateSpinner.setEnabled(true);
                    dateSpinner.setModel(new SpinnerDateModel(today, tmp.getTime(), null,
                            Calendar.MONTH));
                    dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "MMMM yyyy"));



                    toolBar.getParent().remove(weekPanel);
                    toolBar.getParent().remove(completedPanel);
                    toolBar.getParent().remove(dayPanel);
                    toolBar.getParent().remove(notePanel);
                    toolBar.getParent().add(monthPanel);

                    toolBar.revalidate();

                    monthPanel.loadMonth();
                }

                else if (option == 3){

                    dateSpinner.setEnabled(false);

                    toolBar.getParent().remove(weekPanel);
                    toolBar.getParent().remove(dayPanel);
                    toolBar.getParent().remove(monthPanel);
                    toolBar.getParent().remove(completedPanel);
                    toolBar.getParent().add(notePanel);

                    toolBar.revalidate();

                    notePanel.loadNotes();

                }

                else if (option == 4){

                    dateSpinner.setEnabled(false);

                    toolBar.getParent().remove(weekPanel);
                    toolBar.getParent().remove(dayPanel);
                    toolBar.getParent().remove(monthPanel);
                    toolBar.getParent().remove(notePanel);
                    toolBar.getParent().add(completedPanel);

                    toolBar.revalidate();

                    completedPanel.loadCompleted();

                }


                toolBar.getTopLevelAncestor().repaint();
            }


        }
    }





    protected GregorianCalendar getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date theDay = (Date) dateSpinner.getValue();
        String date = formatter.format(theDay);

        return toDate(date);
    }





    protected static GregorianCalendar toDate(String date){

            Scanner scanner = new Scanner(date);
            scanner.useDelimiter("/");
            int day = scanner.nextInt();
            int month = scanner.nextInt();
            int year = scanner.nextInt();

            return new GregorianCalendar(year, month - 1, day);

    }






    protected static String dateToString(GregorianCalendar date){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date theDay = date.getTime();

        return formatter.format(theDay);
    }




    protected static void addTask(Manager manager, GregorianCalendar curDate){

        try {
            TaskDialogue input = new TaskDialogue(curDate.getTime(), manager);
            int result = input.executeDialogue(null);

            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return;

            String name;
            String description = null;
            GregorianCalendar date;

            if (input.getTypedName().length() > 0) {
                name = input.getTypedName();
            } else throw new IllegalArgumentException("A task must have a name");

            if (input.getTypedDescription().length() > 0) {
                description = input.getTypedDescription();
            }

            if (input.getTypedDate().length() > 0) {
                date = toDate(input.getTypedDate());
            } else throw new IllegalArgumentException("A task must have a deadline");

            Manager.REPEATS repeats = input.getRepeats();

            manager.addTask(name, description, input.getToBeAdded(),
                    date, repeats);

        }

        catch (IllegalArgumentException exception){

            JOptionPane.showConfirmDialog(null, exception.getMessage(), "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        }

        catch (InputMismatchException e){

            JOptionPane.showConfirmDialog(null,
                    "Incorrect date input format (Must be DD/MM/YYYY)", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);


        }


        try{

            TextReaderWriter.write(manager, "state.txt");

        }
        catch (Exception writerException) {

            System.out.println(writerException.getMessage());

        }
    }




    protected static void editTask(Manager manager, String name, GregorianCalendar curDate){

        try {

            Task selectedTask = manager.findTask(name);

            TaskDialogue input = new TaskDialogue(curDate.getTime(), manager);
            int result = input.executeDialogue(selectedTask);

            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return;

            String newName;
            String description;
            GregorianCalendar date = selectedTask.getDate();


            if (input.getTypedName() == null || input.getTypedName().length() == 0) {

                throw new IllegalArgumentException("A task must have a name");

            }

            newName = input.getTypedName();
            description = input.getTypedDescription();

            if (input.getTypedDate().length() > 0){

                date = toDate(input.getTypedDate());

            }

            Manager.REPEATS repeats = input.getRepeats();

            LinkedList<Note> resultList = selectedTask.getNotes();
            LinkedList<Note> added = input.getToBeAdded();
            LinkedList<Note> removed = input.getToBeRemoved();

            for (Note itr : removed) resultList.remove(itr);
            for (Note itr : added) resultList.add(itr);

            manager.editTask(selectedTask.getName(), selectedTask.getDate(), selectedTask.isDone, newName, description,
                    resultList, date, repeats);

        }



        catch (IllegalArgumentException e){

            JOptionPane.showConfirmDialog(null, e.getMessage(),
                    "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        }


        catch (InputMismatchException e){

            JOptionPane.showConfirmDialog(null,
                    "Incorrect date input format (Must be DD/MM/YYYY)", "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);


        }



        try{

            TextReaderWriter.write(manager, "state.txt");

        }
        catch (Exception writerException) {

            System.out.println(writerException.getMessage());

        }
        
    }




    protected static void completeTask(Manager manager, String name, GregorianCalendar curDate){

        Task selectedTask = manager.findTask(name);

        manager.setComplete(selectedTask.getName(), curDate);


        try{

            TextReaderWriter.write(manager, "state.txt");

        }
        catch (Exception writerException) {

            System.out.println(writerException.getMessage());

        }
      
    }


    protected static void setUncomplete(Manager manager, String name, GregorianCalendar date){

        manager.setUncomplete(name, date);

        try{

            TextReaderWriter.write(manager, "state.txt");

        }
        catch (Exception writerException) {

            System.out.println(writerException.getMessage());

        }

    }



    protected static void deleteTask(Manager manager, String name, String date, boolean isDone){

        manager.deleteTask(name, toDate(date));


        try{

            TextReaderWriter.write(manager, "state.txt");

        }
        catch (Exception writerException) {

            System.out.println(writerException.getMessage());

        }

    }


    protected static void saveToTxt(Manager manager){

        try{

            TextReaderWriter.write(manager, "state.txt");

        }
        catch (Exception writerException) {

            System.out.println(writerException.getMessage());

        }

    }




}


