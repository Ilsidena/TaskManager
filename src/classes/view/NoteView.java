package classes.view;
import classes.io.TextReaderWriter;
import classes.model.*;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class NoteView implements ListSelectionListener{

    private Task task;
    private JPanel notePanel;
    private JList notes;
    private JTextArea curDescription;


    public NoteView(Task task){
        super();
        this.task = task;

        notePanel = new JPanel();
        notePanel.setLayout(new BorderLayout());
        notePanel.setPreferredSize(new Dimension(900, 500));
        //this.add(notePanel, "Center");

        notes = new JList();
        notes.addListSelectionListener(this);
        notes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notes.setLayoutOrientation(JList.VERTICAL);
        notes.setVisibleRowCount(4);
        notes.setFont(new Font("Helvetica", Font.PLAIN, 24));

        curDescription = new JTextArea();
        curDescription.setFont(new Font("Helvetica", Font.PLAIN, 24));
        curDescription.setEditable(false);
        curDescription.setLineWrap(true);
        curDescription.setWrapStyleWord(true);

        loadNotes();

    }


    private void loadNotes(){

        notePanel.removeAll();

        DefaultListModel listModel = new DefaultListModel();
        LinkedList<Note> noteList = task.getNotes();

        for (int i = 0; i < noteList.size(); i++){

            listModel.addElement(noteList.get(i).getName());

        }

        notes.setModel(listModel);

        JScrollPane listScroller = new JScrollPane(notes);
        listScroller.setPreferredSize(new Dimension(440, 500));

        notePanel.add(listScroller, "West");
        notePanel.add(curDescription, "East");

    }


    public void displayNotes(){

        JComponent[] component = new JComponent[] {notePanel};

        JOptionPane.showConfirmDialog(null, component, "Notes for " + task.getName(),
                JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);

    }



    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (notes.getSelectedValue() == null){

            curDescription.setText("No note Selected");
            notePanel.revalidate();
            return;
        }

        String name = notes.getSelectedValue().toString();
        Note selectedNote = null;

        for (Note itr : task.getNotes()){
            if (itr.getName().equals(name)){
                selectedNote = itr;
                break;
            }
        }

        JScrollPane scrollPane = new JScrollPane(curDescription);

        notePanel.add(scrollPane, BorderLayout.EAST);
        scrollPane.setPreferredSize(new Dimension(440, 500));

        if (selectedNote != null) {

            if (selectedNote.getDescription() == null || selectedNote.getDescription().length() == 0)
                curDescription.setText("This note has no description");

            else {
                curDescription.setText(selectedNote.getDescription());
                curDescription.setBackground(Color.white);
            }

        }
        notePanel.revalidate();

    }

}

