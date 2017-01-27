/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: Herbivore
 *
 * DESCRIPTION:
 *      This object will represent the herbivore in the simulation. Inherits 
 *      from the actor class.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 *
 ******************************************************************************/
package edu.cs499;

public class Herbivore extends Actor {
    
    /**********************************************************************
     *
     * FUNCTION: Herbivore()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * 
     *********************************************************************/
    public Herbivore(int init_x_pos, int init_y_pos)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
    } // End Herbivore()

    
} // End Herbivore class
