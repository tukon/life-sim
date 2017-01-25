/**********************************************************************************
 *
 * 499 Senior Design Life Simulation
 *
 * OBJECT: DogCreator
 *
 * DESCRIPTION:
 *      This object is the starting point for the simulation. The simulation will
 *      create a dog with specific attributes, then output the attributes.
 *
 * REVISION HISTORY:
 * 01-24-17  MPK  New.
 *
 *********************************************************************************/

package Sample;

public class DogCreator
{

        /**********************************************************************
         *
         * FUNCTION: main()
         *
         * DESCRIPTION: starting point for program. initialize everything here.
         *
         * @param args
         * 
         *********************************************************************/
        public static void main(String[] args)
        {

                // create new dog object
                Dog new_dog = new Dog(2, false, "No Hair");

                // print out the attributes of the dog.
                System.out.println("This dog has " + new_dog.dog_get_legs() + " legs");
                System.out.println("Has a tail?: " + new_dog.dog_get_tail());
                System.out.println("This dog has " + new_dog.dog_get_hair() + " hair");
                
                // set the dog's attributes
                new_dog.dog_set_legs(3);
                new_dog.dog_set_tail(true);
                new_dog.dog_set_hair("Furry");

                // print out the attributes of the dog.
                System.out.println("This dog has " + new_dog.dog_get_legs() + " legs");
                System.out.println("Has a tail?: " + new_dog.dog_get_tail());
                System.out.println("This dog has " + new_dog.dog_get_hair() + " hair");

        } // End main()
        
} // End DogCreator class

