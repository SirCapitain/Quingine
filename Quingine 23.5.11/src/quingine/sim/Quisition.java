package quingine.sim;

/**
 * A position in 3D space
 * @author Quinn Graham
 */

public class Quisition {

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

    public double x, y, z;
    /**
     * Create a new point and set where it is.
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     */
    public void setPos(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPos(Quisition pos){
        x = pos.x;
        y = pos.y;
        z = pos.z;
    }

    /**
     * Add two quisitions together.
     * @param pos another position you want to add.
     */
    public void add(Quisition pos){
        x += pos.x;
        y += pos.y;
        z += pos.z;
    }

    /**
     * Add a value to the position
     * @param value number to be added to the x, y, and z
     */
    public void add(double value){
        x += value;
        y += value;
        z += value;
    }

    /**
     * Subtract two quisitions.
     * @param pos another position you want to take from the current.
     */
    public void subtract(Quisition pos){
        x -= pos.x;
        y -= pos.y;
        z -= pos.z;
    }

    /**
     * Subtract a value from a position
     * @param value amount taken from the x, y, and z
     */
    public void subtract(double value){
        x -= value;
        y -= value;
        z -= value;
    }

    /**
     * Multiply to quisitions together
     * @param pos another position you want to multiply the current by.
     */
    public void multiply(Quisition pos){
        x *= pos.x;
        y *= pos.y;
        z *= pos.z;
    }

    /**
     * Multiply a position by a value
     * @param value value the x, y, and z are multiplied by
     */
    public void multiply(double value){
        x *= value;
        y *= value;
        z *= value;
    }

    /**
     * Divide a quisition from another
     * @param pos another position you want to divide the current from.
     */
    public void divide(Quisition pos){
        x /= pos.x;
        y /= pos.y;
        z /= pos.z;
    }

    /**
     * Divide a value from a position
     * @param value amount that x, y, and z are divided by
     */
    public void divide(double value){
        x /= value;
        y /= value;
        z /= value;
    }

    public boolean equals(Quisition pos){
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
     * Turns the point into a 3D coordinate.
     * @return a 3D coordinate.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
