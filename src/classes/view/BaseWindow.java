package classes.view;
import classes.io.CSVWriter;
import classes.model.Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class BaseWindow extends JFrame {
    private Manager manager;

    public BaseWindow(Manager manager){
        this.manager = manager;
        this.setSize(1700, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Task Manager");
        

        Container c = getContentPane();
        this.setVisible(true);

        MainPanel tasks = new MainPanel(manager);


        c.add(tasks);

        revalidate();

    }

    public BaseWindow(){

        this(null);

    }
}