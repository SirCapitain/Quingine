package quingine.util;

import java.awt.*;

/**
 * Holds values of a pixel on the screen.
 * This also takes a z-value to make z-buffering possible.
 * @author Quinn Graham
 */

public class Quixel {
    /**
     * The best way to initializing a quixel.
     * @param x integer value on screen
     * @param y integer value on screen
     * @param z double value of position in 3D world.
     * @param color the color of the quixel.
     */
    public Quixel(int x, int y, double z, Color color){
        setPos(x,y,z);
        setColor(color);
    }

    /**
     * The second best way of making a quixel.
     * @param x integer value on screen
     * @param y integer value on screen
     * @param z double value of position in 3D world.
     */
    public Quixel(int x, int y, double z){
        setPos(x,y,z);
    }

    public int x, y;
    public double z;

    /**
     * Set the position of the quixel.
     * @param x integer value on screen
     * @param y integer value on screen
     * @param z double value of position in 3D world.
     */
    public void setPos(int x, int y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the current position of the quixel.
     * @return a list of the current position.
     */
    public double[] getPos(){
        return new double[]{x, y, z};
    }

    private Color color = Color.white;

    /**
     * Set the color of a specific quixel.
     * @param color color you want it to be.
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Get the current color a quixel is.
     * @return the current color.
     */
    public Color getColor(){
        return color;
    }
}
