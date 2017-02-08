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
 *
 ******************************************************************************/
package edu.cs499;

public class PlantLife extends Actor {
    private int diameter;
    
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
     * 
     *********************************************************************/
    public PlantLife(int init_x_pos, int init_y_pos, int init_diameter)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        this.diameter = init_diameter;
        
    } // End PlantLife()

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
    
} // End PlantLife class
