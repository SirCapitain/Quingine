package quingine.render.sim.pos;

/**
 * Really just a quisition with a w.
 * I like w.
 * w is a cool letter
 */

public class Quaternion extends Quisition{

    public double w;

    /**
     * Create a new quaternion with (0,0,0,0)
     */
    public Quaternion(){
        w = 1;
    }

    /**
     * Create a new quaternion bases off parameters.
     * @param x value of i
     * @param y value of j
     * @param z value of k
     * @param w value of w
     */
    public Quaternion(double x, double y, double z, double w) {
        super(x, y, z);
        this.w = w;
    }

    /**
     * Copy a quaternion from another one
     * @param quaternion the one to copy from.
     */
    public Quaternion(Quaternion quaternion){
        super(quaternion);
        this.w = quaternion.w;
    }

    /**
     * Normalizes the quaternion.
     * Sometimes those pesky quaternions deviate a little.
     */
    public void normalize(){
        double mag = Math.sqrt(w*w+x*x+y*y+z*z);
        divide(mag);
        w /= mag;
    }

    /**
     * resets the quaternion to default of 0 degrees.
     */
    public void reset(){
        w = 1;
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Create a quaternion based a quisition.
     * Only freaks would use this.
     * @param quisition ITS A QUISITION!
     */
    public Quaternion(Quisition quisition){
        super(quisition);
    }

    @Override
    public String toString() {
        return x + "i" + " + " + y + "j" + " + " + z + "k" + " + " + w ;
    }
}
