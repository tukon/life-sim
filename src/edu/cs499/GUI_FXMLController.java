/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: GUI_FXMLController
 *
 * DESCRIPTION:
 *      This object contains all of the control code for the JavaFX GUI.
 *
 * REVISION HISTORY:
 * 02-27-17  MPK  New.
 *
 ******************************************************************************/
package edu.cs499;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GUI_FXMLController implements Initializable {

    // The GUI objects that must be manually inserted here
    // so that the code can use them.
    @FXML
    private Button StartButton;
    @FXML
    private Button PauseButton;
    @FXML
    private Button EndButton;
    @FXML
    private Button SpeedComboBox_1;
    @FXML
    private Button SpeedComboBox_10;
    @FXML
    private Button SpeedComboBox_50;
    @FXML
    private Button SpeedComboBox_100;
    @FXML
    private Canvas canvas;
   
    // Create lists to store actors
    private List<Rock>      rock_list       = new ArrayList<>();
    private List<PlantLife> plant_life_list = new ArrayList<>();
    private List<Herbivore> herbivore_list  = new ArrayList<>();
    private List<Predator>  predator_list   = new ArrayList<>();
    
    /**********************************************************************
     *
     * FUNCTION: initialize()
     *
     * DESCRIPTION: Initializes the controller class
     * 
     *********************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert canvas != null;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        start_server( gc );
    }
    
    /**********************************************************************
     *
     * FUNCTION: start_server()
     *
     * DESCRIPTION: starts the server that updates the GUI,
     * 
     *********************************************************************/
    private void start_server(GraphicsContext gc)
    {
        
        // retrieve all of the inital actors.
        LifeSim_Interface sim_interface = new LifeSim_Interface();
        rock_list       = sim_interface.get_rock_list();
        plant_life_list = sim_interface.get_plant_life_list();
        herbivore_list  = sim_interface.get_herbivore_list();
        predator_list   = sim_interface.get_predator_list();
        
        final long startNanoTime = System.nanoTime();
        final int animal_height = 10;
        final int animal_width = 10;

        // animation timer for the GUI
        new AnimationTimer()
        {
            @Override
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0; 

                double x = 150 + 100 * Math.cos(t);
                double y = 150 + 100 * Math.sin(t);
                
                // TODO 
                // Put checks here, so that everytime it loops it looks for 
                // a button event. If a button event has happend, DO NOT
                // clear the canvas object and stop the evolution of the
                // simulation if Pause has been pressed. Any other respones
                // would be here as well. 
                // Use a switch statement to check for boolean values being set?

                // clear the previous canvas state
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                
                //set the rocks
                gc.setFill(Color.RED);
                Iterator<Rock> rock_list_iterator = rock_list.iterator();
		while (rock_list_iterator.hasNext()) 
                {
                    Rock rock = rock_list_iterator.next();
                    gc.fillOval(rock.get_x_pos(), rock.get_y_pos(), rock.get_diameter(), rock.get_diameter()); 
                    System.out.println("Rock: x" + rock.get_x_pos() + " y" + rock.get_y_pos() + " w" + rock.get_diameter() + " w" + rock.get_diameter());
		}
                
                // set the plant life
                gc.setFill(Color.GREEN);
                Iterator<PlantLife> plant_life_list_iterator = plant_life_list.iterator();
		while (plant_life_list_iterator.hasNext()) 
                {
                    PlantLife plant_life = plant_life_list_iterator.next();
                    gc.fillOval(plant_life.get_x_pos(), plant_life.get_y_pos(), plant_life.get_diameter(), plant_life.get_diameter()); 
                    System.out.println("Plant: x" + plant_life.get_x_pos() + " y" + plant_life.get_y_pos() + " w" + plant_life.get_diameter() + " w" + plant_life.get_diameter());
		}
                
                // set the herbivores
                gc.setFill(Color.BLUE);
                Iterator<Herbivore> herbivore_list_iterator = herbivore_list.iterator();
		while (herbivore_list_iterator.hasNext()) {
                    Herbivore herbivore = herbivore_list_iterator.next();
                    gc.fillRect(herbivore.get_x_pos(), herbivore.get_y_pos(), animal_width, animal_height); 
                    System.out.println("herbivore: x" + herbivore.get_x_pos() + " y" + herbivore.get_y_pos() + " w" + animal_width + " w" + animal_width);
		}
                
                // set the predators
                gc.setFill(Color.RED);
                Iterator<Predator> predator_list_iterator = predator_list.iterator();
		while (predator_list_iterator.hasNext()) {
                    Predator predator = predator_list_iterator.next();
                    gc.fillRect(predator.get_x_pos(), predator.get_y_pos(), animal_width+5, animal_height+5); 
                    System.out.println("predator: x" + predator.get_x_pos() + " y" + predator.get_y_pos() + " w" + animal_width + " w" + animal_width);
		}
                
                // sample object being moved around the canvas area.
                gc.fillRect(x, y, x, y);

                // test printout
                System.out.println("looping");

            }
        }.start();
    }    
    
}
