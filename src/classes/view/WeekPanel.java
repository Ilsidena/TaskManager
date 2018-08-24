package classes.view;



import classes.model.Manager;
import classes.model.Task;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.*;


public class WeekPanel extends JPanel implements MouseListener {

    private SimpleDateFormat day = new SimpleDateFormat("d");
    private Date date = new Date();
    private Manager manager;
    private int width;
    private int height;
    private Task draggedTask;
    private Component lastEntered;
    private Border highlighted = BorderFactory.createLineBorder(new Color(120, 230, 250), 4);
    private Font standard = new Font("helvetica", Font.BOLD, 12);
    private Font strikeTrough;


    public WeekPanel(Manager manager){

        super();
        this.setLayout(new GridLayout(7, 8));
        this.manager = manager;
        this.addMouseListener(this);

        Map attributes = standard.getAttributes();

        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        strikeTrough = new Font(attributes);

    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void paintComponent(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);


        //Painting
        /////////////////////////////////////////////////////////
        g.setColor(new Color(250, 220, 220));
        g.fillRect(0, 0, getWidth(), getHeight() / 7);
        g.setColor(new Color(220, 250, 220));
        g.fillRect(0, getHeight() / 7, getWidth(), (getHeight() / 7) * 5);
        g.setColor(new Color(250, 220, 220));
        g.fillRect(0, getHeight() / 7 * 6, getWidth(), getHeight() / 7);
        //////////////////////////////////////////////////////////


        g.setColor(Color.black);

        width = getWidth();
        height = getHeight();

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK) + 1);


        for (int i = 0; i < 7; i++) {

            g.drawLine(0, (getHeight() / 7) * i, getWidth(), (getHeight() / 7) * i);

            g.drawString(getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)),
                    10, (getHeight() / 7) * i + 20);

            g.drawString(day.format(cal.getTime()), 10, (getHeight() / 7) * i + 40);

            cal.add(Calendar.DATE, +1);

        }

        for (int d = 0; d < 8; d++) {

            g.drawLine((getWidth() / 8) * d, 0, (getWidth() / 8) * d, getHeight());

        }


    }


    public void loadWeek(){

        this.removeAll();

        width = getParent().getWidth();
        height = getParent().getHeight();

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK) + 1);

        LinkedList<Task> taskList;


        for (int d = 0; d < 7; d++){


            for (int counter = 0; counter < 8; counter++){

                JPanel dayPanel = new JPanel();
                dayPanel.setName(MainPanel.dateToString(cal));
                dayPanel.setOpaque(false);
                dayPanel.setPreferredSize(new Dimension(width / 8, height / 7));
                dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.PAGE_AXIS));
                dayPanel.add(Box.createVerticalGlue());
                dayPanel.addMouseListener(this);

                if (counter == 0){

                    this.add(dayPanel);
                    continue;

                }



                taskList = manager.getDateTask(cal);

                if(counter <= taskList.size()) {

                    JButton button = new JButton(taskList.get(counter - 1).getName());

                    if (taskList.get(counter - 1).isDone == true){
                        button.setBackground(Color.lightGray);
                        button.setForeground(Color.darkGray);
                        button.setFont(strikeTrough);
                    }
                    else{
                        button.setBackground(new Color(100, 230, 250));
                        button.setForeground(Color.darkGray);
                        button.setFont(standard);
                    }

                    button.setMinimumSize(new Dimension(width / 7 - 70, 20));
                    button.setMaximumSize(new Dimension(width / 7 - 70, 20));
                    button.setActionCommand(MainPanel.dateToString(cal));
                    button.addMouseListener(new MenuListener());
                    button.addMouseListener(this);
                    button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                    dayPanel.add(button);

                }

                this.add(dayPanel);

            }

            cal.add(Calendar.DATE, 1);

        }


        this.revalidate();

    }


    protected static String getDayOfWeek(int day){
        switch (day){

            case 1:
                return "Sun";

            case 2:
                return "Mon";

            case 3:
                return "Tue";

            case 4:
                return "Wed";

            case 5:
                return "Thu";

            case 6:
                return "Fri";

            case 7:
                return "Sat";

            default:
                throw new IllegalArgumentException("Non-existent day of week was used");
        }
    }






    private void showMenu(MouseEvent e){
        if (e.isPopupTrigger()) {
            PopUpMenu menu = new PopUpMenu(e.getComponent());
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }



    public void mouseClicked(MouseEvent e){

        if (e.getButton() != MouseEvent.BUTTON1) return;

        MainPanel.addTask(manager, MainPanel.toDate(e.getComponent().getName()));

        loadWeek();

    }




    public void mousePressed(MouseEvent e){

        if(e.getButton() == MouseEvent.BUTTON3) return;

        Component source = e.getComponent();

        if(!(source instanceof JButton)) return;


        String name = ((JButton) source).getText();

        draggedTask = manager.findTask(name);

        LinkedList<Task> list = manager.getDoneTasks();

        for (Task itr : list){
            if (itr.getName().equals(name)){
                draggedTask = null;
                return;
            }
        }

        ((JPanel)(source.getParent())).setBorder(highlighted);

    }


    public void mouseReleased(MouseEvent e){

        Component source = lastEntered;

        if (draggedTask == null) return;

        String date;

        if(source instanceof JButton){

            date = ((JButton) source).getActionCommand();

        }

        else if (source instanceof JPanel){

            date = source.getName();

        }

        else return;

        manager.editTask(draggedTask.getName(), draggedTask.getDate(), draggedTask.isDone, draggedTask.getName(), draggedTask.getDescription(),
                draggedTask.getNotes(), MainPanel.toDate(date), manager.getRepeats(draggedTask));

        loadWeek();

        draggedTask = null;

    }

    public void mouseEntered(MouseEvent e){

        lastEntered = e.getComponent();


        if (lastEntered instanceof JPanel && draggedTask != null && !lastEntered.equals(this)){

            ((JPanel) lastEntered).setBorder(highlighted);

            int i = lastEntered.getParent().getComponentZOrder(lastEntered);
            int row = i / 8;

            while (i / 8 == row && i > -1) i--;
            i += 1;

            while (i / 8 == row){
                ((JPanel)lastEntered.getParent().getComponent(i)).setBorder(highlighted);
                i++;
            }

        }

    }

    public void mouseExited(MouseEvent e){

        if (lastEntered instanceof JPanel && draggedTask != null && !lastEntered.equals(this)){

            ((JPanel) lastEntered).setBorder(null);

            int i = lastEntered.getParent().getComponentZOrder(lastEntered);
            int row = i / 8;

            while (i / 8 == row && i > -1) i--;
            i += 1;

            while (i / 8 == row){
                ((JPanel)lastEntered.getParent().getComponent(i)).setBorder(null);
                i++;
            }

        }

    }




    private class PopUpMenu extends JPopupMenu implements ActionListener {
        private JMenuItem view;
        private JMenuItem edit;
        private JMenuItem complete;
        private JMenuItem uncomplete;
        private JMenuItem delete;
        private JButton button;

        public PopUpMenu(Component comp){

            if (comp instanceof JButton) {
                button = (JButton) comp;
            }
            String date = button.getActionCommand();
            String name = button.getText();
            LinkedList<Task> tasks = manager.getDoneTasks();
            boolean isDone = false;

            for (Task itr : tasks){
                if (itr.getName().equals(name) && MainPanel.dateToString(itr.getDate()).equals(date)){
                    isDone = true;
                    break;
                }
            }

            view = new JMenuItem("View Notes");
            view.setActionCommand("View Notes");
            view.addActionListener(this);
            this.add(view);

            edit = new JMenuItem("Edit");
            edit.setActionCommand("Edit");
            edit.addActionListener(this);
            this.add(edit);


            if (isDone == false) {
                complete = new JMenuItem("Complete");
                complete.setActionCommand("Complete");
                complete.addActionListener(this);
                this.add(complete);
            }

            else {
                uncomplete = new JMenuItem("Cancel Complete");
                uncomplete.setActionCommand("Cancel");
                uncomplete.addActionListener(this);
                this.add(uncomplete);
            }


            delete = new JMenuItem("Delete");
            delete.setActionCommand("Delete");
            delete.addActionListener(this);
            this.add(delete);



        }

        public void actionPerformed(ActionEvent e){
            String command = e.getActionCommand();
            String date = button.getActionCommand();
            String name = button.getText();
            GregorianCalendar calendar = MainPanel.toDate(date);
            boolean isDone;
            if (button.getFont().equals(strikeTrough)) isDone = true;
            else isDone = false;

            Task selectedTask = manager.findTask(name);

            switch (command){

                case "View Notes":
                    NoteView view = new NoteView(manager.findTask(name));
                    view.displayNotes();
                    break;

                case "Edit":

                    MainPanel.editTask(manager, name, selectedTask.getDate());
                    break;


                case "Complete":

                    MainPanel.completeTask(manager, name, calendar);
                    break;


                case "Delete":

                    MainPanel.deleteTask(manager, name, date, isDone);
                    break;

                case "Cancel":

                    MainPanel.setUncomplete(manager, name, calendar);
                    break;

            }


            loadWeek();
        }

    }





    private class MenuListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            showMenu(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showMenu(e);
        }

    }

}