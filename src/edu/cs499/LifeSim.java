/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: LifeSim
 *
 * DESCRIPTION:
 *      This object is responsible for the GUI for the simulation
 *
 * REVISION HISTORY:
 * 01-23-17  AA   New.
 * 01-26-17  MPK  Added constructor so that this object can be initialized in 
 *                the LifeSim_Interface class.
 *                Also added a LifeSim_Create function to call so that it is
 *                clear that the GUI is being created elsewhere.
 * 02-27-17  MPK  Added the FXML based GUI, so the code was changed to start it.
 *
 ******************************************************************************/
package edu.cs499;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The main application class.
 * @author adam
 */
public class LifeSim extends Application {
    
       /**********************************************************************
        *
        * FUNCTION: LifeSim()
        *
        * DESCRIPTION: constructor for the LifeSim class
        * 
        *********************************************************************/
        public LifeSim() 
        {
            // constructor
            
        } // End LifeSim()
	
       /**********************************************************************
        *
        * FUNCTION: start()
        *
        * DESCRIPTION: overrides starting function for the JavaFX GUI class.
        * 
        * @param stage
        * @throws java.lang.Exception
        * 
        *********************************************************************/
	@Override
	public void start(Stage stage) throws Exception {
            
            // retrieve the JavaFX GUI from the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("GUI_FXML.fxml"));
        
            Scene scene = new Scene(root);
        
            stage.setTitle( "Life Simulation" );
            stage.setScene(scene);
            stage.show();
            
	} // End start()

       /**********************************************************************
        *
        * FUNCTION: main()
        *
        * DESCRIPTION: default starting point for this program if you just
        *              hit run instead of "right-click run"ing the program
        *              inside the LifeSim_Interface file.
        * 
        * @param args
        * 
        *********************************************************************/
	public static void main(String[] args) {
            launch(args);
                
	} // End main()
        
       /**********************************************************************
        *
        * FUNCTION: LifeSim_create()
        *
        * DESCRIPTION: function to call to create the GUI once a new object
        *              is created.
        * 
        *********************************************************************/
        public void LifeSim_create() {
            // created to pass into launch
            String[] args = null; 
            
            // launch GUI
            launch(args);
            
        } // End LifeSim_create()
	
} // End LifeSim class
