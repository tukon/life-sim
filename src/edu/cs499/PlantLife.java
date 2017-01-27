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
 *
 ******************************************************************************/
package edu.cs499;

public class PlantLife extends Actor {
    
    /**********************************************************************
     *
     * FUNCTION: PlantLife()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * 
     *********************************************************************/
    public PlantLife(int init_x_pos, int init_y_pos)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
    } // End PlantLife()

    
} // End PlantLife class
