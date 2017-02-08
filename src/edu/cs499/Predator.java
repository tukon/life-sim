/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: Predator
 *
 * DESCRIPTION:
 *      This object will represent the predator in the simulation. Inherits 
 *      from the actor class.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 * 02-08-17  MPK  Finished implementing the base statistics.
 *
 ******************************************************************************/
package edu.cs499;

public class Predator extends Actor {
    private int energy;
    private String genotype;
    
    /**********************************************************************
     *
     * FUNCTION: Predator()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * @param init_energy
     * @param init_genotype
     * 
     *********************************************************************/
    public Predator(int init_x_pos, int init_y_pos, int init_energy, String init_genotype)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        energy = init_energy;
        genotype = init_genotype;
        
    } // End Predator()
    
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
     * FUNCTION: get_genotype()
     *
     * DESCRIPTION: gets the genotype of the predator
     *
     * @return
     * 
     *********************************************************************/
    public String get_genotype()
    {
        return this.genotype;
        
    } // End get_genotype()

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
    
    /**********************************************************************
     *
     * FUNCTION: set_genotype()
     *
     * DESCRIPTION: sets the genotype of the predator
     *
     * @param new_genotype
     * 
     *********************************************************************/
    public void set_genotype(String new_genotype)
    {
        this.genotype = new_genotype;
        
    } // End set_genotype()
    
} // End Predator class
