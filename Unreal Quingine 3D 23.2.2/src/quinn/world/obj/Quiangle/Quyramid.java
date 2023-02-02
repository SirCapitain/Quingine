package quinn.world.obj.Quiangle;

import quinn.world.obj.Qulane;
import quinn.world.obj.Quobject3D;

import java.awt.*;

/**
 * The place where Egyptians were inspired! Then stole.
 * Pyramid were original called Quyramids but those Egyptians take
 * what every seems super duper cool to them. I will get them back
 * some day. MARK MY WORD!
 *
 * @author Quinn Graham
 */

public class Quyramid extends Quobject3D {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BASE = 2;
    public static final int BACK = 3;
    public static final int FRONT = 4;

    /**
     * The base code for a Quyramid
     * @param x middle point
     * @param y middle point
     * @param z middle point
     * @param width how long horizontally
     * @param height how long vertically
     * @param depth how long backitally
     */
    public Quyramid(double x, double y, double z, double width, double height, double depth){
        super(x, y, z, new Qulane[]{new Qulane(depth, Math.sqrt(Math.pow(height,2)+Math.pow(width/2,2)), x-width/2, y, z, Qulane.TRIANGLE), //Left
                new Qulane(depth, Math.sqrt(Math.pow(height,2)+Math.pow(width/2,2)),x+width/2, y, z,Qulane.TRIANGLE), //Right
                new Qulane(depth, width, x, y+height/2, z,Qulane.RECTANGLE), //Base
                new Qulane(width, Math.sqrt(Math.pow(height,2)+Math.pow(depth/2,2)), x, y, z-depth/2,Qulane.TRIANGLE), //Back
                new Qulane(width, Math.sqrt(Math.pow(height,2)+Math.pow(depth/2,2)), x, y, z+depth/2,Qulane.TRIANGLE)}); //Front
        double thetaX = Math.toDegrees(Math.atan((width/2)/height));
        double thetaZ = Math.toDegrees(Math.atan((depth/2)/height));
        getPlanes()[0].rotate(90,0,1,0);//Left
        getPlanes()[1].rotate(-90,0,1,0);//Right
        getPlanes()[0].rotate(thetaX,0,0,1);//Left
        getPlanes()[0].setPos(getPlanes()[0].getPos().x - (getPlanes()[0].height/2)*Math.cos(Math.toRadians(90-thetaX)), getPlanes()[0].getPos().y, getPlanes()[0].getPos().z);
        getPlanes()[1].rotate(-thetaX,0,0,1);//Right
        getPlanes()[1].setPos(getPlanes()[1].getPos().x + (getPlanes()[0].height/2)*Math.cos(Math.toRadians(90-thetaX)), getPlanes()[1].getPos().y, getPlanes()[1].getPos().z);
        getPlanes()[2].rotate(90,1,0,0);//Base
        getPlanes()[2].rotate(90,0,1,0);//Base
        //Front
        getPlanes()[3].rotate(-thetaZ,1,0,0);
        getPlanes()[3].setPos(getPlanes()[3].getPos().x, getPlanes()[3].getPos().y, getPlanes()[3].getPos().z - (getPlanes()[3].height/2)*Math.cos(Math.toRadians(90-thetaZ)));
        //Back
        getPlanes()[4].rotate(thetaZ,1,0,0);
        getPlanes()[4].setPos(getPlanes()[4].getPos().x, getPlanes()[4].getPos().y, getPlanes()[4].getPos().z + (getPlanes()[4].height/2)*Math.cos(Math.toRadians(90-thetaZ)));


    }

    /**
     * STOP MAKE THAT FACE AT ME!
     * @param color is what you want your face to look like
     * @param face what face you want to change
     */
    public void setFaceColor(Color color, int face){
        getPlanes()[face].setFillColor(color);
    }

    public double width, height, depth;

    /**
     * Sometimes, just a different size is all you need.
     * @param width you know
     * @param height what these
     * @param depth do
     */
    public void setDimensions(double width, double height, double depth){
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
}
