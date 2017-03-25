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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Herbivore extends Actor {
    private int inputRate;
    private int outputRate;
    private int energyToReproduce;
    private double maintainSpeedTime;
    private double maxSpeed;
    private int energy;
    private boolean eating;
    private int maxEatingTime = 600;
    private int eatingTimer;
    private int maxEnergyTime = 60;
    private int energyTimer;
    private PlantLife eatingPlant;
    private boolean alive = true;
    private boolean reproduced = false;

    
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
    public Herbivore(int init_x_pos, int init_y_pos, int init_energy, int input_rate, int output_rate, 
                     int energy_to_reproduce, double maintain_speed_time, double max_speed)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        this.energy = init_energy;
        this.inputRate = input_rate;
        this.outputRate = output_rate;
        this.energyToReproduce = energy_to_reproduce;
        this.maintainSpeedTime = maintain_speed_time;
        this.maxSpeed = max_speed;
        
    } // End Herbivore()

    /**********************************************************************
     *
     * FUNCTION: get_energy()
     *
     * DESCRIPTION: gets the current energy of the herbivore
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
     * DESCRIPTION: sets the current energy of the herbivore
     *
     * @param new_energy
     * 
     *********************************************************************/
    public void set_energy(int new_energy)
    {
        this.energy = new_energy;
        
    } // End set_energy()
    
    public boolean isEating() 
    {
        return this.eating;
    }
    
    public void startEating()
    {
        this.eating = true;
        this.eatingTimer = this.maxEatingTime;
        this.energyTimer = this.maxEnergyTime;
    }
    
    public void stopEating() 
    {
        this.eating = false;
        this.eatingPlant = null;
    }
    
    public void eat() 
    {
        this.eatingTimer--;
        this.energyTimer--;
        
        if (this.eatingTimer == 0) 
        {
            this.eating = false;
            this.eatingPlant.set_diameter(this.eatingPlant.get_diameter() - 5.0);
            if (this.eatingPlant.get_diameter() <= 0) {
                this.eatingPlant.setIsAlive(false);
                this.eatingPlant = null;
            }
            else {
                this.startEating();
            }
        }
        
        if (this.energyTimer == 0) 
        {
            this.energy += this.inputRate;
            this.energyTimer = this.maxEnergyTime;
            
            if (this.energy > this.energyToReproduce) {
                this.reproduced = true;
            }
        }
    }
    
    public void moveToFood(List<PlantLife> plants, List<Rock> rocks, List<Herbivore> herbivores) 
    {
        double moveRange = this.maxSpeed;
        
        // No visible plants, so move in some direction
        // If energy is less than 25, we can only move 10 DU
        if (this.energy < 25) {
            moveRange = 10;
        }
        
        // if we have a chosen plant to eat, continue moving toward it
        if (this.eatingPlant != null) {
            // coult make this random, but for now move along x and then y
            int diffX = this.eatingPlant.get_x_pos() - this.get_x_pos();
            int leftover = 0;
            
            if (diffX != 0) {
                if (diffX < 0) {
                    diffX = diffX * -1;
                    
                    if ((int)moveRange > diffX) {
                        leftover = (int)moveRange - diffX;
                        this.set_x_pos(this.get_x_pos() - diffX);
                    }
                    else {
                        this.set_x_pos(this.get_x_pos() - (int)moveRange);
                    }
                }
                else {
                    if ((int)moveRange > diffX) {
                        leftover = (int)moveRange - diffX;
                        this.set_x_pos(this.get_x_pos() + diffX);
                    }
                    else {
                        this.set_x_pos(this.get_x_pos() + (int)moveRange);
                    }
                }     
            }  
            else {
                leftover = (int)moveRange;
            }
            
            if (leftover > 0) {
                int diffY = this.eatingPlant.get_y_pos() - this.get_y_pos();
                
                if (diffY < 0) {
                    diffY = diffY * -1;
                    
                    if (leftover > diffY) {
                        this.set_y_pos(this.get_y_pos() - diffY);
                        leftover -= diffY;
                    }
                    else {
                        this.set_y_pos(this.get_y_pos() - (int)moveRange);
                        leftover = 0;
                    }
                }
                else {
                    if ((int)leftover > diffY) {
                        this.set_y_pos(this.get_y_pos() + diffY);
                        leftover -= diffY;
                    }
                    else {
                        this.set_y_pos(this.get_y_pos() + (int)moveRange);
                        leftover = 0;
                    }
                }
            }
            
            this.spendEnergy((int)moveRange - leftover);
            
            if (this.get_x_pos() == this.eatingPlant.get_x_pos() && this.get_y_pos() == this.eatingPlant.get_y_pos()) {
                boolean spotOpen = false;
                while(!spotOpen) {
                    spotOpen = true;
                    for (Herbivore grazer : herbivores) {
                        // if there is already an herbivore at this location, move to the side of it
                        if (grazer != this && grazer.get_x_pos() == this.get_x_pos() && grazer.get_y_pos() == this.get_y_pos()) {
                            spotOpen = false;
                            
                            Random rnd = new Random();
                            int direction = rnd.nextInt(3);
                            switch (direction) {
                                case 0:
                                    this.set_x_pos(this.get_x_pos() + 11);
                                    break;
                                case 1:
                                    this.set_y_pos(this.get_y_pos() + 11);
                                    break;
                                case 2:
                                    this.set_x_pos(this.get_x_pos() - 11);
                                    break;
                                default:
                                    this.set_y_pos(this.get_y_pos() - 11);
                                    break;
                            }
                            break;
                        }  
                    }
                }
                
                // Arrived at food
                this.startEating();
            }
            return;
        }
        
        // Get list of plants that are in movement range
        List<PlantLife> inRange = this.plantsInRange(plants);
        Random rn = new Random();
        
        if (inRange.size() > 0) {
            // Filter out plants that are not visible due to rocks
            List<PlantLife> visible = this.visiblePlants(inRange, rocks);
            
            if (visible.size() > 0) {
                // We have visible plants, so move toward one
                int minDistance = 1000;
                int minIndex = 0;
                int index = 0;
                
                while (index < visible.size()) {
                    int diffx = this.get_x_pos() - visible.get(index).get_x_pos();
                    int diffy = this.get_y_pos() - visible.get(index).get_y_pos();
                    int distance = Math.abs(diffx) + Math.abs(diffy);
                    
                    if (distance < minDistance) {
                        minDistance = distance;
                        minIndex = index;
                    }
                    
                    index++;
                }
                
                this.eatingPlant = visible.get(minIndex);
                this.moveToFood(plants, rocks, herbivores);
            }
        }
        
        // Select a random x between 1 and max speed
        int x = rn.nextInt((int)moveRange) + 1;
        int y = (int)moveRange - x;
        
        int dirX = rn.nextInt(1);
        if (dirX == 0) {
            // If 0, move to the left
            this.set_x_pos(this.get_x_pos() - x);
        }
        else {
            this.set_x_pos(this.get_x_pos() + x);
        }
        
        int dirY = rn.nextInt(1);
        if (dirY == 0) {
            this.set_y_pos(this.get_y_pos() - y);
        }
        else {
            this.set_y_pos(this.get_y_pos() + y);
        }
        
        // For every 5 DU used, lose energy at the output rate
        this.spendEnergy(this.outputRate * ((int)moveRange/5));
    }
    
    private void spendEnergy(int amount) {
        this.energy -= amount;
        
        if (this.energy <= 0) {
            this.alive = false;
        }
    }
    
    public void die() {
        this.energy = 0;
        this.alive = false;
    }
    
    public boolean isAlive() {
        return this.alive;
    }
    
    private List<PlantLife> plantsInRange(List<PlantLife> plants) {
        List<PlantLife> inRange = new ArrayList<>();
        
        int minX = this.x_pos - 150;
        int maxX = this.x_pos + 150;
        if (minX < 0) { 
            minX = 0;
        }
        
        int minY = this.y_pos - 150;
        int maxY = this.y_pos + 150;
        if (minY < 0) {
            minY = 0;
        }
        
        // Loook at all plants and determine which are in range for movement
        for(PlantLife plant : plants)
        {
            /*double plantMinX = plant.get_x_pos() - plant.get_diameter()/2;
            double plantMaxX = plant.get_x_pos() + plant.get_diameter()/2;
            double plantMinY = plant.get_y_pos() - plant.get_diameter()/2;
            double plantMaxY = plant.get_y_pos() + plant.get_diameter()/2;
            
            if ((plantMinX >= minX && plantMaxX <= maxX) &&
                (plantMinY >= minY && plantMaxY <= maxY)) {
                inRange.add(plant);
            }*/
            // For now, just look at the plants center
            if ((plant.get_x_pos() >= minX && plant.get_x_pos() <= maxX) &&
                (plant.get_y_pos() >= minY && plant.get_y_pos() <= maxY)) {
                inRange.add(plant);
            }
        }
        
        return inRange;
    }
    
    private List<PlantLife> visiblePlants(List<PlantLife> plants, List<Rock> rocks) 
    {
        List<PlantLife> visible = new ArrayList<>();
        
        for (PlantLife plant : plants) {  
            boolean isVisible = true;
            boolean right = false;
            boolean up = false;
            boolean xEqual = false;
            boolean yEqual = false;
            
            int x = this.get_x_pos();
            int y = this.get_y_pos();
            
            if (plant.get_x_pos() > x) {
                right = true;
            }
            else if (plant.get_x_pos() == x) {
                xEqual = true;
            }
           
            if (plant.get_y_pos() < y) {
                up = true;
            }
            else if (plant.get_y_pos() == y) {
                yEqual = true;
            }
            
            while(true) {
                if (!xEqual) {
                    if (right) 
                        x++;
                    else 
                        x--;
                }
                
                if (!yEqual) {
                    if (up)
                        y++;
                    else 
                        y--;
                }
                
                if (x != plant.get_x_pos() && y != plant.get_y_pos()) {
                    for (Rock rock : rocks) {
                        if (rock.get_x_pos() == x && rock.get_y_pos() == y) {
                            // Rock is in between herbivore and plant
                            isVisible = false;
                        }
                    }
                }
                else {
                    break;
                }   
            }
            
            if (isVisible) {
                visible.add(plant);
            }
        }
        
        return visible;
    }
    
    public boolean hasReproduced() {
        return this.reproduced;
    }
    
    public void setReproduced(boolean val) {
        this.reproduced = val;
    }
    
} // End Herbivore class
