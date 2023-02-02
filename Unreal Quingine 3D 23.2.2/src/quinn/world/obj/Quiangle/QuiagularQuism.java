package quinn.world.obj.Quiangle;

import quinn.world.obj.Qulane;
import quinn.world.obj.Quobject3D;

import java.awt.*;

/**
 * The Quangular Quism is like the Quectangular Quism but with two quiangles as faces! *gasp*
 * @author Quinn Graham
 */

public class QuiagularQuism extends Quobject3D {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BASE = 2;
    public static final int BACK = 3;
    public static final int FRONT = 4;

    /**
     * The making of the Qiangular Quism. Enjoy
     * @param x middle of quiangle
     * @param y middle of quiangle
     * @param z middle of quiangle
     * @param width How fat the quiangle is
     * @param height How tall the quiangle is
     * @param depth How... long? the quiangle is.
     */
    public QuiagularQuism(double x, double y, double z, double width, double height, double depth){
        super(x, y, z, new Qulane[]{new Qulane(depth, Math.sqrt(Math.pow(height,2)+Math.pow(width/2,2)), x-width/2, y, z, Qulane.RECTANGLE), //Left
                new Qulane(depth, Math.sqrt(Math.pow(height,2)+Math.pow(width/2,2)),x+width/2, y, z,Qulane.RECTANGLE), //Right
                new Qulane(depth, width, x, y+height/2, z,Qulane.RECTANGLE), //Base
                new Qulane(width, height, x, y, z-depth/2,Qulane.TRIANGLE), //Back
                new Qulane(width, height, x, y, z+depth/2,Qulane.TRIANGLE)}); //Front
        double theta = Math.atan((width/2)/height);
        getPlanes()[0].rotate(90,0,1,0);//Left
        getPlanes()[1].rotate(-90,0,1,0);//Right
        getPlanes()[0].rotate(Math.toDegrees(theta),0,0,1);//Left
        getPlanes()[0].setPos(getPlanes()[0].getPos().x - (getPlanes()[0].height/2)*Math.cos(90-theta), getPlanes()[0].getPos().y, getPlanes()[0].getPos().z);
        getPlanes()[1].rotate(Math.toDegrees(-theta),0,0,1);//Right
        getPlanes()[1].setPos(getPlanes()[1].getPos().x + (getPlanes()[0].height/2)*Math.cos(90-theta), getPlanes()[1].getPos().y, getPlanes()[1].getPos().z);
        getPlanes()[2].rotate(90,1,0,0);//Base
        getPlanes()[2].rotate(90,0,1,0);//Base
        }

    /**
     * Setting the color of each face is nice. Try it.
     * @param color WHAT EVER COLOR YOU WANT!
     * @param face WHAT EVER FACE YOU WANT!
     */
    public void setFaceColor(Color color, int face){
            getPlanes()[face].setFillColor(color);
        }


        public double width, height, depth;

    /**
     * Setting these dimensions can help you create cool shapes.
     * @param width how wide
     * @param height how high
     * @param depth how dep
     */
        public void setDimensions(double width, double height, double depth){
            this.width = width;
            this.height = height;
            this.depth = depth;
    }
}
