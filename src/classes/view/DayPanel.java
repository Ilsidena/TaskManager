package classes.view;


import classes.model.Manager;
import classes.model.Task;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Map;


public class DayPanel extends JPanel implements ListSelectionListener {
    protected Manager manager;
    protected GregorianCalendar today;
    protected JList tasks;
    protected JTextArea details;
    protected JPanel buttons, listPanel;
    protected JButton add, edit, delete, complete, viewNotes;
    private Font standard = new Font("helvetica", Font.PLAIN, 24);
    private Font strikeTrough;

    public DayPanel(Manager manager, GregorianCalendar date){
        super();
        this.manager = manager;
        today = date;
        
        Map attributes = standard.getAttributes();

        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        strikeTrough = new Font(attributes);

        listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());


        buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 65, 10));
        add = new JButton("Add Task",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/add.png"));

        edit = new JButton("Edit",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/pencil.png"));

        delete = new JButton("Delete",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/delete.png"));

        complete = new JButton("Complete/Cancel Complete",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/check.png"));

        viewNotes = new JButton("View Notes",
                new ImageIcon("/media/anna/Новый том/Studing/PT/Java/TaskManager/src/resources/note_pinned.png"));

        Dimension standard = new Dimension(250, 50);
        add.setMinimumSize(standard);
        add.setMaximumSize(standard);
        add.addMouseListener(new TaskButtonListener());
        edit.setMinimumSize(standard);
        edit.setMaximumSize(standard);
        edit.addMouseListener(new TaskButtonListener());
        delete.setMinimumSize(standard);
        delete.setMaximumSize(standard);
        delete.addMouseListener(new TaskButtonListener());
        complete.setMinimumSize(standard);
        complete.setMaximumSize(standard);
        complete.addMouseListener(new TaskButtonListener());
        viewNotes.addMouseListener(new TaskButtonListener());

        buttons.add(add);
        buttons.add(edit);
        buttons.add(complete);
        buttons.add(delete);
        buttons.add(viewNotes);

        tasks = new JList();
        tasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasks.setLayoutOrientation(JList.VERTICAL);
        tasks.setFont(new Font("Helvetica", Font.PLAIN, 24));
        tasks.setVisibleRowCount(4);
        tasks.addListSelectionListener(this);
        tasks.setCellRenderer( new MyListCellRenderer());

        this.setLayout(new BorderLayout());

        this.add(listPanel, "Center");
        this.add(buttons, "South");


        details = new JTextArea();
        details.setFont(new Font("Helvetica", Font.PLAIN, 24));
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setBackground(Color.white);

        revalidate();
    }

    public void load(){
        listPanel.removeAll();
        today = ((MainPanel)this.getParent()).getDate();
        LinkedList<Task> taskList = manager.getDateTask(today);
        DefaultListModel listModel = new DefaultListModel();

        for (int i = 0; i < taskList.size(); i++){
            listModel.addElement(taskList.get(i).getName());
        }

        if (taskList.size() == 0){

            listModel.addElement("No tasks for today");
            tasks.setEnabled(false);
        }

        else  tasks.setEnabled(true);

        tasks.setModel(listModel);

        int width = this.getParent().getWidth() / 2 - 20;
        int height = this.getParent().getHeight();

        if (width == -20) width = 625;
        if (height == 0) height = 500;

        JScrollPane listScroller = new JScrollPane(tasks);
        listScroller.setPreferredSize(new Dimension(width, height));

        listPanel.add(listScroller, "West");


        JScrollPane scroller = new JScrollPane(details);
        listPanel.add(scroller, BorderLayout.EAST);
        scroller.setPreferredSize(new Dimension(width, height));
        details.setText("No task selected");

        this.repaint();
        this.revalidate();
    }




    private class MyListCellRenderer extends DefaultListCellRenderer {

        JLabel label;
        Font font;

        MyListCellRenderer(){

            font = strikeTrough;

        }

        public Component getListCellRendererComponent(JList list, // The list
                                                      Object value, // value to display
                                                      int index, // cell index
                                                      boolean isSelected, // is the cell selected
                                                      boolean cellHasFocus) // the list and the cell have the focus
        {
            label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);

            LinkedList<Task> taskList = manager.getDateTask(today);

            for (int i = 0; i < taskList.size(); i++) {
                if (label.getText().equals(taskList.get(i).getName()) && taskList.get(i).isDone == true) {
                    label.setFont(font);
                    return label;
                }
            }

            label.setFont(standard);
            return  label;
        }
    }






    public void valueChanged(ListSelectionEvent e){
        Task selectedTask = null;
        GregorianCalendar theDay = today;


        if (tasks.getSelectedValue() == null){
            details.setText("No task Selected");
            listPanel.revalidate();
            return;
        }

        String name = tasks.getSelectedValue().toString();


        LinkedList<Task> todaysTasks = manager.getDateTask(theDay);

        for (Task task : todaysTasks){
            if (task.getName().equals(name)){
                selectedTask = task;
                break;
            }
        }

        JScrollPane scroller = new JScrollPane(details);

        int width = this.getParent().getWidth() / 2 - 20;
        int height = this.getParent().getHeight();

        if (width == -20) width = 820;
        if (height == 0) height = 700;


        listPanel.add(scroller, BorderLayout.EAST);
        scroller.setPreferredSize(new Dimension(width, height));

        if (selectedTask.getDescription() == null || selectedTask.getDescription().length() < 1)
            details.setText("This task has no description");

        else {
            details.setText(selectedTask.getDescription());
        }

        listPanel.revalidate();

    }


    private class TaskButtonListener implements MouseListener {
        public void mouseClicked(MouseEvent e){

            JButton source = (JButton)e.getSource();

            try {

                if (e.getSource().equals(edit)) {


                    MainPanel.editTask(manager, tasks.getSelectedValue().toString(), today);

                }

                else if (e.getSource().equals(add)) {

                    MainPanel.addTask(manager, today);

                }
                else if (e.getSource().equals(complete)) {

                    LinkedList<Task> taskList = manager.getDateTask(today);

                    for (int i = 0; i < taskList.size(); i++) {

                        if (tasks.getSelectedValue().toString().equals(taskList.get(i).getName())
                                && taskList.get(i).isDone == true) {

                            manager.setUncomplete(tasks.getSelectedValue().toString(), today);
                            load();
                            revalidate();
                            MainPanel.saveToTxt(manager);
                            return;
                        }

                    }

                    MainPanel.completeTask(manager, tasks.getSelectedValue().toString(), today);

                }

                else if (e.getSource().equals(delete)){

                   manager.deleteTask(tasks.getSelectedValue().toString(), today);
                   MainPanel.saveToTxt(manager);
                }

                else if (e.getSource().equals(viewNotes)){
                    NoteView notes = new NoteView(manager.findTask(tasks.getSelectedValue().toString()));

                    notes.displayNotes();
                }

            }

            catch (NullPointerException exception){

                JOptionPane.showConfirmDialog(null, "No task selected",
                        "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

            }

            load();
            tasks.revalidate();

        }

        public void mouseEntered(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mousePressed(MouseEvent e){}
    }
}
