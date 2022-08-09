package cscd212classes.lab3.entities;

import cscd212interface.lab3.HealthBar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 * The player often the hero to do things
 * @see Agent
 * @see HealthBar
 */
public abstract class Player extends Agent implements HealthBar {

    /**
     * The current of the player
     */
    protected int currentLifePoints;

    /**
     * The max heath the player can have
     */
    public static final int MAX_LIFE_POINTS = 100;

    /**
     * Making a player
     * @param name making the player
     * @param currentLifePoints the current heath of the player
     */
    public Player(final String name, final int currentLifePoints) {
        super(name);

        // Pre-check
        if (currentLifePoints < 0 || currentLifePoints > MAX_LIFE_POINTS) {
            throw new IllegalArgumentException("bad prams in Player constructor");
        }

        this.currentLifePoints = currentLifePoints;
    }

    /**
     * Get amount of health the enemy has
     * @return The amount of health the enemy has
     */

    public int getHealth(){
        return this.currentLifePoints;
    }

    /**
     * Taking damage and letting anyone the look at know there dead
     * @NOTE There is a missing lines in this code you need to write when your start
     * @param damage the damage that the Player takes
     * @see java.beans.PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    public void takeDamage(final int damage) {
        if (damage < 0 || damage > this.currentLifePoints) {
            throw new IllegalArgumentException("bad prams takeDamage in Player");
        }
        int oldLifePoints = this.currentLifePoints;
        this.currentLifePoints -= damage;
        if (this.currentLifePoints == 0) {
            pcs.firePropertyChange("dead", oldLifePoints, new int[]{this.getX(), this.getY()});
        }
    }

    /**
     * The place to know when something happen
     * <br> One event it looks for is if it took some damage (lest then 3 away)
     * @NOTE to get distance in java Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
     * @param evt the event that happen
     * @see PropertyChangeEvent#getPropertyName()
     * @see PropertyChangeEvent#getNewValue()
     */
    /* TODO: fix */
        public void propertyChange(final PropertyChangeEvent evt){
            String str = evt.getPropertyName();
                if(str.equalsIgnoreCase("take damage")){
                    int[] data = (int[]) evt.getNewValue();
                    double z = Math.sqrt(Math.pow((this.getX() - data[1]),2) + Math.pow((this.getY() - data[2]), 2));
                    if(z < 3){
                        this.takeDamage(data[0]);
                    }
            }
        }


    /**
     * Able to call there special ability
     */
    public abstract void useSpecialAbility();

    /**
     * Getting the status of the Player
     * @return super.toString() + there health
     */
    @Override
    public String toString() {
        return super.toString() + " also is at " + this.getHealth() + "HP";
    }
}
