/* 
 * Project: Life Sim for CS 499
 * Team 2: Adam, Alicia, Matt, Paul
 */
package edu.cs499.tests;

import edu.cs499.Predator;
import edu.cs499.Rock;
import java.util.ArrayList;

/**
 * Tests various functions of the Predator class.
 * @author adam
 */
public class PredatorTests {
    /**
     * Runs the predator tests by themselves.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PredatorTests test = new PredatorTests();
        test.runTests();
    }
    
    public void runTests()
    {
        Predator p = new Predator(100, 10,
            450,
            10,
            (Predator.AGR_1 | Predator.SPD_1 | Predator.STR_1),
            4.0,
            18.0,
            500,
            3,
            5.0,
            100);
        
        ArrayList<Rock> rocks = new ArrayList<>();
        int height = p.get_height();
        // Can’t see over this one (right):
        rocks.add(new Rock(120, 10, 10, height + 2));
        // Can see over this one (left):
        rocks.add(new Rock(80, 10, 10, height - 2));
        
        // Right target
        Predator targetR = new Predator(200, 10,
            450,
            10,
            (Predator.AGR_1 | Predator.SPD_1 | Predator.STR_1),
            4.0,
            18.0,
            500,
            3,
            5.0,
            100);
        
        // Left target
        Predator targetL = new Predator(0, 10,
            450,
            10,
            (Predator.AGR_1 | Predator.SPD_1 | Predator.STR_1),
            4.0,
            18.0,
            500,
            3,
            5.0,
            100);
        
        System.out.print("Line-of-sight test 1: ");
        if (p.visible(targetR, rocks) == false)
        {
            System.out.println("PASSED");
        }
        else
        {
            System.out.println("FAILED");
        }
        
        System.out.print("Line-of-sight test 2: ");
        if (p.visible(targetL, rocks) == true)
        {
            System.out.println("PASSED");
        }
        else
        {
            System.out.println("FAILED");
        }
    }
}
