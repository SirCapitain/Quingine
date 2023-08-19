package quingine.sim.obj.prism;

import quingine.sim.Quisition;
import quingine.sim.obj.BindingPlane;
import quingine.sim.obj.Quobject;

import java.awt.*;

public class QuectangularQuism extends Quobject {
    /**
     * Easy way of creating a rectangular prism
     * @param width how far back it goes on the z axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the x axis
     * @param x center position of rectangular prism in 3D world
     * @param y center position of rectangular prism in 3D world
     * @param z center position of rectangular prism in 3D world
     */
    public QuectangularQuism(double width, double height, double depth, double x, double y, double z){
        super(new Quisition[]{new Quisition(x-width*.5, y+height*.5, z+depth*.5),
                new Quisition(x-width*.5, y+height*.5, z-depth*.5),
                new Quisition(x+width*.5, y+height*.5, z-depth*.5),
                new Quisition(x+width*.5, y+height*.5, z+depth*.5),
                new Quisition(x+width*.5, y-height*.5, z+depth*.5),
                new Quisition(x-width*.5, y-height*.5, z+depth*.5),
                new Quisition(x-width*.5, y-height*.5, z-depth*.5),
                new Quisition(x+width*.5, y-height*.5, z-depth*.5)}, x, y, z);
        addPlane(new int[]{1,0,3});//Top
        addPlane(new int[]{1,3,2});

        addPlane(new int[]{4,5,6});//Bottom
        addPlane(new int[]{4,6,7});

        addPlane(new int[]{5,0,1});//Left
        addPlane(new int[]{5,1,6});

        addPlane(new int[]{7,2,3});//Right
        addPlane(new int[]{7,3,4});

        addPlane(new int[]{4,3,0});//Back
        addPlane(new int[]{4,0,5});

        addPlane(new int[]{6,1,2});//Front
        addPlane(new int[]{6,2,7});
    }
    public void debugColors(){
        fill(true);
        setPlaneColor(TOP, Color.red);
        setPlaneColor(TOP+1, Color.red);
        setPlaneColor(BOTTOM, Color.magenta);
        setPlaneColor(BOTTOM+1, Color.magenta);
        setPlaneColor(LEFT, Color.blue);
        setPlaneColor(LEFT+1, Color.blue);
        setPlaneColor(RIGHT, Color.yellow);
        setPlaneColor(RIGHT+1, Color.yellow);
        setPlaneColor(BACK, Color.green);
        setPlaneColor(BACK+1, Color.green);
        setPlaneColor(FRONT, Color.cyan);
        setPlaneColor(FRONT+1, Color.cyan);
    }

    public final int TOP = 0;
    public final int BOTTOM = 2;
    public final int LEFT = 4;
    public final int RIGHT = 6;
    public final int BACK = 8;
    public final int FRONT = 10;
}
