/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: LifeSim_Interface
 *
 * DESCRIPTION:
 *      This object provides the interface for initializing all modules. It 
 *      starts the GUI in a separate thread so the interface can run 
 *      concurrently.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 *
 ******************************************************************************/
package edu.cs499;

import java.util.ArrayList;
import java.util.List;

public class LifeSim_Interface {
    public static String DATAFILE = new String(System.getProperty("user.dir") + "/LifeSimulation01.xml");

    /**********************************************************************
     *
     * FUNCTION: LifeSim_Interface()
     *
     * DESCRIPTION: constructor for LifeSim_Interface
     * 
     *********************************************************************/
    public LifeSim_Interface()
    {
        // constructor 
        
    } // End LifeSim_Interface()
    
    /**********************************************************************
     *
     * FUNCTION: main()
     *
     * DESCRIPTION: starting point for program. initialize everything here.
     *
     * @param args
     * 
     *********************************************************************/
    public static void main(String[] args)
    {
        // initialize variables needed for storing
	int iVal = 0;
        int iPlantCount = 0, iGrazerCount = 0, iPredatorCount = 0, iObstacleCount = 0;
        double dVal = 0.0;
                
        // Create lists to store actors
        List<Rock>      rock_list       = new ArrayList<>();
        List<PlantLife> plant_life_list = new ArrayList<>();
        List<Herbivore> herbivore_list  = new ArrayList<>();
        List<Predator>  predator_list   = new ArrayList<>();
        
        // get the singleton and initalize the data parser
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        lsdp.initDataParser(DATAFILE);
        
        /****************************************************************
         * start the GUI Thread
         ***************************************************************/ 
        Start_GUI LifeSim_GUI = new Start_GUI("GUI_Thread");
        LifeSim_GUI.start();
      
        // THIS WORKS, leaving here for reference.
        // run the data parser
        //LifeSimDataParserMain lfdp = new LifeSimDataParserMain();
        
        /****************************************************************
         * Retrieve Rocks
         ***************************************************************/ 
        iVal = lsdp.getObstacleCount(); // Number of obstacles
        
        // error checking
        System.out.println("Obstacle count = " + iVal);
        if(iVal == 15)
                System.out.println("\t Correct.\n");
        else
                System.out.println("\t Incorrect.\n");
        iObstacleCount = iVal;

        // Global variables for value return*
        /*
        public int ObstacleX;
        public int ObstacleY;
        public int ObstacleDiameter;
        public int ObstacleHeight;
        */
        
        // loop through each obstacle (based on count retrived from data)
        for(int i=0; i< iObstacleCount; i++)
        {
            // if there is still data to be processed, get it
            if(lsdp.getObstacleData())
            {
                // print out info for current obstacle
                System.out.println("Obstacle " + i + " (" + lsdp.ObstacleX + ", " + lsdp.ObstacleY + ") diameter = " + 
                                    lsdp.ObstacleDiameter + ", height = " + lsdp.ObstacleHeight);
                
                // create a new rock object with the current statistics
                Rock rock = new Rock(lsdp.ObstacleX, lsdp.ObstacleY, lsdp.ObstacleDiameter, lsdp.ObstacleHeight);
                // add it to the list of rocks.
                rock_list.add(rock);
            }
            else
            {
                    System.out.println("Failed to read data for obstacle " + i);
            }
        }
        
        /****************************************************************
         * Retrieve Plant Life
         ***************************************************************/ 
        // TODO
                
        /****************************************************************
         * Retrieve Herbivores
         ***************************************************************/ 
        // TODO
                        
        /****************************************************************
         * Retrieve Predators
         ***************************************************************/ 
        // TODO
        
    } // end main()

    /**********************************************************************
     *
     * CLASS: Start_GUI()
     *
     * DESCRIPTION: nested class that implements Runnable for creating a
     *              thread that starts the GUI
     *
     * @param args
     * 
     *********************************************************************/
    static class Start_GUI implements Runnable {
        private Thread t;
        private String threadName;

        /**********************************************************************
         *
         * FUNCTION: Start_GUI()
         *
         * DESCRIPTION: constructor for local Start_GUI class
         *
         * @param name
         * 
         *********************************************************************/
        Start_GUI(String name) 
        {
            threadName = name;
            
        } // End Start_GUI

        /**********************************************************************
         *
         * FUNCTION: run()
         *
         * DESCRIPTION: constructor for local Start_GUI class
         * 
         *********************************************************************/
        @Override
        public void run() 
        {
            // start the GUI
            LifeSim LifeSim_GUI = new LifeSim();
            LifeSim_GUI.LifeSim_create();

        } // End run()

        /**********************************************************************
         *
         * FUNCTION: start()
         *
         * DESCRIPTION: starts the thread for the GUI
         * 
         *********************************************************************/
        public void start() {
            if (t == null) 
            {
                t = new Thread(this, threadName);
                t.start ();
            }
        } // End start()

    } // End Start_GUI class
    
} // End LifeSim_Interface class