package classes.view;

import classes.model.Manager;
import classes.model.Note;
import classes.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class TaskDialogue implements ActionListener {
    private Date spinnerDate;
    private JTextField nameField;
    private JTextArea descriptionField;
    private JComboBox regularity;
    private HintTextField dateField;
    private JButton addNotes, removeNotes;
    private Manager.REPEATS repeats;
    private Task currentTask;
    private JList notesList;
    private Manager manager;
    private LinkedList<Note> toBeAdded;
    private LinkedList<Note> toBeRemoved;


    public TaskDialogue() {
        repeats = Manager.REPEATS.NONE;
    }

    public TaskDialogue(Date day, Manager manager){
        this.manager = manager;
        spinnerDate = day;
        repeats = Manager.REPEATS.NONE;
        toBeAdded = new LinkedList<>();
        toBeRemoved = new LinkedList<>();
        notesList = new JList();
        notesList.setLayoutOrientation(JList.VERTICAL);
        notesList.setSelectionModel(new DefaultListSelectionModel() {

            @Override
            public void setSelectionInterval(int index0, int index1) {
                if(super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                }
                else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });

        notesList.setFont(new Font("Helvetica", Font.PLAIN, 24));
    }


    public int executeDialogue(Task task){
        nameField = new JTextField();
        if (task != null) nameField.setText(task.getName());
        currentTask = task;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (task != null) {
            Date theDay = spinnerDate;
            String date = formatter.format(theDay);
            dateField = new HintTextField(date);

        }
        else {
            Date theDay = spinnerDate;
            String date = formatter.format(theDay);
            dateField = new HintTextField(date);
        }

        String[] options = {"None", "Daily", "Weekly", "Monthly", "Yearly"};
        regularity = new JComboBox(options);
        regularity.setBackground(Color.white);
        regularity.setSelectedIndex(0);
        regularity.addActionListener(new RegularityListener());

        if (task != null){
            Manager.REPEATS repeats = manager.getRepeats(task);

            switch (repeats){
                case NONE:
                    regularity.setSelectedIndex(0);
                    break;

                case DAILY:
                    regularity.setSelectedIndex(1);
                    break;

                case WEEKLY:
                    regularity.setSelectedIndex(2);
                    break;

                case MONTHLY:
                    regularity.setSelectedIndex(3);
                    break;

                case YEARLY:
                    regularity.setSelectedIndex(4);
                    break;
            }
        }


        descriptionField = new JTextArea();
        descriptionField.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setPreferredSize(new Dimension(50, 100));
        if (task != null) descriptionField.setText(task.getDescription());

        addNotes = new JButton("Add Notes");
        addNotes.addActionListener(this);
        removeNotes = new JButton("Remove Notes");
        removeNotes.addActionListener(this);

        if (currentTask == null){
            removeNotes.setEnabled(false);
        }
        else if (currentTask.getNotes().size() == 0){
            removeNotes.setEnabled(false);
        }

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Title"),
                nameField,
                new JLabel("Regularity"),
                regularity,
                new JLabel("Date"),
                dateField,
                addNotes, removeNotes,
                new JLabel("Description"),
                new JScrollPane(descriptionField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, "Task Editing Window", JOptionPane.OK_CANCEL_OPTION);
        return result;
    }
    public String getTypedName(){
        return nameField.getText();
    }
    public String getTypedDescription(){
        return descriptionField.getText();
    }
    public  String getTypedDate(){
        return dateField.getText();
    }
    public Manager.REPEATS getRepeats(){
        return repeats;
    }
    public LinkedList<Note> getToBeAdded() { return toBeAdded; }
    public LinkedList<Note> getToBeRemoved() { return toBeRemoved; }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        LinkedList<Note> list;
        DefaultListModel listModel = new DefaultListModel();

        if (source.equals(addNotes)) {

            list = manager.getNotes();

            if (currentTask != null) {
                LinkedList<Note> thisNotes = currentTask.getNotes();

                for (Note itr : list) {
                    if (!thisNotes.contains(itr) && !toBeAdded.contains(itr)) {
                        listModel.addElement(itr.getName());
                    }
                }

            }
            else {
                for (Note itr : list) {
                    if (!toBeAdded.contains(itr))
                    listModel.addElement(itr.getName());
                }
            }

            for (Note itr : toBeRemoved)  listModel.addElement(itr.getName());


            notesList.setModel(listModel);

        }

        if (source.equals(removeNotes)){

            LinkedList<Note> thisNotes;
            if (currentTask != null) {

                thisNotes = currentTask.getNotes();
                for (Note itr : thisNotes) listModel.addElement(itr.getName());

            }
            for (Note itr : toBeAdded)  listModel.addElement(itr.getName());

            notesList.setModel(listModel);

        }

        final JComponent[] inputs = new JComponent[] {
                new JScrollPane(notesList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) };

        String name;
        if (source.equals(addNotes)) name = "Attach Notes";
        else name = "Remove Notes";

        int result = JOptionPane.showConfirmDialog(null, inputs, name,
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION){

            List selected = notesList.getSelectedValuesList();
            LinkedList<String> selectedList = new LinkedList<String>(selected);
            LinkedList<Note> notes = manager.getNotes();

            if (source.equals(addNotes)) {

                if (currentTask != null){
                    for (Note itr : currentTask.getNotes())
                        toBeAdded.add(itr);
                }

                for (String itr : selectedList) {
                    for (Note itr2 : notes) {
                        if (itr.equals(itr2.getName())) {
                            toBeAdded.add(itr2);
                            break;
                        }
                    }
                }
                for (int i = 0; i < toBeAdded.size(); i++)
                    for (int j = 0; j < toBeRemoved.size(); j++){
                        if (toBeAdded.get(i).equals(toBeRemoved.get(j))){
                            toBeRemoved.remove(j);
                            toBeAdded.remove(i);
                            i--;
                            break;
                        }
                    }
                }

            if (source.equals(removeNotes)){

                for (String itr : selectedList) {
                    for (Note itr2 : notes) {
                        if (itr.equals(itr2.getName())) {
                            toBeRemoved.add(itr2);
                            break;
                        }
                    }
                }

                for (int i = 0; i < toBeRemoved.size(); i++){
                    for (int j = 0; j < toBeAdded.size(); j++){
                        if (toBeRemoved.get(i).equals(toBeAdded.get(j))){
                            toBeAdded.remove(j);
                            toBeRemoved.remove(i);
                            i--;
                            break;
                        }
                    }
                }

            }
        }
        if (toBeAdded.size() == 0){
            removeNotes.setEnabled(false);
        }
        else{
            removeNotes.setEnabled(true);
        }
    }


    private class RegularityListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int option = regularity.getSelectedIndex();

            switch(option){
                case 0:
                    repeats = Manager.REPEATS.NONE;
                    dateField.setEnabled(true);
                    break;
                case 1:
                    repeats = Manager.REPEATS.DAILY;
                    dateField.setEnabled(false);
                    break;
                case 2:
                    repeats = Manager.REPEATS.WEEKLY;
                    dateField.setEnabled(true);
                    break;
                case 3:
                    repeats = Manager.REPEATS.MONTHLY;
                    dateField.setEnabled(true);
                    break;
                case 4:
                    repeats = Manager.REPEATS.YEARLY;
                    dateField.setEnabled(true);
                    break;
            }
        }
    }

    private class HintTextField extends JTextField implements FocusListener {

        private final String hint;
        private boolean showingHint;

        public HintTextField(final String hint) {
            super(hint);
            this.hint = hint;
            this.showingHint = true;
            super.addFocusListener(this);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if(showingHint == true) {
                super.setText("");
                super.setForeground(Color.BLACK);
                showingHint = false;
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if(this.getText().isEmpty()) {
                super.setText(hint);
                super.setForeground(Color.GRAY);
                showingHint = true;
            }
        }

        @Override
        public String getText() {
            return super.getText();
        }
    }

}