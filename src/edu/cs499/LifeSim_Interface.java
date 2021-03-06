/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: LifeSim_Interface
 *
 * DESCRIPTION:
 *      This object provides the interface for initializing all modules except
 *      the GUI.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 * 02-08-17  MPK  Implemented functionality to retrieve the world, herbivore,
 *                predator, and plant life data (found in data parsers).
 * 02-27-17  MPK  Changed the implementation so that the GUI controller now
 *                creates an instance of this class, to instantiate and control
 *                the evolution of the actors.
 * 02-28-17  MPK  Moved the data involved in the instantiation of the actors
 *                to its own class. Now, this object creates an instance of 
 *                Actor state and controls when it evolves (how fast the time
 *                (units pass).
 *                The GUI interacts with this class to manipulate the flow of 
 *                the simulation.
 * 03-01-17  MPK  Added the ActorState_evolve function to the timer function.
 *                (Adam added the timer functionality in the last revision).
 *
 ******************************************************************************/
package edu.cs499;

import java.util.List;

public class LifeSim_Interface {
    
    // global variables to control the flow
    private ActorState sim_actor_state;
    private Timer      sim_timer;
    private Runnable   time_task;
    private int        sim_clock;
    
    // start influenced by the GUI buttons
    private enum Sim_State {
        OFF,
        GO,
        NOGO;
    }
    
    // state variable
    private Sim_State sim_state;

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
        LifeSim_Interface_create();
        
    } // End LifeSim_Interface()
    
    /**********************************************************************
     *
     * FUNCTION: LifeSim_Interface_create()
     *
     * DESCRIPTION: initializes the actor state
     * 
     *********************************************************************/
    private void LifeSim_Interface_create()
    {
        sim_actor_state = new ActorState();
        sim_state       = Sim_State.OFF;
        sim_clock       = 0;
        
        LifeSim_Interface_server();

    } // End LifeSim_Interface_create()
    
    /**********************************************************************
     *
     * FUNCTION: LifeSim_Interface_server()
     *
     * DESCRIPTION: starts the interface server
     * 
     *********************************************************************/
    private void LifeSim_Interface_server()
    {

        // Start the Simulation Thread
        time_task = new Start_Simulation();
        sim_timer = new Timer(time_task);

    } // End LifeSim_Interface_server()
    
    /**********************************************************************
     *
     * FUNCTION: get_rock_list()
     *
     * DESCRIPTION: get the current list of rocks
     *
     * @return
     * 
     *********************************************************************/
    public List<Rock> get_rock_list() 
    {
        return sim_actor_state.get_rock_list_state();

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
    public List<PlantLife> get_plant_life_list() 
    {
        return sim_actor_state.get_plant_life_list_state();

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
    public List<Herbivore> get_herbivore_list() 
    {
        return sim_actor_state.get_herbivore_list_state();

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
    public List<Predator> get_predator_list() 
    {
        return sim_actor_state.get_predator_list_state();

    } // End get_predator_list()
    
    /**********************************************************************
     *
     * FUNCTION: start_sim()
     *
     * DESCRIPTION: starts the simulation via the interface
     * 
     *********************************************************************/
    public void start_sim() 
    {
        sim_state = Sim_State.GO;
        sim_timer.start();

    } // End start_sim()
    
    /**********************************************************************
     *
     * FUNCTION: pause_sim()
     *
     * DESCRIPTION: pauses the simulation via the interface
     * 
     *********************************************************************/
    public void pause_sim() 
    {
        sim_state = Sim_State.NOGO;
        sim_timer.suspend();
        
    } // End pause_sim()
    
    /**********************************************************************
     *
     * FUNCTION: end_sim()
     *
     * DESCRIPTION: ends the simulation via the interface
     * 
     *********************************************************************/
    public void end_sim() 
    {
        sim_state = Sim_State.OFF;
        sim_timer.terminate();

    } // End end_sim()
    
    /**********************************************************************
     *
     * FUNCTION: change_sim_speed()
     *
     * DESCRIPTION: changes the simulation speed via the interface
     * 
     * @param speed
     * 
     *********************************************************************/
    public void change_sim_speed(Timer.TimerSpeed speed) 
    {
        sim_timer.setSpeed(speed);

    } // End change_sim_speed()
    
    /**********************************************************************
     *
     * FUNCTION: output_sim_statistics()
     *
     * DESCRIPTION: outputs the curent statistic info via the interface
     * 
     * @param filename
     * 
     *********************************************************************/
    public void output_sim_statistics(String filename) 
    {
        // TODO call an output module
        System.out.println("output_sim_statistics() called with filename: \"" + filename + "\"");

    } // End output_sim_statistics()
    
    /**********************************************************************
     *
     * FUNCTION: get_sim_time()
     *
     * DESCRIPTION: changes the simulation speed via the interface
     * 
     * @return 
     * 
     *********************************************************************/
    public int get_sim_time() 
    {
        return sim_clock;

    } // End get_sim_time()
   
    /**********************************************************************
     *
     * CLASS: Start_Simulation()
     *
     * DESCRIPTION: nested class that implements Runnable for creating a
     *              thread that starts the Simulation. This is so the 
     *              GUI can run concurrently with the background
     *              simulation processes.
     *
     * @param args
     * 
     *********************************************************************/
    private class Start_Simulation implements Runnable {

        /**********************************************************************
         *
         * FUNCTION: Start_Simulation()
         *
         * DESCRIPTION: constructor for local Start_Simulation class
         * 
         *********************************************************************/
        Start_Simulation() 
        {

        } // End Start_Simulation

        /**********************************************************************
         *
         * FUNCTION: run()
         *
         * DESCRIPTION: constructor for local Start_Simulation class. This is
         * called by the timer on each tick.
         * 
         *********************************************************************/
        @Override
        public void run() 
        {
            // move the simulation one time unit forward
            sim_actor_state.ActorState_evolve();
            
            // move the simulation clock for the GUI one time unit forward.
            ++sim_clock;
            
        } // End run()
        
    } // End Start_Simulation class
    
} // End LifeSim_Interface class