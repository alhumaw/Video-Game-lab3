package cscd212classes.lab3.entities.characters;

import java.beans.PropertyChangeSupport;

import cscd212classes.lab3.GameMaster;
import cscd212classes.lab3.entities.Player;

/**
 * Simple Hero with only one ability
 * <br> Normal has 95 health
 * <br> Normal is Blue
 */
public class SimpleHero extends Player {

    private int timePassAfterUsingAbility;
    
    /**
     * Making a Hero
     * @param name The name of the Hero
     */
    public SimpleHero(final String name) {
        super(name, 95);
        setColor(GameMaster.getColor("Blue"));
        this.timePassAfterUsingAbility = 0;

    }

    @Override
    public void upDate() {
        super.upDate();
        this.timePassAfterUsingAbility++;
        if (this.timePassAfterUsingAbility > 3) {
            setColor(GameMaster.getColor("Blue"));
        }
    }

    /**
     * SpecialAbility this hero has
     * <br> attack everyone around them (including them self)
     * <br> also does a quick heal (+10hp) before the attack.
     * <br> it also change there color to purple
     * (Hinting that the hero need to learn some other attacks before he gets fully cursed by his ability)
     * @see java.beans.PropertyChangeSupport#firePropertyChange(String, int, int)
     */
    @Override
    public void useSpecialAbility() {
        this.currentLifePoints = Math.min(this.currentLifePoints + 10, Player.MAX_LIFE_POINTS);
        setColor(GameMaster.getColor("Purple"));
        this.timePassAfterUsingAbility = 0;
       
        pcs.firePropertyChange("area attack",0,15);
    }
}
