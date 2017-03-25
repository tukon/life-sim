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
 * 03-03-17  MPK  Added functionality for the growth of the plant life.
 *                No functionality yet implemented for the plant life being 
 *                eaten by the herbivore.
 *
 ******************************************************************************/
package edu.cs499;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
    
    // create lists for offspring creation
    private List<PlantLife> plant_life_germination_list = new ArrayList<>();
    private List<Herbivore> herbivore_offspring_list  = new ArrayList<>();
    private List<Predator>  predator_offspring_list   = new ArrayList<>();
    
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
    private int    pl_germination_time       = 10;

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
    
    private List<Integer[]> current_coords;
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
        current_coords = new ArrayList<Integer[]>(2);
        
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
                PlantLife plant_life = new PlantLife(lsdp.PlantX, lsdp.PlantY, lsdp.PlantDiameter, 0); // no germination timer for existing plants
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
                Herbivore herbivore = new Herbivore(lsdp.GrazerX, lsdp.GrazerY, lsdp.GrazerEnergy, h_energy_input_rate,
                                                    h_energy_output_rate, h_energy_to_reproduce, h_maintain_speed_time, h_max_speed);
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
                int genes = Predator.parse_genotype(lsdp.PredatorGenotype);
                double max_speed = 0.0;
                switch (genes & Predator.SPEED)
                {
                case Predator.SPD_1 | Predator.SPD_2:  // FF
                    max_speed = p_max_speed_hod;
                    break;
                case Predator.SPD_1:  // Ff
                case Predator.SPD_2:  // fF
                    max_speed = p_max_speed_hed;
                    break;
                default:  // ff
                    max_speed = p_max_speed_hor;
                }
                
                Predator predator = new Predator(lsdp.PredatorX, lsdp.PredatorY,
                    lsdp.PredatorEnergy, p_energy_output_rate/5, genes,
                    p_maintain_speed_time, max_speed, p_energy_to_reproduce,
                    p_max_offspring, p_gestation_period,
                    p_offspring_energy_level);
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
        List<Rock>      evolve_rocks      = new ArrayList<>();
        List<PlantLife> evolve_plant_life = new ArrayList<>();
        List<Herbivore> evolve_herbivore  = new ArrayList<>();
        List<Predator>  evolve_predator   = new ArrayList<>();
        
        // get the current state values
        // probably uneccessary for ciritcal sections to be here
        enter_critical_section();
        evolve_rocks      = rock_list;
        evolve_plant_life = plant_life_list;
        evolve_herbivore  = herbivore_list;
        evolve_predator   = predator_list;
        exit_critical_section();
        
        // store the current x,y coordinates of all actors.
        // this will be checked against so that no actors will overlap
        
        refresh_coordinate_list(evolve_rocks, 
                                evolve_plant_life, 
                                evolve_herbivore, 
                                evolve_predator);
        
        // evolve the simulation one unit of time
        
        // evolve plant life
        // clear plant_life_germination_list before starting
        plant_life_germination_list.clear();
        for (int i = 0; i < evolve_plant_life.size(); i++) {
            PlantLife pl = evolve_a_plant(evolve_plant_life.get(i));
            if (pl.isAlive()) {
                evolve_plant_life.set(i, pl);
            }
            else {
                evolve_plant_life.remove(i);
            }
        }
        
        // add any plant life that were successfully germinated
        for (PlantLife new_plant_life : plant_life_germination_list) {
            evolve_plant_life.add(new_plant_life);
        }
        
        // refresh coordinate list after plant life moved
        refresh_coordinate_list(evolve_rocks, 
                                evolve_plant_life, 
                                evolve_herbivore, 
                                evolve_predator);
                
        // evolve herbivore
        for (int i = 0; i < evolve_herbivore.size(); i++) {
            Herbivore h = evolve_a_herbivore(evolve_herbivore.get(i));
            if (h.isAlive()) {
                // If our herbivore reproduced, generate a new one with half the original's energy
                if (h.hasReproduced()) {
                    evolve_herbivore.add(new Herbivore(h.get_x_pos()+20, h.get_y_pos()+20, h.get_energy()/2, h_energy_input_rate,
                                                    h_energy_output_rate, h_energy_to_reproduce, h_maintain_speed_time, h_max_speed));
                    h.set_energy(h.get_energy()/2);
                    h.setReproduced(false);
                }
                evolve_herbivore.set(i, h);
                
            }
            else {
                evolve_herbivore.remove(i);
            }
        }
        
        // refresh coordinate list after herbivores move
        refresh_coordinate_list(evolve_rocks, 
                                evolve_plant_life, 
                                evolve_herbivore, 
                                evolve_predator);
        
        // evolve predators
        for (int i = 0; i < evolve_predator.size(); i++) {
            //evolve_predator.set(i, evolve_a_predator(evolve_predator.get(i)));
            Predator p = evolve_a_predator(evolve_predator.get(i));
            if (p.is_alive())
            {
                // TODO: reproduction
            }
            else
            {
                evolve_predator.remove(i);
            }
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
     * @param pl
     * @return
     *
     *********************************************************************/
    private PlantLife evolve_a_plant(PlantLife pl)
    {
        if (pl.isAlive()) {
            // if a seed is still germinating
            if (pl.get_germination_timer() < pl_germination_time)
            {
                // increment timer then return
                pl.increment_germination_timer();
                return pl;
            }

            if (pl.isGrowing()) {
                // evolve the plant if germinated
                double diameter = pl.get_diameter();

                // grow the plant if it is not at max size
                if (diameter < pl_max_size)
                {
                    // grow the plant
                    double new_diameter = diameter + diameter * pl_growth_rate;

                    // check to see if it grew past max size
                    if (new_diameter > pl_max_size)
                    {
                        new_diameter = pl_max_size;
                        pl.setReachedMaxSize();
                    }

                    // assign back the value
                    diameter = new_diameter;
                }

                // set the new diameter
                pl.set_diameter(diameter);
                // set the height as the radius of the plant
                pl.set_height(diameter/2);
            }
            else {
                // if the plant has reached maxed size, germinate
                // increment the timer by one
                pl.increment_seed_pod_timer();

                // if the timer reaches the time to germinate, germinate!
                if (pl.get_seed_pod_timer() >= 3600)
                {
                    // create random object
                    Random rand = new Random();
                    // find random number of seeds that plant will attempt to germinate
                    int seeds = rand.nextInt(pl_max_seed_number);
                    // multiply seeds by the viability to see how many will survive
                    seeds = (int)(seeds * pl_seed_viability);
                    // create int list to find coordinates where the seeds will be 
                    // planted
                    List<Integer[]> seed_coords = new ArrayList<Integer[]>(2);
                    seed_coords = find_seed_coords(seeds, pl.get_x_pos(), pl.get_y_pos());

                    // for each set of coordinates, add a new plant, then add to the
                    // master list.
                    for (Integer[] coords : seed_coords) {
                        // if germination happens, add to the last, so it can be
                        // added once evolving is done.
                        PlantLife plant_life = new PlantLife(coords[0], coords[1], .001, 10);
                        plant_life_list.add(plant_life);

                        // add the coordinates to the master list.
                        // list will be refreshed after all of plant life is done
                        // being evolved. until then, we don't want plant life
                        // planting more plant life in this same coordinate!
                        current_coords.add(coords);
                    }

                    // reset the timer after spreading the seeds
                    pl.reset_seed_pod_timer();
                }
            }
        }
        
        // return plant life data once it's done being manipulated.
        return pl;
        
    } // End evolve_a_plant()
    
    /**********************************************************************
     *
     * FUNCTION: evolve_a_herbivore()
     *
     * DESCRIPTION: evolves a herbivore
     * 
     * @param h
     * @return 
     *
     *********************************************************************/
    private Herbivore evolve_a_herbivore(Herbivore h)
    {
        if (h.isAlive()) {
            // If currently eating, continue 
            if (h.isEating()) 
            {
                h.eat();
            }
            else {
                h.moveToFood(plant_life_list, rock_list, herbivore_list);
            }
        }    
    
        // return herbivore data once it's done being manipulated.
        return h;
    } // End evolve_a_herbivore()
    
    /**********************************************************************
     *
     * FUNCTION: evolve_a_predator()
     *
     * DESCRIPTION: evolves a predator
     * 
     * @param p
     * @return
     *
     *********************************************************************/
    private Predator evolve_a_predator(Predator p)
    {
        p.think(predator_list, rock_list, herbivore_list, (int)w_grid_width,
            (int)w_grid_height);
        
        // return predator data once it's done being manipulated.
        return p;
        
    } // End evolve_a_predator()
    
    /**********************************************************************
     *
     * FUNCTION: refresh_coordinate_list()
     *
     * DESCRIPTION: refreshes the current coordinates to be checked 
     *              against when moving actors
     * 
     * @param rock_list
     * @param plant_life_list
     * @param herbivore_list
     * @param predator_list
     *
     *********************************************************************/
    private void refresh_coordinate_list(List<Rock>      rock_list, 
                                         List<PlantLife> plant_life_list, 
                                         List<Herbivore> herbivore_list, 
                                         List<Predator>  predator_list)
    {
        // clear list first
        current_coords.clear();
        // add rock coords
        rock_list.stream().map((rock) -> new Integer[] {rock.x_pos, rock.y_pos}).forEachOrdered((coords) -> {
            current_coords.add(coords);
        });
        // add plant life coords
        plant_life_list.stream().map((plant) -> new Integer[] {plant.x_pos, plant.y_pos}).forEachOrdered((coords) -> {
            current_coords.add(coords);
        });
        // add herbivore coords
        herbivore_list.stream().map((herbivore) -> new Integer[] {herbivore.x_pos, herbivore.y_pos}).forEachOrdered((coords) -> {
            current_coords.add(coords);
        });
        // add predator coords
        predator_list.stream().map((predator) -> new Integer[] {predator.x_pos, predator.y_pos}).forEachOrdered((coords) -> {
            current_coords.add(coords);
        });
        
    } // End refresh_coordinate_list()

    /**********************************************************************
     *
     * FUNCTION: find_seed_coords()
     *
     * DESCRIPTION: finds the seed coordinates
     * 
     * @param seeds
     * @param xCenter
     * @param yCenter
     * @return
     *
     *********************************************************************/
    private List<Integer[]> find_seed_coords(int seeds, int xCenter, int yCenter)
    {
        List<Integer[]> possible_seed_coords = new ArrayList<Integer[]>(2);
        List<Integer[]> seed_coords = new ArrayList<Integer[]>(2);
        int radius = pl_max_seed_cast_distance;
        
        // TODO find seed coordinates
        
        // find all possible coordinates within the max seed cast distance
        // from the coordinate the plant is rooted.
        for (int x = xCenter - radius ; x <= xCenter; x++)
        {
            for (int y = yCenter - radius ; y <= yCenter; y++)
            {
                // we don't have to take the square root, it's slow
                if ((x - xCenter)*(x - xCenter) + (y - yCenter)*(y - yCenter) <= radius*radius) 
                {
                    int xSym = xCenter - (x - xCenter);
                    int ySym = yCenter - (y - yCenter);
                    // (x, y), (x, ySym), (xSym , y), (xSym, ySym) are in the circle
                    possible_seed_coords.add(new Integer[] {x,    y});
                    possible_seed_coords.add(new Integer[] {x,    ySym});
                    possible_seed_coords.add(new Integer[] {xSym, y});
                    possible_seed_coords.add(new Integer[] {xSym, ySym});
                }
            }
        }
        
        // find random, and unique coordinates, randomly, for the number
        // of seeds that will germinate.
        Random rand = new Random();
        for (int x = 0; x < seeds; x++)
        {
            // get random index out of the possible indices
            int random_coord_index = rand.nextInt(possible_seed_coords.size());
            
            // if the coordinate already contains someone, discard this index
            // and add another x to the loop so we still get the appropiate
            // amount of germinated seeds cast.
            if (current_coords.contains(possible_seed_coords.get(random_coord_index)))
            {
                possible_seed_coords.remove(random_coord_index);
                x--;
            }
            else
            {
                // add those coordinates, with the index, to the seed_coords list
                seed_coords.add(possible_seed_coords.get(random_coord_index));
                // remove the index added from the list of possible seed locations.
                possible_seed_coords.remove(random_coord_index);
            }
        }
        
        // return the list of seed coordinates
        return seed_coords;
        
    } // End find_seed_coords()
    
} // End ActorState Class
