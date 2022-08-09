package cscd212interface.lab3;

import java.beans.PropertyChangeListener;

/**
 * Give others the ability to access ones HealthBar
 * @see PropertyChangeListener
 */
public interface HealthBar extends PropertyChangeListener {

    /**
     * The current health
     * @return The current persave health
     */
    public abstract int getHealth();

    /**
     * Make so the Agent that has a HealthBar takes damage
     * @param damage the damage that the Agent takes
     */
    public abstract void takeDamage(final int damage);
}

