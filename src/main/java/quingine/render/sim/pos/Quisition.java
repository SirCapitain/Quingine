package quingine.render.sim.pos;

import quingine.render.sim.Math3D;

/**
 * A position in 3D space
 */

public class Quisition {

    public double x, y, z;

    public double[] data = null;

    /**
     * Not a fun way to make a point but it is a fast way.
     * Creates a point at (0, 0, 0)
     */
    public Quisition(){
        setPos(0,0,0);
    }

    /**
     * Set the position of a quisition using a list of numbers
     * @param pos list of doubles >= 3 in length
     */
    public Quisition(double[] pos){
        setPos(pos[0], pos[1], pos[2]);
    }

    /**
     * A good way of copying a quisition.
     * @param pos another position you want to copy.
     */
    public Quisition(Quisition pos){
        setPos(pos);
        if (pos.data != null)
            data = pos.data.clone();
    }


    /**
     * Create a new point and set where it is.
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     */
    public Quisition(double x, double y, double z){
        setPos(x, y, z);
    }

    /**
     * Create a new point and set where it is.
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     */
    public synchronized void setPos(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Set the position of a quisition using another quisition
     * This copies the info from one to the other
     * @param pos any quisition you want to copy over
     */
    public synchronized void setPos(Quisition pos){
        x = pos.x;
        y = pos.y;
        z = pos.z;
    }

    /**
     * Add two quisitions together.
     * @param pos another position you want to add.
     */
    public synchronized void add(Quisition pos){
        x += pos.x;
        y += pos.y;
        z += pos.z;
    }

    /**
     * Add a specific value to each parameter.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public synchronized void add(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Add a value to the position
     * @param value number to be added to the x, y, and z
     */
    public synchronized void add(double value){
        x += value;
        y += value;
        z += value;
    }

    /**
     * Subtract two quisitions.
     * @param pos another position you want to take from the current.
     */
    public synchronized void subtract(Quisition pos){
        x -= pos.x;
        y -= pos.y;
        z -= pos.z;
    }

    /**
     * Subtract a specific value from each component;
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public synchronized void subtract(double x, double y, double z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    /**
     * Subtract a value from a position
     * @param value amount taken from the x, y, and z
     */
    public synchronized void subtract(double value){
        x -= value;
        y -= value;
        z -= value;
    }

    /**
     * Multiply to quisitions together
     * @param pos another position you want to multiply the current by.
     */
    public synchronized void multiply(Quisition pos){
        x *= pos.x;
        y *= pos.y;
        z *= pos.z;
    }

    /**
     * Multiply a position by a value
     * @param value value the x, y, and z are multiplied by
     */
    public synchronized void multiply(double value){
        x *= value;
        y *= value;
        z *= value;
    }

    /**
     * Divide a quisition from another
     * @param pos another position you want to divide the current from.
     */
    public synchronized void divide(Quisition pos){
        x /= pos.x;
        y /= pos.y;
        z /= pos.z;
    }

    /**
     * Divide a value from a position
     * @param value amount that x, y, and z are divided by
     */
    public synchronized void divide(double value){
        x /= value;
        y /= value;
        z /= value;
    }

    /**
     * Checks to see if quisitions are equal to one other
     * NOTE! This does not take into factor the w component or texquisition
     * @param pos any quisition
     * @return true if equals, false if not
     */
    public boolean equals(Quisition pos){
        return x == pos.x && y == pos.y && z == pos.z;
    }

    /**
     * Test if all the values in the quisition
     * are not a number.
     * @return true if all not a number, false is at least one is real.
     */
    public boolean isNaN(){
        return Double.isNaN(x) && Double.isNaN(y) && Double.isNaN(z);
    }

    /**
     * Test if at least on value in the quisition
     * are not a number,
     * @return true if at least one is not a number, false if all real.
     */
    public boolean hasNaN(){
        return Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z);
    }

    /**
     * Checks all components of the quisition
     * @param pos any quisition
     * @return true if equals, false if not
     */
    public boolean sameAs(Quisition pos){
        return x == pos.x && y == pos.y && z == pos.z;
    }

    /**
     * Chance x by a specific value.
     * @param value value you want to change x by
     */
    public void changeXBy(double value){
        x += value;
    }

    /**
     * Chance y by a specific value.
     * @param value value you want to change x by
     */
    public void changeYBy(double value){
        y += value;
    }

    /**
     * Chance z by a specific value.
     * @param value value you want to change x by
     */
    public void changeZBy(double value){
        z += value;
    }

    /**
     * Change the values of each component by a specifies value.
     * @param x value to increase x by
     * @param y value to increase y by
     * @param z value to increase z by
     */

    public void changeBy(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Change the position by a vector
     * @param point double[]{x, y, z}
     */
    public void changeBy(double[] point){
        x += point[0];
        y += point[1];
        z += point[2];
    }

    /**
     * Change the position by a vector
     * @param vector another Quisition
     */
    public void changeBy(Quisition vector){
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }


    /**
     * Get the distance from one position to another.
     * @param pos the position to find the distance to
     * @return distance
     */
    public double getDistance(Quisition pos){
        return Math.sqrt(Math.pow(pos.x - x,2) + Math.pow(pos.y - y,2) + Math.pow(pos.z - z,2));
    }

    /**
     * Normalizes the Quisition.
     */
    public void normalize(){
        divide(Math3D.getMagnitude(this));
        if (isNaN())
            setPos(0,0,0);
    }

    /**
     * Turns the point into a 3D coordinate.
     * @return a 3D coordinate.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
