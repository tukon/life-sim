/*******************************************************************************
 *
 * 499 Senior Design Life Simulation
 * Team 2: Adam, Alicia, Matt, Paul
 *
 * OBJECT: Dog
 *
 * DESCRIPTION:
 *      This object provides the necessary functions to emulate a live dog...
 *
 * REVISION HISTORY:
 * 01-24-17  MPK  New.
 *
 ******************************************************************************/
package Sample;

public class Dog
// NOTE: At the start of each class/function add the opening bracket on the 
//       next line.
{
    // NOTE: line up the variables vertically
    private int     legs;
    private boolean tail;
    private String  hair;

    // NOTE: 80 count line in function header to reach near the end of the 
    //       default window, but still separate it from the original header.
    /**********************************************************************
     *
     * FUNCTION: Dog()
     *
     * DESCRIPTION: user-defined constructor for the Dog class.
     *
     * @param legs
     * @param tail
     * @param hair
     * 
     *********************************************************************/
    public Dog(int legs, boolean tail, String hair)
    {
            this.legs = legs;
            this.tail = tail;
            this.hair = hair;

    } // End Dog()

    /**********************************************************************
     *
     * FUNCTION: dog_get_legs()
     *
     * DESCRIPTION: returns the amount of legs the dog has.
     *
     * @return 
     * 
     *********************************************************************/
    // NOTE: for each function in a class, prefix it with the name of the class,
    //       or an abbreviation.
    public int dog_get_legs()
    {
            return legs;

    // NOTE: end each function like this, to show explicitly where it ends, so no
    //       eyes are strained! (also, leave a space in between the last line of
    //       the function, unless its a bracket, so it's easier to distinguish).
    } // End dog_get_legs()

    /**********************************************************************
     *
     * FUNCTION: dog_set_legs()
     *
     * DESCRIPTION: sets the amount of legs the dog has.
     *
     * @param legs
     * 
     *********************************************************************/
    public void dog_set_legs(int legs)
    {
            this.legs = legs;

    } // End dog_set_legs()

    /**********************************************************************
     *
     * FUNCTION: dog_get_tail()
     *
     * DESCRIPTION: gets whether the dog has a tail or not.
     *
     * @return 
     * 
     *********************************************************************/
    public boolean dog_get_tail()
    {
            return tail;

    } // End dog_get_tail()

    /**********************************************************************
     *
     * FUNCTION: dog_set_tail()
     *
     * DESCRIPTION: sets whether the dog has a tail or not.
     *                   
     * @param tail
     * 
    *********************************************************************/
    public void dog_set_tail(boolean tail)
    {
            this.tail = tail;

    } // End dog_set_tail()

    /**********************************************************************
     *
     * FUNCTION: dog_get_hair()
     *
     * DESCRIPTION: gets the type of hair the dog has, in string form. 
     * 
     * @return 
     * 
     *********************************************************************/ 
    public String dog_get_hair()  
    { 
            return hair; 

    } // End dog_get_hair() 

    /********************************************************************** 
     * 
     * FUNCTION: dog_set_hair() 
     * 
     * DESCRIPTION: set the type of hair the dog has, using a string. 
     * 
     * @param hair
     * 
     *********************************************************************/ 
    public void dog_set_hair(String hair)  
    { 
            this.hair = hair; 

    } // End dog_set_hair() 
    
 } // End Dog class 

