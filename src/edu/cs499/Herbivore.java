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
 * 03-08-17  AJP  Finished implementing basic logic and movement.
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
    private Predator fleeingFrom;
    private boolean alive = true;
    private boolean reproduced = false;
    private boolean fleeing = false;
    private double fleeingTimer;

    
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
    
    public void think(List<PlantLife> plants, List<Predator> predators, List<Rock> rocks,
        List<Herbivore> herbivores, int worldWidth, int worldHeight) 
    {
        checkSurroundings(predators, rocks);
                
        if (!fleeing) {
            // If currently eating, continue 
            if (isEating()) 
            {
                eat();
            }
            else {
                moveToFood(plants, rocks, herbivores);
            }
        }
        else 
        {
            flee(worldWidth, worldHeight);
        }
    }
    
    public void checkSurroundings(List<Predator> predators, List<Rock> rocks) 
    {    
        Actor closest = getClosestActor(predators, rocks);
        
        if (closest != null) {
            fleeing = true;
            stopEating();
            fleeingFrom = (Predator)closest;
            fleeingTimer = this.maintainSpeedTime;
        }
        else 
        {
            fleeing = false;
            fleeingFrom = null;
        }
    }
    
    public void flee(int worldWidth, int worldHeight) 
    {
        int leftover = 0;
        double moveRange;
        
        if (fleeingTimer > 0) 
        {
            moveRange = getMoveRange();
        }
        else 
        {
            moveRange = this.maxSpeed * .75;
        }
        
        int x = fleeingFrom.x_pos;
        int y = fleeingFrom.y_pos;
        
        // Make dx,dy a vector pointing away from the target
        double dx = (x - x_pos)*-1;
        double dy = (y - y_pos)*-1;

        // Make it a unit vector
        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        
        dx /= dist;
        dy /= dist;

        dx *= (int)moveRange;
        dy *= (int)moveRange;
        
        if (dx <= 0) 
        {
            leftover = (int)dx * -1;
            x_pos = 0;
        }
        else if (dx >= worldWidth) 
        {
            leftover = (int)dx - worldWidth;
            x_pos = worldWidth;
        }
        else {
            x_pos += dx;
        }
             
        if (dy <= 0)
        {
            leftover = (int)dy * -1;
            y_pos = 0;
        }
        else if (dy >= worldHeight)
        {
            leftover = (int)dy - worldHeight;
            y_pos = worldHeight;
        }
        else 
        {
            y_pos += dy;
        }    
        
        int moved = (int)moveRange - leftover;
            
        this.spendEnergy((moved/5)*this.outputRate);
        fleeingTimer--;
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
    
    double getMoveRange() {
        double moveRange = maxSpeed;
        // If energy is less than 25, we can only move 10 DU
        if (this.energy < 25) {
            moveRange = 10;
        }
        
        return moveRange;
    }
    
    public void moveToFood(List<PlantLife> plants, List<Rock> rocks, List<Herbivore> herbivores) 
    {
        double moveRange = getMoveRange();
        
        // if we have a chosen plant to eat, continue moving toward it
        if (this.eatingPlant != null) {
            int leftover = 0;
            
            int x = this.eatingPlant.x_pos;
            int y = this.eatingPlant.y_pos;
            // Make dx,dy a vector pointing towards the target
            double dx = x - x_pos;
            double dy = y - y_pos;

            // Make it a unit vector
            double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            if (dist < (int)moveRange)
            {
                leftover = (int)moveRange - (int)dist;
                x_pos = x;
                y_pos = y;
            }
            else
            {
                dx /= dist;
                dy /= dist;

                dx *= (int)moveRange;
                dy *= (int)moveRange;

                x_pos += dx;
                y_pos += dy;
            }
            
            int moved = (int)moveRange - leftover;
            
            this.spendEnergy((moved/5)*this.outputRate);
            
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

        Actor closest = getClosestActor(plants, rocks);

        if (closest != null) {
            this.eatingPlant = (PlantLife)closest;
            this.moveToFood(plants, rocks, herbivores);
            return;
        }
        
        Random rn = new Random();
        // Select a random x between 1 and max speed
        int x = rn.nextInt((int)moveRange) + 1;
        int y = (int)moveRange - x;
        
        double dirX = rn.nextInt();
        if (dirX < 0.5) {
            // If 0, move to the left
            this.set_x_pos(this.get_x_pos() - x);
        }
        else {
            this.set_x_pos(this.get_x_pos() + x);
        }
        
        double dirY = rn.nextInt();
        if (dirY < 0.5) {
            this.set_y_pos(this.get_y_pos() - y);
        }
        else {
            this.set_y_pos(this.get_y_pos() + y);
        }
        
        // For every 5 DU used, lose energy at the output rate
        this.spendEnergy(this.outputRate * ((int)moveRange/5));
    }
    
    private Actor getClosestActor(List<? extends Actor> actors, List<Rock> rocks) 
    {
        List<Actor> inRange = actorsInRange(actors);
        
        if (inRange.size() > 0) 
        {
            List<Actor> visible = visibleActors(inRange, rocks);
            
            if (visible.size() > 0) 
            {
                int minDistance = 1000;
                int minIndex = 0;
                int index = 0;

                while (index < actors.size()) {
                    int diffx = this.get_x_pos() - actors.get(index).get_x_pos();
                    int diffy = this.get_y_pos() - actors.get(index).get_y_pos();
                    int distance = Math.abs(diffx) + Math.abs(diffy);

                    if (distance < minDistance) {
                        minDistance = distance;
                        minIndex = index;
                    }

                    index++;
                }

                return actors.get(minIndex);
            }
        }
        
        return null;
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
    
    private List<Actor> actorsInRange(List<? extends Actor> actors) {
        List<Actor> inRange = new ArrayList<>();
        
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
        
        // Loook at all actors and determine which are in range for movement
        for(Actor actor : actors)
        {
            // For now, just look at the actors center
            if ((actor.get_x_pos() >= minX && actor.get_x_pos() <= maxX) &&
                (actor.get_y_pos() >= minY && actor.get_y_pos() <= maxY)) {
                inRange.add(actor);
            }
        }
        
        return inRange;
    }
    
    private List<Actor> visibleActors(List<Actor> actors, List<Rock> rocks) 
    {
        List<Actor> visible = new ArrayList<>();
        
        for (Actor actor : actors) {  
            boolean isVisible = true;
            boolean right = false;
            boolean up = false;
            boolean xEqual = false;
            boolean yEqual = false;
            
            int x = this.get_x_pos();
            int y = this.get_y_pos();
            
            if (actor.get_x_pos() > x) {
                right = true;
            }
            else if (actor.get_x_pos() == x) {
                xEqual = true;
            }
           
            if (actor.get_y_pos() < y) {
                up = true;
            }
            else if (actor.get_y_pos() == y) {
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
                
                if (x != actor.get_x_pos() && y != actor.get_y_pos()) {
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
                visible.add(actor);
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
