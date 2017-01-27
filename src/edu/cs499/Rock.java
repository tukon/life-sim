/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: Rock
 *
 * DESCRIPTION:
 *      This object will represent the rocks in the simulation. Inherits from
 *      the actor class.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 *
 ******************************************************************************/
package edu.cs499;

public class Rock extends Actor {
    private int diameter;
    private int height;
    
    /**********************************************************************
     *
     * FUNCTION: Rock()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * @param init_diameter 
     * @param init_height 
     * 
     *********************************************************************/
    public Rock(int init_x_pos, int init_y_pos, int init_diameter, int init_height)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        // set the diameter
        this.diameter = init_diameter;
        this.height   = init_height;
        
    } // End Rock()
    
     /**********************************************************************
     *
     * FUNCTION: get_diameter()
     *
     * DESCRIPTION: gets the current diameter of the rock
     *
     * @return
     * 
     *********************************************************************/
    public int get_diameter()
    {
        return this.diameter;
        
    } // End get_diameter
    
    /**********************************************************************
     *
     * FUNCTION: get_height()
     *
     * DESCRIPTION: gets the current height of the rock
     *
     * @return
     * 
     *********************************************************************/
    public int get_height()
    {
        return this.height;
        
    } // End get_height
    
    /**********************************************************************
     *
     * FUNCTION: set_diameter()
     *
     * DESCRIPTION: sets the current diameter of the rock
     *
     * @param new_diameter
     * 
     *********************************************************************/
    public void set_diameter(int new_diameter)
    {
        this.diameter = new_diameter;
        
    } // End get_diameter
        
    /**********************************************************************
     *
     * FUNCTION: set_height()
     *
     * DESCRIPTION: sets the current height of the rock
     *
     * @param new_height
     * 
     *********************************************************************/
    public void set_height(int new_height)
    {
        this.height = new_height;
        
    } // End get_diameter
    
} // End Rock class
