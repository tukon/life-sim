/* 
 * Project: Life Sim for CS 499
 * Team 2: Adam, Alicia, Matt, Paul
 */
package edu.cs499;

import java.io.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
/**
 *
 * @author paulm
 */
public class Data_Out {
 //Class for reading in file name and creating file
    public static PrintWriter openWriter(){
        try
        {
            
        
            File file = new File("simulator.txt");
            PrintWriter out =
                new PrintWriter(
                    new BufferedWriter(
                         new FileWriter(file, false) ));//setting to false overwrites previous file
            return out;
        }
        catch (IOException e)
        {
            System.out.println("I/O Error");
            System.exit(0);
        }
        return null;
}
    private static void retrieveData(){
        rock_list = sim_interface.get_rock_list();
        plant_life_list = sim_interface.get_plant_life_list();
        herbivore_list  = sim_interface.get_herbivore_list();
        predator_list   = sim_interface.get_predator_list();
    }
    
    private static void writeData(String name, PrintWriter out){
        out.print(rock_list);
        out.print(plant_life_list);
        out.print(herbivore_list);
        out.print(predator_list);
    }
   
    
}
