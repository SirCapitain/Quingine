package quingine.sim;

/**
 * A UV position that states a specific point on an image.
 */

public class Texquisition {

    /**
     * Takes in a u and v and creates a point
     * U and V are from 0 -> 1
     * @param u units from left most side of image
     * @param v units down from top most side of image
     */
    public Texquisition(double u, double v){
        setUV(u, v);
    }

    /**
     * Copies a Texquisition to this one making it into a separate object.
     * @param texPos a Texquisition to copy to a new one
     */
    public Texquisition(Texquisition texPos){
        setUV(texPos.u, texPos.v);
    }

    /**
     * Creates a blank texquisition with UV at (0,0)
     */
    public Texquisition() {
        setUV(0,0);
    }

    public double u, v;

    /**
     * Set the UV coordinates of the Texquisition
     * @param u units from left most side of image
     * @param v units down from top most side of image
     */
    public void setUV(double u, double v){
        this.u = u;
        this.v = v;
    }

    /**
     * Prints this into a string
     * @return a string
     */
    @Override
    public String toString() {
        return "(" + u + ", " + v + ")";
    }
}
