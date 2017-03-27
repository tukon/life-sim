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
 * 02-28-17  MPK  Added events for the buttons.
 *                Added functionality to control the update process of the
 *                canvas object.
 * 03-01-17  MPK  Made the speed buttons gray out once the simulation has been
 *                ended. (Adam changed the end button functionality in the 
 *                last commit to gray out the start button because the timer
 *                can't be started again once stopped.)
 * 03-27-17  MPK  Swapped Shapes for GIFs for actors.
 *
 ******************************************************************************/
package edu.cs499;

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
    private Button SpeedButton_1;
    @FXML
    private Button SpeedButton_10;
    @FXML
    private Button SpeedButton_50;
    @FXML
    private Button SpeedButton_100;
    @FXML
    private Button GenReportButton; // Not used 
    @FXML
    private TextField FilenameInput;
    @FXML
    private TextField TimeTextField;
    @FXML
    private Canvas canvas;
    
    // Interface
    LifeSim_Interface sim_interface;
   
    // Create lists to store actors
    private List<Rock>      rock_list       = new ArrayList<>();
    private List<PlantLife> plant_life_list = new ArrayList<>();
    private List<Herbivore> herbivore_list  = new ArrayList<>();
    private List<Predator>  predator_list   = new ArrayList<>();
    
    private boolean retrieve_actor_states;
    
    private String plant_imagePath = "file:www.GIFCreator.me_sD9acf.gif";
    private String herbivore_imagePath = "file:www.GIFCreator.me_GkRicN.gif";
    private String predator_imagePath = "file:www.GIFCreator.me_5pxQjn.gif";
    private String rock_imagePath = "file:rock_finished.png";
    	        
    private Image plant_gif = new Image(plant_imagePath);
    private Image herbivore_gif = new Image(herbivore_imagePath);
    private Image predator_gif = new Image(predator_imagePath);
    private Image rock_gif = new Image(rock_imagePath);
    
    
    /**********************************************************************
     *
     * FUNCTION: initialize()
     *
     * DESCRIPTION: Initializes the controller class
     * 
     * @param url
     * @param rb
     * 
     *********************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert canvas != null;
        
        // initalize the buttons
        PauseButton.setDisable(true);
        EndButton.setDisable(true);
        SpeedButton_1.setDisable(true);
        TimeTextField.setText("0");
        
        retrieve_actor_states = false;

        // Initialize the interface
        sim_interface = new LifeSim_Interface();

        // Initialize the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Start Server
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
                        
                // if the pause or end button is engaged, there is no reason
                // to waste computation time on refreshing the canvas since
                // nothing will be changing.
                if (retrieve_actor_states == true)
                {
                    
                    TimeTextField.setText(Integer.toString(sim_interface.get_sim_time()));
                            
                    // retrieve all of the current actors.
                    rock_list       = sim_interface.get_rock_list();
                    plant_life_list = sim_interface.get_plant_life_list();
                    herbivore_list  = sim_interface.get_herbivore_list();
                    predator_list   = sim_interface.get_predator_list();

                    // clear the previous canvas state
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    //set the rocks
                    gc.setFill(Color.RED);
                    Iterator<Rock> rock_list_iterator = rock_list.iterator();
                    while (rock_list_iterator.hasNext()) 
                    {
                        Rock rock = rock_list_iterator.next();
                        //gc.fillOval(rock.get_x_pos(), rock.get_y_pos(), rock.get_diameter(), rock.get_diameter()); 
                        gc.drawImage(rock_gif, rock.get_x_pos(), rock.get_y_pos(), rock.get_diameter()*3, rock.get_diameter()*3); 
                        //System.out.println("Rock: x" + rock.get_x_pos() + " y" + rock.get_y_pos() + " w" + rock.get_diameter() + " w" + rock.get_diameter());
                    }

                    // set the plant life
                    gc.setFill(Color.GREEN);
                    Iterator<PlantLife> plant_life_list_iterator = plant_life_list.iterator();
                    while (plant_life_list_iterator.hasNext()) 
                    {
                        PlantLife plant_life = plant_life_list_iterator.next();
                        //gc.fillOval(plant_life.get_x_pos(), plant_life.get_y_pos(), plant_life.get_diameter(), plant_life.get_diameter()); 
                        gc.drawImage(plant_gif, plant_life.get_x_pos(), plant_life.get_y_pos(), plant_life.get_diameter(), plant_life.get_diameter()); 
                        //System.out.println("Plant: x" + plant_life.get_x_pos() + " y" + plant_life.get_y_pos() + " w" + plant_life.get_diameter() + " w" + plant_life.get_diameter());
                    }

                    // set the herbivores
                    gc.setFill(Color.BLUE);
                    Iterator<Herbivore> herbivore_list_iterator = herbivore_list.iterator();
                    while (herbivore_list_iterator.hasNext()) {
                        Herbivore herbivore = herbivore_list_iterator.next();
                        //gc.fillRect(herbivore.get_x_pos(), herbivore.get_y_pos(), animal_width, animal_height); 
                        // TODO find a more appropriate size for these
                        gc.drawImage(herbivore_gif, herbivore.get_x_pos(), herbivore.get_y_pos(), animal_width*6, animal_height*6); 
                        //System.out.println("herbivore: x" + herbivore.get_x_pos() + " y" + herbivore.get_y_pos() + " w" + animal_width + " w" + animal_width);
                    }

                    // set the predators
                    gc.setFill(Color.RED);
                    Iterator<Predator> predator_list_iterator = predator_list.iterator();
                    while (predator_list_iterator.hasNext()) {
                        Predator predator = predator_list_iterator.next();
                        //gc.fillRect(predator.get_x_pos(), predator.get_y_pos(), animal_width+5, animal_height+5); 
                        gc.drawImage(predator_gif, predator.get_x_pos(), predator.get_y_pos(), (animal_width+5)*2, (animal_height+5)*2); 
                        //System.out.println("predator: x" + predator.get_x_pos() + " y" + predator.get_y_pos() + " w" + animal_width + " w" + animal_width);
                    }

                    // sample object being moved around the canvas area.
                    // THIS DOES NOT FOLLOW THE SAME RULES AS THE TIMER
                    // (it will continue to "move" in the background,
                    //  regardless of the paused or ended state.)
                    //gc.fillRect(x, y, x, y);

                    // test printout
                    //System.out.println("looping");
                }

            }
        }.start();
    }    
    
    /**********************************************************************
     *
     * FUNCTION: start_button_event()
     *
     * DESCRIPTION: starts the simulation by responding to the button event
     * 
     *********************************************************************/
    @FXML
    private void start_button_event(ActionEvent event) 
    {
        sim_interface.start_sim();
        StartButton.setDisable(true);
        PauseButton.setDisable(false);
        EndButton.setDisable(false);
        retrieve_actor_states = true;
        
    } // End start_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: pause_button_event()
     *
     * DESCRIPTION: pauses the simulation by responding to the button event
     * 
     *********************************************************************/
    @FXML
    private void pause_button_event(ActionEvent event) 
    {
        sim_interface.pause_sim();
        StartButton.setDisable(false);
        PauseButton.setDisable(true);
        EndButton.setDisable(false);
        retrieve_actor_states = false;
        
    } // End pause_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: end_button_event()
     *
     * DESCRIPTION: ends the simulation by responding to the button event
     * 
     *********************************************************************/
    @FXML
    private void end_button_event(ActionEvent event) 
    {
        sim_interface.end_sim();
        StartButton.setDisable(true);
        PauseButton.setDisable(true);
        EndButton.setDisable(true);
        SpeedButton_1.setDisable(true);
        SpeedButton_10.setDisable(true);
        SpeedButton_50.setDisable(true);
        SpeedButton_100.setDisable(true);
        retrieve_actor_states = false;
        
    } // End end_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: speed_1x_button_event()
     *
     * DESCRIPTION: changes the speed of the simulation by responding to 
     *              the button event
     * 
     *********************************************************************/
    @FXML
    private void speed_1x_button_event(ActionEvent event) 
    {
        sim_interface.change_sim_speed(Timer.TimerSpeed.X1);
        SpeedButton_1.setDisable(true);
        SpeedButton_10.setDisable(false);
        SpeedButton_50.setDisable(false);
        SpeedButton_100.setDisable(false);

    } // End speed_1x_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: speed_10x_button_event()
     *
     * DESCRIPTION: changes the speed of the simulation by responding to 
     *              the button event
     * 
     *********************************************************************/
    @FXML
    private void speed_10x_button_event(ActionEvent event) 
    {
        sim_interface.change_sim_speed(Timer.TimerSpeed.X10);
        SpeedButton_1.setDisable(false);
        SpeedButton_10.setDisable(true);
        SpeedButton_50.setDisable(false);
        SpeedButton_100.setDisable(false);

    } // End speed_10x_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: speed_50x_button_event()
     *
     * DESCRIPTION: changes the speed of the simulation by responding to 
     *              the button event
     * 
     *********************************************************************/
    @FXML
    private void speed_50x_button_event(ActionEvent event) 
    {
        sim_interface.change_sim_speed(Timer.TimerSpeed.X50);
        SpeedButton_1.setDisable(false);
        SpeedButton_10.setDisable(false);
        SpeedButton_50.setDisable(true);
        SpeedButton_100.setDisable(false);

    } // End speed_50x_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: speed_100x_button_event()
     *
     * DESCRIPTION: changes the speed of the simulation by responding to 
     *              the button event
     * 
     *********************************************************************/
    @FXML
    private void speed_100x_button_event(ActionEvent event) 
    {
        sim_interface.change_sim_speed(Timer.TimerSpeed.X100);
        SpeedButton_1.setDisable(false);
        SpeedButton_10.setDisable(false);
        SpeedButton_50.setDisable(false);
        SpeedButton_100.setDisable(true);

    } // End speed_100x_button_event()
    
    /**********************************************************************
     *
     * FUNCTION: gen_report_button_event()
     *
     * DESCRIPTION: sends the event to print out a report
     * 
     *********************************************************************/
    @FXML
    private void gen_report_button_event(ActionEvent event) 
    {
        sim_interface.output_sim_statistics(FilenameInput.getText());

    } // End gen_report_button_event()
    
}
