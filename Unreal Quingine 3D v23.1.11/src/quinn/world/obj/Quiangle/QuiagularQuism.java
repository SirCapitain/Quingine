package quinn.world.obj.Quiangle;

import quinn.world.obj.Qulane;
import quinn.world.obj.Quobject3D;

import java.awt.*;

public class QuiagularQuism extends Quobject3D {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int BASE = 2;
    public static final int BACK = 3;
    public static final int FRONT = 4;
    public QuiagularQuism(double x, double y, double z, double width, double height, double depth){
        super(x, y, z, new Qulane[]{new Qulane(depth, Math.sqrt(Math.pow(height,2)+Math.pow(width/2,2)), x-width/2, y, z, Qulane.RECTANGLE), //Left
                new Qulane(depth, Math.sqrt(Math.pow(height,2)+Math.pow(width/2,2)),x+width/2, y, z,Qulane.RECTANGLE), //Right
                new Qulane(depth, width, x, y+height/2, z,Qulane.RECTANGLE), //Base
                new Qulane(width, height, x, y, z-depth/2,Qulane.TRIANGLE), //Back
                new Qulane(width, height, x, y, z+depth/2,Qulane.TRIANGLE)}); //Front
        double theta = Math.toDegrees(Math.atan((width/2)/height));
        getPlanes()[0].rotate(90,0,1,0);//Left
        getPlanes()[1].rotate(-90,0,1,0);//Right
        getPlanes()[0].rotate(theta,0,0,1);//Left
        getPlanes()[0].setPos(getPlanes()[0].getPos().x - (getPlanes()[0].height/2)*Math.cos(Math.toRadians(90-theta)), getPlanes()[0].getPos().y, getPlanes()[0].getPos().z);
        getPlanes()[1].rotate(-theta,0,0,1);//Right
        getPlanes()[1].setPos(getPlanes()[1].getPos().x + (getPlanes()[0].height/2)*Math.cos(Math.toRadians(90-theta)), getPlanes()[1].getPos().y, getPlanes()[1].getPos().z);
        getPlanes()[2].rotate(90,1,0,0);//Base
        getPlanes()[2].rotate(90,0,1,0);//Base

        }

        private boolean fill;
        public boolean fill(){
            return fill;
        }
        public void fill(boolean fill){
            this.fill = fill;
            for (Qulane plane : getPlanes())
                plane.fill(fill);
        }

        public void setFaceColor(Color color, int face){
            getPlanes()[face].setFillColor(color);
        }
        public double width, height, depth;
        public void setDimensions(double width, double height, double depth){
            this.width = width;
            this.height = height;
            this.depth = depth;
    }
}
