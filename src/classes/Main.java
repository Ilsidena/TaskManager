package classes;

import classes.io.TextReaderWriter;
import classes.model.*;
import classes.view.*;

public class Main {
    public static void main(String args[]) {
        Manager manager = new Manager();
        try{
            manager = TextReaderWriter.read("state.txt");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        BaseWindow bw = new BaseWindow(manager);
        
       
    }
}