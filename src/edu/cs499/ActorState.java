/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: ActorState
 *
 * DESCRIPTION:
 *      This object will be the state of all of the actors. It will be 
 *      responsible for the evolution of the simulation one time unit at 
 *      a time.
 *
 * REVISION HISTORY:
 * 02-28-17  MPK  New.
 *
 ******************************************************************************/
package edu.cs499;

import java.util.ArrayList;
import java.util.List;

public class ActorState {
    
    // place where datafile is kept.
    public static String DATAFILE = new String(System.getProperty("user.dir") + "/LifeSimulation01.xml");
    
    // Create lists to store actors
    private List<Rock>      rock_list       = new ArrayList<>();
    private List<PlantLife> plant_life_list = new ArrayList<>();
    private List<Herbivore> herbivore_list  = new ArrayList<>();
    private List<Predator>  predator_list   = new ArrayList<>();
    
    // initialize variables needed for storing
    private int iPlantCount    = 0;
    private int iGrazerCount   = 0; 
    private int iPredatorCount = 0; 
    private int iObstacleCount = 0;
        
    // World global info
    private double w_grid_width  = 0.0;
    private double w_grid_height = 0.0;

    // Rock global info
    // None.

    // Plant Life global info
    private double pl_growth_rate            = 0.0;
    private double pl_seed_viability         = 0.0;
    private int    pl_max_size               = 0;
    private int    pl_max_seed_cast_distance = 0;
    private int    pl_max_seed_number        = 0;

    // Herbivore global info
    private double h_maintain_speed_time     = 0.0;
    private double h_max_speed               = 0.0;
    private int    h_energy_input_rate       = 0;
    private int    h_energy_output_rate      = 0;
    private int    h_energy_to_reproduce     = 0;

    // Predator global info
    private double p_max_speed_hod          = 0.0;
    private double p_max_speed_hed          = 0.0;
    private double p_max_speed_hor          = 0.0;
    private double p_maintain_speed_time    = 0.0;
    private double p_gestation_period       = 0.0;
    private int    p_energy_output_rate     = 0;
    private int    p_energy_to_reproduce    = 0;
    private int    p_max_offspring          = 0;
    private int    p_offspring_energy_level = 0;
    
    /**********************************************************************
     *
     * FUNCTION: ActorState()
     *
     * DESCRIPTION: constructor for ActorState
     * 
     *********************************************************************/
    public ActorState()
    {
        // constructor 
        ActorState_create();
        
    } // End ActorState()
        
    /**********************************************************************
     *
     * FUNCTION: ActorState_create()
     *
     * DESCRIPTION: initializes the ActorState by reading the data file
     * 
     *********************************************************************/
    private void ActorState_create()
    {
        
        // get the singleton and initalize the data parser
        LifeSimDataParser lsdp = LifeSimDataParser.getInstance();
        lsdp.initDataParser(DATAFILE);
        
        /****************************************************************
         * Retrieve World Data
         ***************************************************************/ 
        
        // World info functions
        w_grid_width  = lsdp.getWorldWidth();
        w_grid_height = lsdp.getWorldHeight();
        
        /****************************************************************
         * Retrieve Rocks
         ***************************************************************/ 
        
        // Rock info functions
        iObstacleCount = lsdp.getObstacleCount(); // Number of obstacles
        
        // loop through each obstacle (based on count retrived from data)
        for(int i=0; i< iObstacleCount; i++)
        {
            // if there is still data to be processed, get it
            if(lsdp.getObstacleData())
            {
                // create a new rock object with the current statistics
                Rock rock = new Rock(lsdp.ObstacleX, lsdp.ObstacleY, lsdp.ObstacleDiameter, lsdp.ObstacleHeight);
                // add it to the list of rocks.
                rock_list.add(rock);
            }
        }
        
        /****************************************************************
         * Retrieve Plant Life
         ***************************************************************/ 
                
        // Plant info functions
        iPlantCount               = lsdp.getInitialPlantCount();   // Inital plant count
        pl_growth_rate            = lsdp.getPlantGrowthRate();     // Plant growth rate
        pl_max_size               = lsdp.getMaxPlantSize();        // Max plant size
        pl_max_seed_cast_distance = lsdp.getMaxSeedCastDistance(); // Max seed casting distance
        pl_max_seed_number        = lsdp.getMaxSeedNumber();       // max seeds cast
        pl_seed_viability         = lsdp.getSeedViability();       // seed viability ratio

        for(int i=0; i< iPlantCount; i++)
        {
            if(lsdp.getPlantData())
            {
                // create a new plant_life object with the current statistics
                PlantLife plant_life = new PlantLife(lsdp.PlantX, lsdp.PlantY, lsdp.PlantDiameter);
                // add it to the list of plant life.
                plant_life_list.add(plant_life);
            }
        }

        /****************************************************************
         * Retrieve Herbivores
         ***************************************************************/ 

        // Grazer info functions
        iGrazerCount          = lsdp.getInitialGrazerCount();      // Inital Grazer count
        h_energy_input_rate   = lsdp.getGrazerEnergyInputRate();   // Energy input per minute when grazing
        h_energy_output_rate  = lsdp.getGrazerEnergyOutputRate();  // Energy output when moving each 5 DU
        h_energy_to_reproduce = lsdp.getGrazerEnergyToReproduce(); // Energy level needed to reproduce
        h_maintain_speed_time = lsdp.getGrazerMaintainSpeedTime(); // Minutes of simulation to maintain max speed
        h_max_speed           = lsdp.getGrazerMaxSpeed();	   // Max speed in DU per minute

        for(int i=0; i< iGrazerCount; i++)
        {
            if(lsdp.getGrazerData())
            {
                // create a new herbivore object with the current statistics
                Herbivore herbivore = new Herbivore(lsdp.GrazerX, lsdp.GrazerY, lsdp.GrazerEnergy);
                // add it to the list of herbivores.
                herbivore_list.add(herbivore);
            }
        }
                        
        /****************************************************************
         * Retrieve Predators
         ***************************************************************/ 
        
        // Predator info functions
        iPredatorCount           = lsdp.getInitialPredatorCount();         // Inital Predator Count
        p_max_speed_hod          = lsdp.getPredatorMaxSpeedHOD();	   // Get max speed for Homozygous Dominant FF
        p_max_speed_hed          = lsdp.getPredatorMaxSpeedHED();	   // Get max speed for Heterozygous Dominant Ff
        p_max_speed_hor          = lsdp.getPredatorMaxSpeedHOR();	   // Get max speed for Homozygous Recessive ff
        p_energy_output_rate     = lsdp.getPredatorEnergyOutputRate();	   // Energy output when moving each 5 DU
        p_energy_to_reproduce    = lsdp.getPredatorEnergyToReproduce();	   // Energy level needed to reproduce
        p_maintain_speed_time    = lsdp.getPredatorMaintainSpeedTime();	   // Minutes of simulation to maintain max speed
        p_max_offspring          = lsdp.getPredatorMaxOffspring();         // Maximum number of offspring when reproducing
        p_gestation_period       = lsdp.getPredatorGestationPeriod();	   // Gestation period in simulation days 
        p_offspring_energy_level = lsdp.getPredatorOffspringEnergyLevel(); // Energy level of offspring at birth

        for(int i=0; i< iPredatorCount; i++)
        {
            if(lsdp.getPredatorData())
            {
                // create a new predator object with the current statistics
                Predator predator = new Predator(lsdp.PredatorX, lsdp.PredatorY, lsdp.PredatorEnergy, lsdp.PredatorGenotype);
                // add it to the list of predators.
                predator_list.add(predator);
            }
        }
        
    } // End ActorState_create()
    
      /**********************************************************************
     *
     * FUNCTION: get_rock_list()
     *
     * DESCRIPTION: get the current list of rocks
     *
     * @return
     * 
     *********************************************************************/
    public List<Rock> get_rock_list_state() 
    {
        return rock_list;

    } // End get_rock_list()
    
    /**********************************************************************
     *
     * FUNCTION: get_plant_life_list()
     *
     * DESCRIPTION: get the current list of plant life
     *
     * @return
     * 
     *********************************************************************/
    public List<PlantLife> get_plant_life_list_state() 
    {
        return plant_life_list;

    } // End get_plant_life_list()
    
    /**********************************************************************
     *
     * FUNCTION: get_herbivore_list()
     *
     * DESCRIPTION: get the current list of herbivore
     *
     * @return
     * 
     *********************************************************************/
    public List<Herbivore> get_herbivore_list_state() 
    {
        return herbivore_list;

    } // End get_herbivore_list()
    
    /**********************************************************************
     *
     * FUNCTION: get_predator_list()
     *
     * DESCRIPTION: get the current list of predators
     *
     * @return
     * 
     *********************************************************************/
    public List<Predator> get_predator_list_state() 
    {
        return predator_list;

    } // End get_predator_list()
    
    /**********************************************************************
     *
     * FUNCTION: ActorState_evolve()
     *
     * DESCRIPTION: evolves the sumulation one unit of time.
     *
     *********************************************************************/
    public void ActorState_evolve()
    {
        // TODO evolve the simulation one unit of time
        System.out.println("ActorState_evolve() called");

    } // End ActorState_evolve()
    
} // End ActorState Class
