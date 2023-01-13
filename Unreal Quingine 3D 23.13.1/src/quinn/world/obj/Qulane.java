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
     * Sometimes you just gotta set the size.
     * @param width how wide
     * @param height how tall
     */
    public void setDimensions(double width, double height){
        this.height = height;
        this.width = width;
    }
}
