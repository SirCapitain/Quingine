package quingine.sim;

/**
 * A position in 3D space
 */

public class Quisition {

    /**
     * Not a fun way to make a point but it is a fast way.
     * Creates a point at (0, 0, 0)
     */
    public Quisition(){
        setPos(0,0,0,1);
    }

    /**
     * Set the position of a quisition using a list of numbers
     * @param pos list of doubles >= 3 in length
     */
    public Quisition(double[] pos){
        if (pos.length < 4)
            setPos(pos[0], pos[1], pos[2]);
        else
            setPos(pos[0], pos[1], pos[2], pos[3]);
    }

    /**
     * Creates a quisition with a texquisition bound to it
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     * @param texPos a texquisition position
     */
    public Quisition(double x, double y, double z, Texquisition texPos){
        setPos(x, y, z);
        setTexquisition(texPos);
    }

    /**
     * Creates a quisition with a new texquisition bound to it
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     * @param u point on 2D image
     * @param v point on 2D image
     */
    public Quisition(double x, double y, double z, double u, double v){
        setPos(x, y, z);
        setTexquisition(u, v);
    }

    /**
     * Creates a quisition with and also specifies the w component and UV coordinates on an image
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     * @param w something
     * @param u point on 2D image
     * @param v point on 2D image
     */
    public Quisition(double x, double y, double z, double w, double u, double v){
        setPos(x, y, z, w);
        setTexquisition(u, v);
    }

    /**
     * A good way of copying a quisition.
     * @param pos another position you want to copy.
     */
    public Quisition(Quisition pos){
        setPos(pos);
        setTexquisition(pos.t);
    }

    public Texquisition t = new Texquisition();

    /**
     * Set the current texquisition that is point to this position
     * @param u coordinate on 2D image
     * @param v coordinate on 2D image
     */
    public void setTexquisition(double u, double v){
        t = new Texquisition(u,v);
    }

    /**
     * Set the texquisition that is to be bound to the quisition
     * @param texPos a texquisition
     */
    public void setTexquisition(Texquisition texPos){
        t = new Texquisition(texPos);
    }

    /**
     * Get the current texquisition that is bound to the quisition
     * @return current texquisition being used.
     */
    public Texquisition getTexquisition(){
        return t;
    }

    /**
     * Create a new point and set where it is.
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     */
    public Quisition(double x, double y, double z){
        setPos(x, y, z, 1);
    }

    /**
     * Initialize a quisition and its w component
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     * @param w something
     */
    public Quisition(double x, double y, double z, double w){
        setPos(x, y, z, w);
    }

    public double w, x, y, z;
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

    /**
     * Set the position of a quisition and its w component
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     * @param w something
     */
    public void setPos(double x, double y, double z, double w){
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Set the position of a quisition using another quisition
     * This copies the info from one to the other
     * @param pos any quisition you want to copy over
     */
    public void setPos(Quisition pos){
        w = pos.w;
        x = pos.x;
        y = pos.y;
        z = pos.z;
    }

    /**
     * Add two quisitions together.
     * @param pos another position you want to add.
     */
    public void add(Quisition pos){
        w += pos.w;
        x += pos.x;
        y += pos.y;
        z += pos.z;
    }

    /**
     * Add a value to the position
     * @param value number to be added to the x, y, and z
     */
    public void add(double value){
        w += value;
        x += value;
        y += value;
        z += value;
    }

    /**
     * Subtract two quisitions.
     * @param pos another position you want to take from the current.
     */
    public void subtract(Quisition pos){
        w -= pos.w;
        x -= pos.x;
        y -= pos.y;
        z -= pos.z;
    }

    /**
     * Subtract a value from a position
     * @param value amount taken from the x, y, and z
     */
    public void subtract(double value){
        w -= value;
        x -= value;
        y -= value;
        z -= value;
    }

    /**
     * Multiply to quisitions together
     * @param pos another position you want to multiply the current by.
     */
    public void multiply(Quisition pos){
        w *= pos.w;
        x *= pos.x;
        y *= pos.y;
        z *= pos.z;
    }

    /**
     * Multiply a position by a value
     * @param value value the x, y, and z are multiplied by
     */
    public void multiply(double value){
        w *= value;
        x *= value;
        y *= value;
        z *= value;
    }

    /**
     * Divide a quisition from another
     * @param pos another position you want to divide the current from.
     */
    public void divide(Quisition pos){
        w /= pos.w;
        x /= pos.x;
        y /= pos.y;
        z /= pos.z;
    }

    /**
     * Divide a value from a position
     * @param value amount that x, y, and z are divided by
     */
    public void divide(double value){
        w /= value;
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
     * Checks all components of the quisition except for the texquisition.
     * @param pos any quisition
     * @return true if equals, false if not
     */
    public boolean sameAs(Quisition pos){
        return w == pos.w && x == pos.x && y == pos.y && z == pos.z;
    }

    /**
     * Change the w component by a specific value
     * @param value adds a value to w.
     */
    public void changeWBy(double value){ w += value;}

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
