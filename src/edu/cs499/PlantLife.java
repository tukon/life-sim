/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: PlantLife
 *
 * DESCRIPTION:
 *      This object will represent the plant life in the simulation. Inherits 
 *      from the actor class.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 * 02-08-17  MPK  Finished implementing the base statistics.
 * 03-02-17  MPK  Added functionality for the growth of a plant
 * 03-03-17  MPK  Added functionality for the growth of the plant life.
 *                No functionality yet implemented for the plant life being 
 *                eaten by the herbivore.
 *
 ******************************************************************************/
package edu.cs499;

public class PlantLife extends Actor {
    private double diameter;
    private double height; // needs to be double, because it halves the diameter
    private int    seed_pod_timer;
    private int    germination_timer;
    
    /**********************************************************************
     *
     * FUNCTION: PlantLife()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * @param init_diameter
     * @param init_germ_timer
     * 
     *********************************************************************/
    public PlantLife(int init_x_pos, int init_y_pos, double init_diameter, int init_germ_timer)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        this.diameter          = init_diameter;
        this.height            = init_diameter/2;
        this.germination_timer = init_germ_timer;
        this.seed_pod_timer    = 0;
        
    } // End PlantLife()

    /**********************************************************************
     *
     * FUNCTION: get_diameter()
     *
     * DESCRIPTION: gets the current diameter of the plant life
     *
     * @return
     * 
     *********************************************************************/
    public double get_diameter()
    {
        return this.diameter;
        
    } // End get_diameter
    
    /**********************************************************************
     *
     * FUNCTION: get_height()
     *
     * DESCRIPTION: gets the current height of the plant life
     *
     * @return
     * 
     *********************************************************************/
    public double get_height()
    {
        return this.height;
        
    } // End get_height
    
    /**********************************************************************
     *
     * FUNCTION: get_seed_pod_timer()
     *
     * DESCRIPTION: returns the seed pod timer
     *
     * @return 
     * 
     *********************************************************************/
    public int get_seed_pod_timer()
    {
        return this.seed_pod_timer;
        
    } // End get_seed_pod_timer
    
    /**********************************************************************
     *
     * FUNCTION: get_germination_timer()
     *
     * DESCRIPTION: returns the germination timer
     *
     * @return 
     * 
     *********************************************************************/
    public int get_germination_timer()
    {
        return this.germination_timer;
        
    } // End get_germination_timer
    
    /**********************************************************************
     *
     * FUNCTION: set_diameter()
     *
     * DESCRIPTION: sets the current diameter of the plant life
     *
     * @param new_diameter
     * 
     *********************************************************************/
    public void set_diameter(double new_diameter)
    {
        this.diameter = new_diameter;
        
    } // End set_diameter
    
    /**********************************************************************
     *
     * FUNCTION: set_height()
     *
     * DESCRIPTION: sets the current height of the plant life
     *
     * @param new_height
     * 
     *********************************************************************/
    public void set_height(double new_height)
    {
        this.height = new_height;
        
    } // End set_height
        
    /**********************************************************************
     *
     * FUNCTION: increment_seed_pod_timer()
     *
     * DESCRIPTION: increments the seed pod timer
     *
     *********************************************************************/
    public void increment_seed_pod_timer()
    {
        this.seed_pod_timer++;
        
    } // End increment_seed_pod_timer
    
    /**********************************************************************
     *
     * FUNCTION: increment_germination_timer()
     *
     * DESCRIPTION: increments the germination counter
     *
     *********************************************************************/
    public void increment_germination_timer()
    {
        this.germination_timer++;
        
    } // End increment_germination_timer
    
    /**********************************************************************
     *
     * FUNCTION: reset_seed_pod_timer()
     *
     * DESCRIPTION: resets the seed pod timer
     * 
     *********************************************************************/
    public void reset_seed_pod_timer()
    {
        this.seed_pod_timer = 0;
        
    } // End reset_seed_pod_timer
    
} // End PlantLife class
