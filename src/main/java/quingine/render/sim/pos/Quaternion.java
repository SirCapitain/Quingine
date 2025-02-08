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
        w = 0;
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
     * Create a quaternion based a quisition.
     * Only freaks would use this.
     * @param quisition ITS A QUISITION!
     */
    public Quaternion(Quisition quisition){
        super(quisition);
    }

}
