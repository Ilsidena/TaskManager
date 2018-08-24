package classes.view;

import classes.model.Manager;
import classes.model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static classes.view.MainPanel.dateToString;

public class CompletedPanel extends JPanel {

    private JList completedList;
    private Manager manager;

    public CompletedPanel(Manager manager){

        super();
        this.manager = manager;

        completedList = new JList();
        completedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completedList.setLayoutOrientation(JList.VERTICAL);
        completedList.setFont(new Font("Helvetica", Font.PLAIN, 24));


    }

    public void loadCompleted(){

        this.removeAll();

        DefaultListModel listModel = new DefaultListModel();
        LinkedList<Task> tasks = manager.getDoneTasks();


        Object[] list = new Object[tasks.size()];

        for (int i = 0; i < tasks.size(); i++){

            listModel.addElement( dateToString(tasks.get(i).getDate()) + " " + tasks.get(i).getName());

        }

        completedList.setModel(listModel);

        int width = this.getParent().getWidth() - 80;
        int height = this.getParent().getHeight() - 80;



        JScrollPane scrollPane = new JScrollPane(completedList);
        scrollPane.setPreferredSize(new Dimension(width, height));
        this.add(scrollPane);

        revalidate();
    }
}
