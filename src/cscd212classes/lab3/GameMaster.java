package cscd212classes.lab3;

import cscd212classes.lab2.Canvas;
import cscd212classes.lab2.Color;
import cscd212classes.lab3.entities.Agent;
import cscd212interface.lab3.HealthBar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.NoSuchElementException;

/**
 * The master that watches everyone on what is happening
 * @see PropertyChangeListener
 */
public final class GameMaster implements PropertyChangeListener {

    /**
     * Let other watch master so mater can give updates
     * @see PropertyChangeSupport
     */

    private final PropertyChangeSupport pcs;
    /**
     * The current map
     */
    private final Canvas map;

    /**
     * Only let one game master exist in the program
     */
    private static GameMaster gameMaster;

    /**
     * Making the gameMaster
     * @NOTE PropertyChangeSupport SourceBean is this
     * @see GameMaster#pcs
     * @see PropertyChangeSupport
     */



    private GameMaster() {
        makeAllColors();
        this.map = new Canvas(10, 10);
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * Get the game master
     * @return The current gameMaster
     */
    public static GameMaster getGameMaster() {
        if (gameMaster == null) {
            gameMaster = new GameMaster();
        }
        return gameMaster;
    }

    /**
     * Making all the color that the lib has
     */
    private static void makeAllColors() {
        new Color("Black", new short[]{0, 0, 0, 255});
        new Color("Red", new short[]{255, 0, 0, 255});
        new Color("Green", new short[]{0, 255, 0, 255});
        new Color("Yellow", new short[]{255, 255, 0, 255});
        new Color("Blue", new short[]{0, 0, 255, 255});
        new Color("Purple", new short[]{128, 0, 128, 255});
        new Color("Cyan", new short[]{0, 255, 255, 255});
        new Color("White", new short[]{255, 255, 255, 255});
    }

    /**
     * Getting the color without repeat code in many classes
     * @param colorName the name of the color you want
     * @return the color
     * @throws NoSuchElementException when the color does not exist
     */
    public static Color getColor(final String colorName) {
        for (Color color : Color.getAllColors()) {
            if (color.getName().equalsIgnoreCase(colorName)) {
                return color;
            }
        }
        throw new NoSuchElementException(colorName + " does not exist");
    }

    /**
     * To know watch what is happening
     * @param evt The event that happen
     * @see PropertyChangeEvent#getPropertyName()
     * @see String#equalsIgnoreCase(String)
     * @see GameMaster#areaAttack(PropertyChangeEvent)
     * @see GameMaster#updateLocation(PropertyChangeEvent)
     * @see GameMaster#updateColor(PropertyChangeEvent)
     * @see GameMaster#updateDeath(PropertyChangeEvent)
     */

    public void propertyChange(final PropertyChangeEvent evt) {
        String str = evt.getPropertyName();
        if(str.equalsIgnoreCase("area attack"))
            this.areaAttack(evt);

        if(str.equalsIgnoreCase("location"))
            this.updateLocation(evt);

        if(str.equalsIgnoreCase("color change"))
            this.updateColor(evt);

        if(str.equalsIgnoreCase("dead"))
            this.updateDeath(evt);



    }

    /**
     * Update the color in the display
     * @param evt the event that happen
     * @throws IllegalStateException when color that was reset does not match old color
     * @see PropertyChangeEvent#getSource()
     * @see PropertyChangeEvent#getOldValue()
     * @see PropertyChangeEvent#getNewValue()
     */
    private void updateColor(final PropertyChangeEvent evt) {
        Agent a  = (Agent) evt.getSource();
        int x = a.getX();
        int y = a.getY();
        Color color = (Color) evt.getNewValue();
        if (!this.map.resetColor(x, y).equals(evt.getOldValue())) {
            throw new IllegalStateException("Color does not match original color");
        }
        this.map.setColor(x,y,color);
    }

    /**
     * When a death happens
     * <br> This updates the map to remove the agent
     * @param evt the event that happen
     * @see PropertyChangeEvent#getNewValue()
     * @see PropertyChangeEvent#getSource()
     * @see PropertyChangeListener
     */
    private void updateDeath(final PropertyChangeEvent evt) {
        int[] xAndY = (int[]) evt.getNewValue();
        Agent a = (Agent) evt.getSource();

        this.map.resetColor(xAndY[0], xAndY[1]);

        a.removePropertyChangeListener((gameMaster));
        if(a instanceof HealthBar)
            this.removePropertyChangeListener((PropertyChangeListener) evt.getSource());

    }

    /**
     * When an agent change location
     * <br> update the location on the map
     * @param evt the event that happen
     * @see PropertyChangeEvent#getSource()
     * @see PropertyChangeEvent#getOldValue()
     * @see PropertyChangeEvent#getNewValue()
     * @see Canvas#resetColor(int, int)
     * @see Canvas#setColor(int, int, Color)
     * @see Agent#getColor()
     */
    private void updateLocation(final PropertyChangeEvent evt) {
        Agent agent = (Agent) evt.getSource();
        int[] oldXAndY = (int[]) evt.getOldValue();
        int[] xAndY = (int[]) evt.getNewValue();
        this.map.resetColor(oldXAndY[0], oldXAndY[1]);
        this.map.setColor(xAndY[0], xAndY[1], agent.getColor());
    }

    /**
     * When an area attack happen
     * <br> let everyone watching know that they may need to take damage
     * @param evt the event that happen
     * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
     * @see GameMaster#pcs
     * @see PropertyChangeEvent#getSource()
     * @see PropertyChangeEvent#getNewValue()
     * @see Agent#getX()
     * @see Agent#getY()
     */
    private void areaAttack(final PropertyChangeEvent evt) {
        Agent agent = (Agent) evt.getSource();
        int[] dam = new int [] {(int) evt.getNewValue(), agent.getX(), agent.getY()};
        this.pcs.firePropertyChange("take damage", 0, dam);
    }

    /**
     * Add an object that will watch this object
     * @param pcl the watcher
     * @throws IllegalArgumentException when given object is null
     * @see PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
     */

    public void addPropertyChangeListener(final PropertyChangeListener pcl){
        if(pcl == null){
            throw new IllegalArgumentException("given object is null");
        }
        this.pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Removes an object that was watching this object
     * @param pcl the watcher
     * @throws IllegalArgumentException when given object is null
     * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     */

    public void removePropertyChangeListener(final PropertyChangeListener pcl){
        if(pcl == null){
            throw new IllegalArgumentException("given object is null");
        }
       this.pcs.removePropertyChangeListener(pcl);
    }
    /**
     * The current map
     * @return getting the map
     */
    public Canvas getMap() {
        return this.map;
    }

}
