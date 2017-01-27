/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: Actor
 *
 * DESCRIPTION:
 *      This object will be the parent class to all the actors in the life
 *      simulation including rocks, plant life, herbivores, and predators.
 *
 * REVISION HISTORY:
 * 01-26-17  MPK  New.
 *
 ******************************************************************************/
package edu.cs499;

public class Actor {
    protected int x_pos;
    protected int y_pos;
    
    /**********************************************************************
     *
     * FUNCTION: Actor()
     *
     * DESCRIPTION: constructor for the Actor class. Must set x and y
     *              positions on creation.
     *
     * @param init_x_pos 
     * @param init_y_pos 
     * 
     *********************************************************************/
    public Actor(int init_x_pos, int init_y_pos)
    {
        this.x_pos = init_x_pos;
        this.y_pos = init_y_pos;
        
    } // end Actor()
    
    /**********************************************************************
     *
     * FUNCTION: get_x_pos()
     *
     * DESCRIPTION: gets the current x position of the actor
     *
     * @return
     * 
     *********************************************************************/
    public int get_x_pos()
    {
        return this.x_pos;
        
    } // end get_x_pos()
    
    /**********************************************************************
     *
     * FUNCTION: get_y_pos()
     *
     * DESCRIPTION: gets the current y position of the actor
     *
     * @return
     * 
     *********************************************************************/
    public int get_y_pos()
    {
        return this.x_pos;
        
    } // End get_y_pos()
    
    /**********************************************************************
     *
     * FUNCTION: set_x_pos()
     *
     * DESCRIPTION: sets the current x position of the actor
     *
     * @param new_x_pos
     * 
     *********************************************************************/
    public void set_x_pos(int new_x_pos)
    {
        this.x_pos = new_x_pos;
        
    } // End set_x_pos()
        
    /**********************************************************************
     *
     * FUNCTION: set_y_pos()
     *
     * DESCRIPTION: sets the current y position of the actor
     *
     * @param new_y_pos
     * 
     *********************************************************************/
    public void set_y_pos(int new_y_pos)
    {
        this.x_pos = new_y_pos;
        
    } // End set_y_pos()
    
} // End Actor class
