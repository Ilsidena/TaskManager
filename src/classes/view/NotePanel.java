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

public class NotePanel extends JPanel {

    private Manager manager;
    private JPanel toolbar;
    private JPanel notePanel;
    private JList notes;
    private JTextArea curDescription;


    public NotePanel(Manager manager){
        super();
        this.manager = manager;
        this.setLayout(new BorderLayout());

        toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 80, 5));

        JButton addNote = new JButton("Add Note",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/add.png"));

        JButton editNote = new JButton("Edit",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/pencil.png"));

        JButton deleteNote = new JButton("Delete",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/delete.png"));

        Dimension standard = new Dimension(100, 30);
        addNote.setMinimumSize(standard);
        addNote.setMaximumSize(standard);
        addNote.addMouseListener(new NoteButtonListener());

        editNote.addMouseListener(new NoteButtonListener());
        editNote.setMinimumSize(standard);
        editNote.setMaximumSize(standard);

        deleteNote.addMouseListener(new NoteButtonListener());
        deleteNote.setMinimumSize(standard);
        deleteNote.setMaximumSize(standard);

        toolbar.add(addNote);
        toolbar.add(editNote);
        toolbar.add(deleteNote);
        this.add(toolbar, "South");

        notePanel = new JPanel();
        notePanel.setLayout(new BorderLayout());
        this.add(notePanel, "Center");

        notes = new JList();
        notes.addListSelectionListener(new ListListener());
        notes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notes.setLayoutOrientation(JList.VERTICAL);
        notes.setVisibleRowCount(4);
        notes.setFont(new Font("Helvetica", Font.PLAIN, 24));

        curDescription = new JTextArea();
        curDescription.setFont(new Font("Helvetica", Font.PLAIN, 24));
        curDescription.setEditable(false);
        curDescription.setLineWrap(true);
        curDescription.setWrapStyleWord(true);

    }


    public void loadNotes(){

        notePanel.removeAll();

        DefaultListModel listModel = new DefaultListModel();
        LinkedList<Note> noteList = manager.getNotes();

        for (int i = 0; i < noteList.size(); i++){

            listModel.addElement(noteList.get(i).getName());

        }

        notes.setModel(listModel);


        int width = this.getParent().getWidth() / 2 - 20;
        int height = this.getParent().getHeight();

        JScrollPane listScroller = new JScrollPane(notes);
        listScroller.setPreferredSize(new Dimension(width, height));

        notePanel.add(listScroller, "West");

        revalidate();


    }



    private class NoteButtonListener implements MouseListener{
        public void mouseClicked(MouseEvent e) throws NullPointerException{
            JButton source = (JButton)e.getSource();
            String sourceText = source.getText();


            if(sourceText.equals("Edit")){
                try {
                    int index = notes.getSelectedIndex();
                    String name = notes.getSelectedValue().toString();


                    Note selectedNote = manager.findNote(name);

                    NoteDialogue input = new NoteDialogue();
                    int result = input.executeDialogue(selectedNote);

                    if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return;

                    if (input.getTypedName().length() > 0) {
                        selectedNote.setName(input.getTypedName());
                    }
                    else throw new IllegalArgumentException("A note must have a name");

                    if (input.getTypedDescription().length() > 0) {
                        selectedNote.setDescription(input.getTypedDescription());
                    }
                    else selectedNote.setDescription(null);

                    notes.setSelectedIndex(index);

                }

                catch (NullPointerException exception){
                    JOptionPane.showConfirmDialog(null, "No note selected",
                            "Warning", JOptionPane.PLAIN_MESSAGE);
                }

                catch (IllegalArgumentException e1){
                    JOptionPane.showConfirmDialog(null, e1.getMessage(), "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            }

            else if(sourceText.equals("Add Note")){

                try {

                    NoteDialogue input = new NoteDialogue();
                    int result = input.executeDialogue(null);

                    if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return;

                    if (input.getTypedName().length() > 0) {

                        manager.addNote(input.getTypedName(), input.getTypedDescription());

                    } else throw new IllegalArgumentException("A note must have a name");
                }

                catch (IllegalArgumentException e1){
                    JOptionPane.showConfirmDialog(null, e1.getMessage(), "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                }

            }


            else if (sourceText.equals("Delete")) {
                try {
                    String name = notes.getSelectedValue().toString();


                    manager.deleteNote(name);
                }

                catch (NullPointerException exception){
                    JOptionPane.showConfirmDialog(null, "No note selected",
                            "Warning", JOptionPane.PLAIN_MESSAGE);
                }
            }
                loadNotes();
                notes.getTopLevelAncestor().repaint();

            try{
                TextReaderWriter.write(manager, "state.txt");
            }
            catch (Exception writerException) {
                System.out.println(writerException.getMessage());
            }
        }

        public void mouseEntered(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mousePressed(MouseEvent e){}

    }



    private class ListListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (notes.getSelectedValue() == null){

                curDescription.setText("No note Selected");
                revalidate();
                return;
            }

            String name = notes.getSelectedValue().toString();

            Note selectedNote = manager.findNote(name);

            JScrollPane scrollPane = new JScrollPane(curDescription);

            int width = getParent().getWidth() / 2 - 20;
            int height = getParent().getHeight();

            notePanel.add(scrollPane, BorderLayout.EAST);
            scrollPane.setPreferredSize(new Dimension(width, height));

            if (selectedNote.getDescription() == null || selectedNote.getDescription().length() == 0)
                curDescription.setText("This note has no description");

            else {
                curDescription.setText(selectedNote.getDescription());
                curDescription.setBackground(Color.white);
            }
            revalidate();


        }
    }

}
