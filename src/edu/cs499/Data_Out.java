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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    // Create lists to store actors
    private List<Rock>      rock_list       = new ArrayList<>();
    private List<PlantLife> plant_life_list = new ArrayList<>();
    private List<Herbivore> herbivore_list  = new ArrayList<>();
    private List<Predator>  predator_list   = new ArrayList<>();
    
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
    private void retrieveData(){
        //rock_list = sim_interface.get_rock_list();
        //plant_life_list = sim_interface.get_plant_life_list();
        //herbivore_list  = sim_interface.get_herbivore_list();
        //predator_list   = sim_interface.get_predator_list();
    }
    
    private void writeData(String name, PrintWriter out){
        out.print(rock_list);
        out.print(plant_life_list);
        out.print(herbivore_list);
        out.print(predator_list);
    }
    
    public void populate(List<Rock> r_list, List<PlantLife> pl_list, List<Herbivore> h_list, List<Predator> p_list){
        rock_list = r_list;
        plant_life_list = pl_list;
        herbivore_list = h_list;
        predator_list = p_list;
    }
    
    public void print_info(String filename)
    {
        // find the home directory of the current user
        String home = System.getProperty("user.home");
        // Add the correct filesystem path
        if ("Linux".equals(System.getProperty("os.name")))
            home = home + "/";
        else
            home = home + "\\";
        
        // debug statements
        System.out.println("This is the directory the file is going to: " + home);
        System.out.println("This is the operating system: " + System.getProperty("os.name"));
        
        // write the data to the file 
        // this overwrites a previous file if you don't change the name
        try{
            try (PrintWriter writer = new PrintWriter(home + filename + ".txt", "UTF-8")) {
                
                // write the rocks to the file
                Iterator<Rock> rock_list_iterator = rock_list.iterator();
                while (rock_list_iterator.hasNext()) 
                {
                    Rock rock = rock_list_iterator.next();
                    writer.println("Rock - x_pos:" + Integer.toString(rock.get_x_pos()) + "  \ty_pos:"+ Integer.toString(rock.get_y_pos()) + "  \tdiameter:"+ Integer.toString(rock.get_diameter()) + "\theight:" +Integer.toString(rock.get_height()));
                }
/*
                // TODO
                // write the plant life to the file
                Iterator<PlantLife> plant_life_list_iterator = plant_life_list.iterator();
                while (plant_life_list_iterator.hasNext()) 
                {
                    PlantLife plant_life = plant_life_list_iterator.next();
                    //gc.fillOval(plant_life.get_x_pos(), plant_life.get_y_pos(), plant_life.get_diameter(), plant_life.get_diameter()); 
                    gc.drawImage(plant_gif, plant_life.get_x_pos(), plant_life.get_y_pos(), plant_life.get_diameter(), plant_life.get_diameter()); 
                    //System.out.println("Plant: x" + plant_life.get_x_pos() + " y" + plant_life.get_y_pos() + " w" + plant_life.get_diameter() + " w" + plant_life.get_diameter());
                }
                
                // TODO
                // write the herbivore to the file
                Iterator<Herbivore> herbivore_list_iterator = herbivore_list.iterator();
                while (herbivore_list_iterator.hasNext()) {
                    Herbivore herbivore = herbivore_list_iterator.next();
                    //gc.fillRect(herbivore.get_x_pos(), herbivore.get_y_pos(), animal_width, animal_height); 
                    // TODO find a more appropriate size for these
                    gc.drawImage(herbivore_gif, herbivore.get_x_pos(), herbivore.get_y_pos(), animal_width*6, animal_height*6); 
                    //System.out.println("herbivore: x" + herbivore.get_x_pos() + " y" + herbivore.get_y_pos() + " w" + animal_width + " w" + animal_width);
                }
                
                // TODO
                // write the the predators to the file
                Iterator<Predator> predator_list_iterator = predator_list.iterator();
                while (predator_list_iterator.hasNext()) {
                    Predator predator = predator_list_iterator.next();
                    //gc.fillRect(predator.get_x_pos(), predator.get_y_pos(), animal_width+5, animal_height+5); 
                    gc.drawImage(predator_gif, predator.get_x_pos(), predator.get_y_pos(), (animal_width+5)*2, (animal_height+5)*2); 
                    //System.out.println("predator: x" + predator.get_x_pos() + " y" + predator.get_y_pos() + " w" + animal_width + " w" + animal_width);
                }
*/
            }
        } catch (IOException e) {
           // do something
        }
        
    }
    
}
