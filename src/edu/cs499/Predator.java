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
 * 03-10-17  AGA  Finished implementing basic logic and movement.
 *
 ******************************************************************************/
package edu.cs499;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;  // used in wander()

public class Predator extends Actor {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    
    /** How much energy this predator has. */
    private int energy;
    
    /** Whether or not this predator is alive. */
    private boolean alive;
    
    /** Whether or not this predator is seeking a mate. */
    private boolean needsMate;
    
    /** Whether or not this predator is pregnant. */
    private boolean isPregnent;
    
    /** Whether or not this predator has reproduced. */
    private boolean hadBabies;
    
    /** Keeps track of how long this predator has been pregnant for. */
    private int gestationCounter;
    
    /**
     * Destination to move towards when wandering. Used by the wander()
     * method.
     */
    private Point wanderTarget;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Const. Parameters">
    
    /**
     * How quickly this predator consumes energy when moving, in energy units
     * per distance unit.
     */
    private final double energyOutputRate;
    
    /** Maximum speed of this predator. */
    private final double maxSpeed;
    
    /** Speed this predator can currently move at. */
    private double currentSpeed;
    
    /** How long this predator can move at maximum speed. */
    private final double maxSprintTime;
    
    /** How long this predator has been sprinting for. */
    private double sprintTime;
    
    /**How much energy this predator needs before it will try to find a mate. */
    private final int energyToReproduce;
    
    /** Maximum number of offspring this predator can have. */
    private final int maxOffspring;
    
    /** How long the predator must be pregnent before it can give birth. */
    private final double gestationPeriod;
    
    /** How much energy each baby predator gets. */
    private final int offspringEnergy;
    
        
    /** Animals beyond this distance are not visible. */
    private static final int MAX_VISIBILITY_DISTANCE = 150;
    
    /**
     * Animals within this distance can be smelled, even if they cannot be seen.
     */
    private static final int MAX_SMELL_DISTANCE = 25;
    
    /**
     * If the distance to another animal is less than this, it is close enough
     * to mate with/attack.
     */
    private static final int TOUCHING_DISTANCE = 3;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Genotype stuff">
    
    /**
     * Bitmask of gene values. On = dominant, off = recessive. Examples:
     *  - `genotype & AGGRESSION`: Returns just the aggression bits
     *  - `(genotype & AGGRESSION) == (AGR_1 | AGR_2)`: True if this is a
     *    homozygous dominant (AA) predator
     *  - `genotype |= AGR_1`: Make 1st aggression gene dominant (Ax)
     *  - `genotype ^= AGR_2`: Make 2nd aggression gene recessive (xa)
     */
    private int genotype;
    
    /** First aggression gene bit (A-). */
    public static final int AGR_1 = 0x02;
    /** Second aggression gene bit (-A). */
    public static final int AGR_2 = 0x01;
    
    /** Mask for the aggression bits (e.g. 0000 0011). */
    public static final int AGGRESSION = AGR_1 | AGR_2;
    
    /** First strength gene bit (S-). */
    public static final int STR_1 = 0x08;
    /** Second  strength gene bit (-S). */
    public static final int STR_2 = 0x04;
    
    /** Mask for the strength bits (e.g. 0000 1100). */
    public static final int STRENGTH = STR_1 | STR_2;
    
    /** First speed gene bit (F-). */
    public static final int SPD_1 = 0x20;
    /** Second speed gene bit (-F). */
    public static final int SPD_2 = 0x10;
    
    /** Mask for the speed bits (e.g. 0011 0000). */
    public static final int SPEED = SPD_1 | SPD_2;
    
    // </editor-fold>
    
    /**
     * Converts a genotype string into bitmask values.
     * @param genotype e.g. "Aa SS ff"
     * @return A bitmask of genes.
     */
    public static int parse_genotype(String genotype)
    {
        // TODO: error handling for missing genes, duplicate genes, and
        // unknown genes
        String[] parts = genotype.split(" ");
        int result = 0;
        for (String g : parts)
        {
            switch (g)
            {
            case "AA":
                result |= (Predator.AGR_1 | Predator.AGR_2);
                break;
            case "Aa":
                result |= Predator.AGR_1;
                break;
            case "aA":
                result |= Predator.AGR_2;
                break;
                
            case "SS":
                result |= (Predator.STR_1 | Predator.STR_2);
                break;
            case "Ss":
                result |= Predator.STR_1;
                break;
            case "sS":
                result |= Predator.STR_2;
                break;
                
            case "FF":
                result |= (Predator.SPD_1 | Predator.SPD_2);
                break;
            case "Ff":
                result |= Predator.SPD_1;
                break;
            case "fF":
                result |= Predator.SPD_2;
                break;
            default:
                // Don’t need to do anything for aa, ss, or ff because all of
                // the bits are initialized to 0.
            }
        }
        
        return result;
    }
    
    /**
     * Creates a new predator.
     * @param init_x_pos Horizontal position
     * @param init_y_pos Vertical position
     * @param init_energy Initial energy
     * @param energy_output_rate In energy units per distance unit
     * @param init_genotype A bitmask of genes
     * @param maintain_speed_time How long the predator can sprint for
     * @param max_speed The predator’s top speed
     * @param energy_to_reproduce How much energy the predator needs before it
     *     will attempt to reproduce
     * @param max_offspring Maximum number of babies the predator can have
     * @param gestation_period How long the predator must be pregnent for before
     *     it can give birth
     * @param offspring_energy How much energy the baby predators have
     */
    public Predator(int init_x_pos, int init_y_pos, int init_energy,
            double energy_output_rate,
            int init_genotype, double maintain_speed_time, double max_speed,
            int energy_to_reproduce, int max_offspring, double gestation_period,
            int offspring_energy)
    {
        // use the x and y position with the Actor constructor
        super(init_x_pos, init_y_pos);
        
        energy = init_energy;
        alive = (init_energy > 0);
        needsMate = false;
        isPregnent = false;
        hadBabies = false;
        wanderTarget = null;
        
        genotype = init_genotype;
        gestationCounter = 0;
        
        energyOutputRate = energy_output_rate;
        currentSpeed = maxSpeed = max_speed;
        sprintTime = maxSprintTime = maintain_speed_time;
        
        energyToReproduce = energy_to_reproduce;
        maxOffspring = max_offspring;
        gestationPeriod = gestation_period;
        offspringEnergy = offspring_energy;
    } // End Predator()
    
    /**
     * Gets the current energy of the predator.
     * @return Current energy level
     */
    public int get_energy()
    {
        return this.energy;
        
    }
    
    /**
     * Gets the genotype of the predator.
     * @return A bitmask of gene values.
     */
    public int get_genotype()
    {
        return this.genotype;
        
    }

    /**
     * Sets the energy level of the predator.
     * @param new_energy New energy level
     */
    public void set_energy(int new_energy)
    {
        this.energy = new_energy;
        
    }
    
    /**
     * Sets the genotype of the predator.
     * @param new_genotype Bitmask of gene values.
     */
    public void set_genotype(int new_genotype)
    {
        this.genotype = new_genotype;
        
    }
    
    /**
     * Measures the distance from this predator to a given point.
     * @param x Target X position
     * @param y Target Y position
     * @return Distance from this predator to (x, y)
     */
    private double distance_to(int x, int y)
    {
        return Math.sqrt(Math.pow(x_pos - x, 2) + Math.pow(y_pos - y, 2));
    }
    
    /**
     * Determines the distance to another animal.
     * @param other Some other animal
     * @return The distance from this predator to that animal.
     */
    private double distance_to(Actor other)
    {
        return distance_to(other.get_x_pos(), other.get_y_pos());
    }
    
    /**
     * Finds the nearest animal (excluding self).
     * @param animals A list of all animals. It should not be empty.
     * @return The closest animal from the list.
     */
    private Actor nearest(List<? extends Actor> animals)
    {
        Actor nearest = animals.get(0);
        double dist = distance_to(nearest);
        for (Actor steve : animals)
        {
            if (steve == this)  continue;
            
            // Is Steve the nearest animal?
            double d = distance_to(steve);
            if (d < dist)
            {
                dist = d;
                nearest = steve;
            }
        }
        
        return nearest;
    }
    
    /**
     * The main update function for this predator.
     * @param predators A list of all predators
     * @param rocks A list of all rocks
     * @param herbivores A list of all herbivores
     * @param worldWidth Width of the world (used when wandering)
     * @param worldHeight Height of the world (used when wandering)
     */
    public void think(List<Predator> predators, List<Rock> rocks,
        List<Herbivore> herbivores, int worldWidth, int worldHeight)
    {
        if (!alive)  return;  // dead
        
        if (energy > energyToReproduce && !hadBabies && !isPregnent)
        {
            needsMate = true;
        }
        
        // Find the nearest animal
        Herbivore nearestHerbivore = (Herbivore)nearest(herbivores);
        Predator nearestPredator = (Predator)nearest(predators);
        Actor nearest = null;
        if (distance_to(nearestPredator) < distance_to(nearestHerbivore))
        {
            nearest = nearestPredator;
        }
        else
        {
            nearest = nearestHerbivore;
        }
        
        // Main logic:
        if (!visible(nearest, rocks))  // all alone…
        {
            wander(worldWidth, worldHeight);
        }
        else if (nearest == nearestHerbivore)  // found food!
        {
            hunt(nearest);
        }
        else  // nearest animal is another predator
        {
            if (needsMate)
            {
                mate(nearestPredator);
            }
            else
            {
                switch (genotype & AGGRESSION)
                {
                case AGR_1 & AGR_2:  // AA: attack it
                    hunt(nearest);
                    break;
                case AGR_1:  // Aa or aA: ignore it
                case AGR_2:
                    // find nearest prey
                    if (visible(nearestHerbivore, rocks))
                    {
                        hunt(nearestHerbivore);
                    }
                    else
                    {
                        wander(worldWidth, worldHeight);
                    }
                    break;
                default:  // aa: flee
                    flee(nearestPredator);
                }
            }
        }
        
        if (isPregnent)
        {
            if (++gestationCounter > gestationPeriod)
            {
                give_birth();
            }
        }
    }
    
    /**
     * Use some energy, and possibly die.
     * @param amount How much energy to use
     */
    private void spendEnergy(int amount) {
        this.energy -= amount;
        
        if (this.energy <= 0) {
            this.alive = false;
        }
    }
    
    /**
     * Determines if this predator can see a given animal.
     * @param target The animal whose visibility should be checked.
     * @param obstacles A list of all of the rocks (some might be obscuring the
     *     predator’s view of the target.)
     */
    private boolean visible(Actor target, List<Rock> obstacles)
    {
        double dist = distance_to(target);
        if (target == this || dist > MAX_VISIBILITY_DISTANCE)
        {
            // It is too far away, or it is us (there are no mirrors in this
            // simulation.)
            return false;
        }
        else if (dist < MAX_SMELL_DISTANCE)
        {
            return true;
        }
        else
        {
            return true;  // TODO: proper line-of-sight checking
        }
    }
    
    /**
     * Move towards the given point. Energy will be consumed, and the predator
     * may die if it runs out.
     * @param x Target x position
     * @param y Target y position
     * @see spendEnergy
     */
    private void move_toward(int x, int y)
    {
        if (currentSpeed == 0)
        {
            // TODO: not sure what to do here. For now, we rest for half the
            // time we ran
            sprintTime -= 2;
            if (sprintTime == 0)
            {
                currentSpeed = maxSpeed;
            }
            return;
        }
        // Make dx,dy a vector pointing towards the target
        double dx = x - x_pos;
        double dy = y - y_pos;
        
        // Make it a unit vector
        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        if (dist < currentSpeed)
        {
            x_pos = x;
            y_pos = y;
        }
        else
        {
            dx /= dist;
            dy /= dist;

            dx *= currentSpeed;
            dy *= currentSpeed;

            x_pos += dx;
            y_pos += dy;
        }
        // TODO: collision checking (do not move into rocks)
        
        spendEnergy((int)(currentSpeed * energyOutputRate));
        
        ++sprintTime;
        if (sprintTime > maxSprintTime)
        {
            // Decrement current speed every 15 seconds
            if ((sprintTime - maxSprintTime) % 15 == 0)
            {
                --currentSpeed;
            }
        }
    }
    
    /**
     * Move towards the given target.
     * @param target The actor to move towards.
     */
    private void move_toward(Actor target)
    {
        move_toward(target.get_x_pos(), target.get_y_pos());
    }
    
    /**
     * Move in a random direction to look for food or a mate.
     * @param width Width of the world
     * @param height Height of the world
     */
    private void wander(int width, int height)
    {
        if (wanderTarget == null ||
            distance_to(wanderTarget.x, wanderTarget.y) < TOUCHING_DISTANCE)
        {
            if (wanderTarget == null)
            {
                wanderTarget = new Point();
            }
            wanderTarget.x = ThreadLocalRandom.current().nextInt(0, width+1);
            wanderTarget.y = ThreadLocalRandom.current().nextInt(0, height+1);
        }
        
        move_toward((int)wanderTarget.x, (int)wanderTarget.y);
    }
    
    /**
     * Persue or attack the given target.
     * @param target The animal to hunt
     */
    private void hunt(Actor target)
    {
        if (distance_to(target) < TOUCHING_DISTANCE)
        {
            attack(target);
        }
        else
        {
            move_toward(target);
        }
    }
    
    /**
     * Attack the given animal. Either it or this predator may die.
     * @param target Animal to attack
     */
    private void attack(Actor target)
    {
        // TODO: implement attack()
    }
    
    /**
     * Persue or mate with the given target.
     * @param target The predator that this predator wants to mate with.
     */
    private void mate(Predator target)
    {
        if (distance_to(target) < TOUCHING_DISTANCE)
        {
            reproduce_with(target);
        }
        else
        {
            move_toward(target);
        }
    }
    
    /**
     * Gets this predator pregnent.
     * @param target This predator’s partner
     */
    private void reproduce_with(Predator target)
    {
        // TODO: implement reproduce
    }
    
    /**
     * Creates baby predators.
     */
    private void give_birth()
    {
        isPregnent = false;
        needsMate = false;
        hadBabies = true;
        
        // TODO: create babies
    }
    
    /**
     * Run away from another predator.
     * @param hunter The predator that is scaring this one.
     */
    private void flee(Predator hunter)
    {
        // If the hunter is e.g. 2 units to the left and 3 units up, set target
        // to be 2 units to the right and 3 units down.
        int dx = x_pos - hunter.get_x_pos();
        int dy = y_pos - hunter.get_y_pos();
        int x = x_pos + dx;
        int y = y_pos + dy;
        move_toward(x, y);
    }
    
} // End Predator class
