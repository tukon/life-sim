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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;  // used in wander() & attack()

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
    
    /** The mate’s genes. Used when creating babies. */
    private int matesGenotype;
    
    /** Keeps track of how long this predator has been pregnant for. */
    private int gestationCounter;
    
    /**
     * Destination to move towards when wandering. Used by the wander()
     * method.
     */
    private Point wanderTarget;
    
    /**
     * Animals that this predator is ignoring.
     */
    private ArrayList<Actor> ignoredAnimals;
    
    /**
     * How long this predator has been ignoring each animal in ignoredAnimals,
     * in seconds.
     */
    private ArrayList<Integer> ignoredTime;
    
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
    
    /**
     * How long to ignore others for, in seconds.
     */
    private static final int IGNORE_TIME = 60 * 60;  // one hour
    
    /**
     * Gets the probability of a predator killing another predator based on
     * their strength genes.
     * @param hunter The hunter’s genes.
     * @param target The target’s genes.
     * @return A number between 0.0 and 1.0, or -1.0, which should be handled
     *     like so:
     *         • 50% chance of a draw
     *         • 25% chance to win
     *         • 25% chance to lose
     */
    private static float killChancePredator(int hunter, int target)
    {
        /*
         *     #     Target
         * self#  ss |  Ss |  SS
         * ----#=====≠=====≠=====
         *  ss #  ★  | 25% |  5% 
         * ----#-----+-----+-----
         *  Ss # 75% |  ★  | 25% 
         * ----#-----+-----+-----
         *  SS # 95% | 75% |  ★
         * 
         * ★: -1.0 is returned. These work like this:
         *     • 50% chance of a draw
         *     • 25% chance to win
         *     • 25% chance to lose
         */
        
        switch (hunter & STRENGTH)
        {
        case ~(Predator.STR_1 | Predator.STR_2): // ss
                switch (target & STRENGTH)
                {
                case ~(Predator.STR_1 | Predator.STR_2):  //ss
                    return -1.0f;
                case Predator.STR_1:  // Ss
                case Predator.STR_2:
                    return 0.25f;
                case (Predator.STR_1 | Predator.STR_2):  // SS
                    return 0.05f;
                }
            break;
        
        case Predator.STR_1: // Ss
        case Predator.STR_2:
                switch (target & STRENGTH)
                {
                case ~(Predator.STR_1 | Predator.STR_2):  //ss
                    return 0.75f;
                case Predator.STR_1:  // Ss
                case Predator.STR_2:
                    return -1.0f;
                case (Predator.STR_1 | Predator.STR_2):  // SS
                    return 0.25f;
                }
            break;
        
        case (Predator.STR_1 | Predator.STR_2): // SS
                switch (target & STRENGTH)
                {
                case ~(Predator.STR_1 | Predator.STR_2):  //ss
                    return 0.95f;
                case Predator.STR_1:  // Ss
                case Predator.STR_2:
                    return 0.75f;
                case (Predator.STR_1 | Predator.STR_2):  // SS
                    return -1.0f;
                }
            break;
        }
        
        // The above switches should handle all possibilities, but the compiler
        // doesn’t know that
        System.err.println("FIXME: Predator::winChance() broke!");
        System.err.printf("Inputs: %x, %x\n", hunter, target);
        return -1.0f;
    }
    
    /**
     * Gets the probability of a predator killing a herbivore, based on the
     * predator’s genes.
     * @param predator The predator’s genes.
     * @return A number between 0.0 and 1.0.
     */
    private static float killChanceHerbivore(int predator)
    {
        switch (predator & Predator.STRENGTH)
        {
        case Predator.STR_1:  // Ss
        case Predator.STR_2:
            return 0.75f;
        case (Predator.STR_1 & Predator.STR_2):
            return 0.95f;
        default:  // ss
            return 0.05f;
        }
    }
    
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
        // These are used to keep track of which genes have been set, so that we
        // can complain if some are duplicated or missing
        boolean setAgr = false;
        boolean setStr = false;
        boolean setSpd = false;
        
        String[] parts = genotype.split(" ");
        int result = 0;
        for (String g : parts)
        {
            switch (g)
            {
            case "aa":
                result ^= (Predator.AGR_1 | Predator.AGR_2);
                break;
            case "AA":
                result |= (Predator.AGR_1 | Predator.AGR_2);
                break;
            case "Aa":
                result |= Predator.AGR_1;
                break;
            case "aA":
                result |= Predator.AGR_2;
                break;
                
            case "ss":
                result ^= (Predator.STR_1 | Predator.STR_2);
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
                
            case "ff":
                result ^= (Predator.SPD_1 | Predator.SPD_2);
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
            default:  // Unknown gene
                // TODO: throw an exception?
                System.out.println("Warning: invalid gene \"" + g +
                    "\" given to this predator");
                System.out.println("Gene string: \"" + genotype + "\"");
            }
            
            // Check for duplicates
            switch (g)
            {
            case "aa":
            case "Aa":
            case "aA":
            case "AA":
                if (setAgr)
                {
                    System.out.println("Warning: multiple aggression values " +
                        "given for this predator; using the latest one");
                    System.out.println("Gene string: \"" + genotype + "\"");
                }
                else  setAgr = true;
                break;
                
            case "ss":
            case "Ss":
            case "sS":
            case "SS":
                if (setStr)
                {
                    System.out.println("Warning: multiple strength values " +
                        "given for this predator; using the latest one");
                    System.out.println("Gene string: \"" + genotype + "\"");
                }
                else  setStr = true;
                break;
    
            case "ff":
            case "Ff":
            case "fF":
            case "FF":
                if (setSpd)
                {
                    System.out.println("Warning: multiple speed values " +
                        "given for this predator; using the latest one");
                    System.out.println("Gene string: \"" + genotype + "\"");
                }
                else  setSpd = true;
                break;
            }
        }
        
        // Warn if some genes were never set (they will be regressive, e.g. xx)
        // TODO: exception?
        if (!setAgr || !setStr || !setSpd)
        {
            System.out.println("Warning: This predator is missing one or more "+
                "genes; they have been set to their regressive forms (xx)");
            System.out.println("Gene string: \"" + genotype + "\"");
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
        
        ignoredAnimals = new ArrayList<>();
        ignoredTime = new ArrayList<>();
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
     * Indicates whether or not this predator is alive.
     * @return True if it is alive, false if it is not.
     */
    public boolean is_alive()
    {
        return alive;
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
            if (ignoredAnimals.contains(steve))  continue;
            
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
            ++gestationCounter;
        }
        
        // Update the timers of ignored animals
        for (int ii = 0; ii < ignoredAnimals.size(); ++ii)
        {
            ignoredTime.set(ii, ignoredTime.get(ii) + 1);
            if (ignoredTime.get(ii) >= IGNORE_TIME)
            {
                ignoredAnimals.remove(ii);
                ignoredTime.remove(ii);
            }
        }
    }  // think()
    
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
     * Attack the given predator. Either it or this predator may die.
     * @param target Predator to attack
     */
    private void attack(Actor target)
    {
        float outcome = ThreadLocalRandom.current().nextFloat();
        if (target instanceof Predator)
        {
            float chance = killChancePredator(genotype,
                ((Predator)target).get_genotype());
            
            if (chance == -1.0)  // special case: see winChance
            {
                if (outcome < 0.5)  ignore(target);  // draw
                else if (outcome < 0.75)  eat(target);  // win
                else  die();  // lose
            }
            else if (outcome < chance)  eat(target);
            else  die();
        }
        else if (target instanceof Herbivore)
        {
            float chance = killChanceHerbivore(genotype);
            
            if (outcome < chance)  eat(target);
            else  ignore(target);// TODO: requirements don’t say what to do here
        }
    }
    
    /**
     * Eats the given animal, killing it and giving this preadator energy.
     * @param target The animal to kill & eat.
     */
    private void eat(Actor target)
    {
        // TODO: get_energy() and die() should probably be in Actor, so we don’t
        // have to do this silly typecasting stuff
        if (target instanceof Predator)
        {
            Predator p = (Predator)target;
            energy += p.get_energy() * 0.90;
            p.die();
        }
        else if (target instanceof Herbivore)
        {
            Herbivore h = (Herbivore)target;
            energy += h.get_energy() * 0.90;
            h.die();
        }
    }
    
    /** Kills this predator. */
    public void die()
    {
        energy = 0;
        alive = false;
    }
    
    /**
     * Ignore the given animal for some time.
     * @param target Animal to ignore.
     */
    public void ignore(Actor target)
    {
        // If we’re already ignoring this animal, do nothing
        for (Actor a : ignoredAnimals)
        {
            if (a == target)  return;
        }
        
        ignoredAnimals.add(target);
        ignoredTime.add(0);
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
        isPregnent = true;
        needsMate = false;
        gestationCounter = 0;
        matesGenotype = target.get_genotype();
    }
    
    /**
     * Creates baby predators.
     * @return The new baby predators, or null if not pregnent.
     */
    public ArrayList<Predator> give_birth()
    {
        if (!isPregnent)  return null;
        
        isPregnent = false;
        needsMate = false;
        hadBabies = true;
        
        ThreadLocalRandom r = ThreadLocalRandom.current();
        ArrayList<Predator> babies = new ArrayList<>();
        
        int totalBabies = r.nextInt(1, maxOffspring);
        for (int ii = 0; ii < totalBabies; ++ii)
        {
            Predator p = new Predator(x_pos + r.nextInt(-5, 5),
                y_pos + r.nextInt(-5, 5),
                offspringEnergy,
                energyOutputRate,
                get_baby_genes(),
                maxSprintTime,
                maxSpeed,
                energyToReproduce,
                maxOffspring,
                gestationPeriod,
                offspringEnergy);
            ignore(p);
            babies.add(p);
        }
        
        // Make the babies ignore each other
        for (Predator current : babies)
        {
            for (Predator other : babies)
            {
                current.ignore(other);
            }
        }
        
        return babies;
    }
    
    /**
     * Randomly creates genes from this predator’s genes and its mate’s. If
     * called before mating, this function’s behavior is undefined.
     * @return Genotype for a new baby.
     */
    private int get_baby_genes()
    {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int newGenotype = 0;
        
        /*
         * Process:
         *     1. Get a random boolean value.
         *     2. If it is true, inherit from parent.
         *     3. Otherwise, inherit from mate.
         * This is done for each of the six genes.
         */
        
        // AGR
        if (r.nextBoolean())  newGenotype |= (genotype & Predator.AGR_1);
        else  newGenotype |= (matesGenotype & Predator.AGR_1);
        if (r.nextBoolean())  newGenotype |= (genotype & Predator.AGR_2);
        else  newGenotype |= (matesGenotype & Predator.AGR_2);
        
        // STR
        if (r.nextBoolean())  newGenotype |= (genotype & Predator.STR_1);
        else  newGenotype |= (matesGenotype & Predator.STR_1);
        if (r.nextBoolean())  newGenotype |= (genotype & Predator.STR_2);
        else  newGenotype |= (matesGenotype & Predator.STR_2);
        
        // SPD
        if (r.nextBoolean())  newGenotype |= (genotype & Predator.SPD_1);
        else  newGenotype |= (matesGenotype & Predator.SPD_1);
        if (r.nextBoolean())  newGenotype |= (genotype & Predator.SPD_2);
        else  newGenotype |= (matesGenotype & Predator.SPD_2);
        
        return newGenotype;
    }
    
    
    /**
     * Whether or not this predator is ready to give birth.
     * @return If true, you should call give_birth().
     */
    public boolean ready_to_give_birth()
    {
        return (isPregnent && gestationCounter >= gestationPeriod);
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
