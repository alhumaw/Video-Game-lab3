package cscd212classes.lab3.entities;

import cscd212classes.lab2.Color;
import cscd212classes.lab3.GameMaster;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The agent that players, enemies and npc uses
 */
public abstract class Agent {

    // ### Filled's ###

    /**
     * The String representing this Agent's name.
     * @NOTE We are using default so that classes that extend this class
     */
    protected String name;

    /**
     * so other can watch this agent
     * @see PropertyChangeSupport
     */
     protected PropertyChangeSupport pcs;


    /**
     * The y location on the map
     */
    private int x;

    /**
     * The x location on the map
     */
    private int y;

    /**
     * Move to x
     */
    private int moveToX;

    /**
     * Move to y
     */
    private int moveToY;

    /**
     * The color of the agent on the map
     */
    private Color color;

    // ### Constructor's ###

    /**
     * Making the agent
     * @param name the name of the agent
     */
    public Agent(final String name) {
        this(name, 0, 0);
    }

    /**
     * Making the agent <br>
     * Note: PropertyChangeSupport SourceBean is this
     * @param name the name of the agent
     * @param x the stating location
     * @param y the stating location
     * @see PropertyChangeSupport
     */
    public Agent(final String name, final int x, final int y) {
        if (name == null || name.isBlank() || x < 0 || y < 0) {
            throw new IllegalArgumentException("bad prams in Agent Consulter");
        }

        this.name = name;
        this.x = x;
        this.y = y;
        this.moveToX = x;
        this.moveToY = y;
        this.color = GameMaster.getColor("White");
        this.pcs = new PropertyChangeSupport(this);
    }

    // ### Getters and Setters ###

    /**
     * The name of the agent
     * @return string name
     */
    public String getName() {
        return this.name;
    }

    /**
     * The agent x on the map
     * @return x
     */
    public int getX() {
        return this.x;
    }

    /**
     * The agent y on the map
     * @return y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Getting the color of the agent as there will appear as on the map
     * @return the color of the agent
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Set the color of the agent
     * @param color the new color for the agent
     * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    protected void setColor(final Color color) {
        Color oldColor = this.color;
        this.color = color;
        this.pcs.firePropertyChange("color change", oldColor, color);

    }
    /**
     * Updating the location and going to location of the agent
     * @param x new x location
     */
    public void setX(final int x) {
        this.moveToX = x;
        this.updateX(x);
    }

    /**
     * new location of the agent
     * @param newX the x where the agent at
     * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    private void updateX(final int newX) {
        int oldX = this.x;
        this.x = newX;
        int[] pX = new int[] {oldX, this.getY()};
        int[] cX = new int[] {newX, this.getY()};
        this.pcs.firePropertyChange("location", pX, cX);
    }

    /**
     * Updating the location and going to location of the agent
     * @param y new y location
     */
    public void setY(final int y) {
        this.moveToY = y;
        this.updateY(y);
    }

    /**
     * new location of the agent
     * @param newY the y where the agent at
     * @see PropertyChangeSupport#firePropertyChange(String, Object, Object)
     */
    private void updateY(final int newY) {
        int oldY = this.y;
        this.y = newY;
        int[] pY = new int[] {this.getX(), oldY};
        int[] cY = new int[] {this.getX(), newY};
        this.pcs.firePropertyChange("location", pY, cY);
    }

    /**
     * Updating the moveto x and y
     * @param newX the x place agent going to
     * @param newY the y place agent going to
     */
    public void move(final int newX, final int newY) {
        if (newX < 0 && newY < 0) {
            throw new IllegalArgumentException();
        }
        this.moveToX = newX;
        this.moveToY = newY;
    }

    // ## Methods ##

    // # Property Change Require Methods #

    /**
     * Add an object that will watch this object
     * @param pcl the watcher
     * @throws IllegalArgumentException when object is null
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
     * @throws IllegalArgumentException when object is null
     * @see PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
     */

    public void removePropertyChangeListener(final PropertyChangeListener pcl){
        if(pcl == null){
            throw new IllegalArgumentException("given object is null");
        }
        this.pcs.removePropertyChangeListener(pcl);
    }
    /**
     * When the agent need to be updated there things
     * <br> Example when one need to move because the player moved
     */
    public void upDate() {
        if (this.x != this.moveToX) {
            if (this.x > this.moveToX) {
                this.updateX(this.x - 1);
            } else {
                this.updateX(this.x + 1);
            }

        }
        if (this.y != this.moveToY) {
            if (this.y > this.moveToY) {
                this.updateY(this.y - 1);
            } else {
                this.updateY(this.y + 1);
            }
        }
    }

    /**
     * Getting the status of the agent
     * @return a string repeating the status of the agent.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append(" is at (");
        stringBuilder.append(this.x);
        stringBuilder.append(",");
        stringBuilder.append(this.y);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }


}
