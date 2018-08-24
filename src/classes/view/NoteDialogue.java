package classes.view;

import classes.model.Note;

import javax.swing.*;
import java.awt.*;



public class NoteDialogue {
    private JTextField nameField;
    private JTextArea descriptionField;


    public NoteDialogue() { }


    public int executeDialogue(Note note){
        nameField = new JTextField();
        if (note != null) nameField.setText(note.getName());

        descriptionField = new JTextArea();
        descriptionField.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setPreferredSize(new Dimension(50, 100));
        if (note != null) descriptionField.setText(note.getDescription());

        final JComponent[] inputs = new JComponent[] {
                new JLabel("Title"),
                nameField,
                new JLabel("Description"),
                new JScrollPane(descriptionField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
        };
        int result = JOptionPane.showConfirmDialog(null, inputs,
                "Note Editing Window", JOptionPane.OK_CANCEL_OPTION);
        return result;

    }
    public String getTypedName(){
        return nameField.getText();
    }
    public String getTypedDescription(){
        return descriptionField.getText();
    }

}