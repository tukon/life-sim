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
 * 03-01-17  MPK  Added critical sections so that there is no corrupt data
 *                caused by the GUI loop trying to read the actors states
 *                while the ActorState is writing back to the state after
 *                running all of the evolve functionality.
 * 03-02-17  MPK  Added functionality to evolve each of the individual, current
 *                actors in the list.
 *                Also started adding code for the evolution for the plant life.
 *
 ******************************************************************************/
package edu.cs499;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private final Semaphore semaphore;
    
    /**********************************************************************
     *
     * FUNCTION: ActorState()
     *
     * DESCRIPTION: constructor for ActorState
     * 
     *********************************************************************/
    public ActorState()
    {
        semaphore = new Semaphore(1);
        
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
        
        enter_critical_section();
        
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
        iPlantCount               = lsdp.getInitialPlantCount();   // Initial plant count
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
        
        exit_critical_section();
        
    } // End ActorState_create()
        
    /**********************************************************************
     *
     * FUNCTION: enter_critical_section()
     *
     * DESCRIPTION: enters the critical section where the GUI requests
     *              the current state or when the state is being written to.
     *
     *********************************************************************/
    private void enter_critical_section()
    {
        try {
            semaphore.acquire(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ActorState.class.getName()).log(Level.SEVERE, null, ex);
        }

    } // End enter_critical_section()
    
    /**********************************************************************
     *
     * FUNCTION: exit_critical_section()
     *
     * DESCRIPTION: exits the critical section where the GUI requests
     *              the current state or when the state is being written to.
     * 
     *********************************************************************/
    private void exit_critical_section()
    {
        semaphore.release(1);

    } // End exit_critical_section()
    
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
        List<Rock> rocks = new ArrayList<>();
        
        enter_critical_section();
        rocks = rock_list;
        exit_critical_section();
        
        return rocks;

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
        List<PlantLife> plant_life = new ArrayList<>();
        
        enter_critical_section();
        plant_life = plant_life_list;
        exit_critical_section();
        
        return plant_life;

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
        List<Herbivore> herbivore = new ArrayList<>();
        
        enter_critical_section();
        herbivore = herbivore_list;
        exit_critical_section();
        
        return herbivore;

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
        List<Predator> predators = new ArrayList<>();
        
        enter_critical_section();
        predators = predator_list;
        exit_critical_section();
        
        return predators;

    } // End get_predator_list()
    
    /**********************************************************************
     *
     * FUNCTION: ActorState_evolve()
     *
     * DESCRIPTION: evolves the simulation one unit of time.
     *
     *********************************************************************/
    public void ActorState_evolve()
    {
        // use these local values to manipulate the values before
        // writing back
        //List<Rock>      evolve_rocks      = new ArrayList<>();
        List<PlantLife> evolve_plant_life = new ArrayList<>();
        List<Herbivore> evolve_herbivore  = new ArrayList<>();
        List<Predator>  evolve_predator   = new ArrayList<>();
        
        // get the current state values
        // probably uneccessary for ciritcal sections to be here
        enter_critical_section();
        //evolve_rocks      = rock_list;
        evolve_plant_life = plant_life_list;
        evolve_herbivore  = herbivore_list;
        evolve_predator   = predator_list;
        exit_critical_section();
        
        // evolve the simulation one unit of time
        
        // evolve plant life
        for (int i = 0; i < evolve_plant_life.size(); i++) {
            evolve_plant_life.set(i, evolve_a_plant(evolve_plant_life.get(i)));
        }
        
        // evolve herbivore
        for (int i = 0; i < evolve_herbivore.size(); i++) {
            evolve_herbivore.set(i, evolve_a_herbivore(evolve_herbivore.get(i)));
        }
        
        // evolve predators
        for (int i = 0; i < evolve_predator.size(); i++) {
            evolve_predator.set(i, evolve_a_predator(evolve_predator.get(i)));
        }
        
        // write the values back to the state
        enter_critical_section();
        //rock_list       = evolve_rocks;
        plant_life_list = evolve_plant_life;
        herbivore_list  = evolve_herbivore;
        predator_list   = evolve_predator;
        exit_critical_section();

    } // End ActorState_evolve()
    
    /**********************************************************************
     *
     * FUNCTION: evolve_a_plant()
     *
     * DESCRIPTION: evolves a plant
     *
     *********************************************************************/
    private PlantLife evolve_a_plant(PlantLife pl)
    {
        // evolve the plant
        int diameter = pl.get_diameter();
        
        // grow the plant if it is not at max size
        if (diameter < pl_max_size)
        {
            // grow the plant
            int new_diameter = (int)(diameter + diameter * pl_growth_rate);
            
            // check to see if it grew past max size
            if (new_diameter > pl_max_size)
                new_diameter = pl_max_size;
            
            // assign back the value
            diameter = new_diameter;
        }
        
        // if the plant hax reached maxed size, germinate
        if (diameter >= pl_max_size)
        {
            // increment the timer by one
            pl.increment_seed_pod_timer();
            
            // if the timer reaches the time to germinate, do it
            if (pl.get_seed_pod_timer() >= 3600)
            {
                // TODO germinate
                System.out.println("A plant germinated!");
                
                // TODO produce a random number of seeds from 0 to 
                //      "pl_max_seed_number" that are cast randomly in
                //      a radius around the plant "pl_max_seed_cast_distance"
                //      that also doesn't occupy another actor. Only a 
                //      percentage of these seeds will grow, defined by
                //      "pl_seed_viability"
                //      when a seed is produced, and is viable, it will 
                //      be produced 10 units of time later (have a germination
                //      counter before it starts doing regular shit.)
                //      The new plant will be initalized with the radius of
                //      1/100 of a distance unit. (.001)
        
                // reset the timer after spreading the seeds
                pl.reset_seed_pod_timer();
            }

        }
        
        // set the new diameter
        pl.set_diameter(diameter);
        // set the height as the radius of the plant
        pl.set_height(diameter/2);
        
        // return plant life data once it's done being manipulated.
        return pl;
        
    } // End evolve_a_plant()
    
    /**********************************************************************
     *
     * FUNCTION: evolve_a_herbivore()
     *
     * DESCRIPTION: evolves a herbivore
     *
     *********************************************************************/
    private Herbivore evolve_a_herbivore(Herbivore h)
    {
        // TODO evolve the herbivore
        
        // return herbivore data once it's done being manipulated.
        return h;
        
    } // End evolve_a_herbivore()
    
    /**********************************************************************
     *
     * FUNCTION: evolve_a_predator()
     *
     * DESCRIPTION: evolves a predator
     *
     *********************************************************************/
    private Predator evolve_a_predator(Predator p)
    {
        // TODO evolve the predator
        
        // return predator data once it's done being manipulated.
        return p;
        
    } // End evolve_a_predator()
    
} // End ActorState Class
