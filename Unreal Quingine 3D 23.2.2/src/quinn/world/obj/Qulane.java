package quinn.world.obj;

/**
 * The 2D pla- I mean Qulane that makes up 3D objects.
 *
 * @author Quinn Graham
 */

public class Qulane extends Quobject {

    public double width, height;
    public static final int RECTANGLE = 0;
    public static final int TRIANGLE = 1;

    /**
     * A way to make a qulane
     * @param width how wide it is
     * @param height how tall it is.
     * @param x center of qulane
     * @param y center of qulane
     * @param z center of qulane
     * @param shape What kind of shape it makes up.
     */
    public Qulane(double width, double height, double x, double y, double z, int shape){
        super(x,y,z);
        setDimensions(width,height);
        if (shape == 1)
        setPoints(new Quisition[]{new Quisition(x, y-height/2, z),//Top
                                       new Quisition(x-width/2, y+height/2, z),//Bottom Left
                                       new Quisition(x+width/2, y+height/2, z)});//Bottom Right
        else
            setPoints(new Quisition[]{new Quisition(x-width/2, y-height/2, z),//Top Left
                    new Quisition(x-width/2, y+height/2, z),//Bottom Left
                    new Quisition(x+width/2, y+height/2, z),//Bottom Right
                    new Quisition(x+width/2, y-height/2, z)});//Top Right
    }

    /**
     * Get the equation that make up the plane.
     * This method only takes the FIRST THREE points of the qulane.
     * If you have one point out of place then you will notice problems.
     * @return equation in the format: ax + by + cz + b
     */
    public double[] getEquation(){
        return getEquation(getPoints()[0], getPoints()[1], getPoints()[2]);
    }

    /**
     * Get the equation of any set of points with ease!
     * @param pos1 first position
     * @param pos2 second position
     * @param pos3 third position
     * @return equation in the format: ax + by + cz + b
     */
    public static double[] getEquation(Quisition pos1, Quisition pos2, Quisition pos3) {
        double[] vec = Quisition.calcVectorNotCollinear(pos1, pos2, pos3);
        if (vec != null)
            return new double[]{vec[0],vec[1],vec[2], (vec[0]*-pos1.x + vec[1]*-pos1.y +vec[2]*-pos1.z)};
        return null;
    }

    /**
     * Calculate the z-value of any list of points.
     * Note! This only takes the FIRST THREE points of the list.
     * Any more will not be calculated for.
     * @param points list of quisitions.
     * @param x value
     * @param y value
     * @return z
     */
    public static double calcZ(Quisition[] points, double x, double y){
        double[] vec = Quisition.calcVector(points[0], points[1], points[2]);
        double z;
        if (vec[2] == 0)
            z = points[0].z;
        else
            z = (1/vec[2])*(vec[0]*points[0].x + vec[1]*points[0].y + vec[2]*points[0].z - vec[0]*x - vec[1]*y);
        if (Double.isInfinite(z))
            z = points[0].z;
        return z;
    }

    /**
     * Sometimes you just gotta set the size.
     * @param width how wide
     * @param height how tall
     */
    public void setDimensions(double width, double height){
        this.height = height;
        this.width = width;
    }
}
