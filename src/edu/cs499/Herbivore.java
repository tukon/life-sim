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
 * 02-08-17  MPK  Finished implementing the base statistics.
 *
 ******************************************************************************/
package edu.cs499;

public class Herbivore extends Actor {
    private int energy;
    
    /**********************************************************************
     *
     * FUNCTION: Herbivore()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * @param init_energy
     * 
     *********************************************************************/
    public Herbivore(int init_x_pos, int init_y_pos, int init_energy)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        this.energy = init_energy;
        
    } // End Herbivore()

    /**********************************************************************
     *
     * FUNCTION: get_energy()
     *
     * DESCRIPTION: gets the current energy of the predator
     *
     * @return
     * 
     *********************************************************************/
    public int get_energy()
    {
        return this.energy;
        
    } // End get_energy()


    /**********************************************************************
     *
     * FUNCTION: set_energy()
     *
     * DESCRIPTION: sets the current energy of the predator
     *
     * @param new_energy
     * 
     *********************************************************************/
    public void set_energy(int new_energy)
    {
        this.energy = new_energy;
        
    } // End set_energy()
    
} // End Herbivore class
