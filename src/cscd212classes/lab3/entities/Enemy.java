package cscd212classes.lab3.entities;

import cscd212interface.lab3.HealthBar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 * Enemy for the hero? to kill
 * @see Agent
 * @see HealthBar
 */
public abstract class Enemy extends Agent implements HealthBar {

    /**
     * Most enemy do not have their own name
     */
    private static int enemyNumber = 1;

    /**
     * Perceived amount of health this enemy has
     */
    private int showLifePoints;

    /**
     * Making an enemy
     * @param enemyType A string reparteeing the type of enemy
     * @param lifePoints amount of lifePints the enemy has
     */
    public Enemy(final String enemyType, final int lifePoints) {
        super(enemyType + " #" + enemyNumber);
        if (lifePoints < 0) {
            throw new IllegalArgumentException("Bad Prams Enemy Constructor");
        }

        enemyNumber++;
        this.showLifePoints = lifePoints;
    }

    /**
     * The number of enemy there is
     * @return how many enemy there is
     */
    public static int getEnemyNumber() {
        return enemyNumber;
    }

    /**
     * Get amount of health the enemy has
     * @return The amount of health the enemy has
     */
    public int getHealth(){
       return this.showLifePoints;

    }

    /**
     * Taking damage and letting anyone the look at know there dead
     * @NOTE There is a missing line in this code you need to write when your start
     * @param damage the damage that the Enemy takes
     * @see java.beans.PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    @Override
    public void takeDamage(final int damage) {
        if (damage < 0 || this.showLifePoints == 0) {
            throw new IllegalArgumentException("bad prams enemy take hit");
        }
        if (damage >= this.showLifePoints) {
            this.showLifePoints = 0;
            enemyNumber--;
            pcs.firePropertyChange("dead", 0, new int[]{this.getX(), this.getY()});
        } else {
            this.showLifePoints -= damage;
        }
    }

    /**
     * Getting the status of the enemy
     * @return super.toString() + there health
     */
    @Override
    public String toString() {
        return super.toString() + " also is at " + this.getHealth() + "HP";
    }

    /**
     * The place to know when something happen
     * <br> One event it looks for is if it took some damage (lest then 4 away)
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
            if(z < 4){
                this.takeDamage(data[0]);
            }
        }
    }
}
