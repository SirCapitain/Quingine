package quingine.render.sim.env.obj.prism;

import quingine.render.sim.pos.Quisition;
import quingine.render.sim.env.obj.Quobject;

import java.awt.*;

public class QuectangularQuism extends Quobject {

    private double width;
    private double height;
    private double depth;

    /**
     * Easy way of creating a rectangular prism
     * @param width how far back it goes on the x axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the z axis
     * @param x center position of rectangular prism in 3D world
     * @param y center position of rectangular prism in 3D world
     * @param z center position of rectangular prism in 3D world
     */
    public QuectangularQuism(double width, double height, double depth, double x, double y, double z) {
        super();
        setPos(x,y,z);
        setDimensions(width, height, depth);
    }

    /**
     * Set the dimensions of the quectangular quism
     * @param width how far back it goes on the x axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the z axis
     */
    public void setDimensions(double width, double height, double depth){
        double x = getPos().x;
        double y = getPos().y;
        double z = getPos().z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        setPoints(new Quisition[]{new Quisition(x - width * .5, y + height * .5, z + depth * .5),
                new Quisition(x - width * .5, y + height * .5, z - depth * .5),
                new Quisition(x + width * .5, y + height * .5, z - depth * .5),
                new Quisition(x + width * .5, y + height * .5, z + depth * .5),
                new Quisition(x + width * .5, y - height * .5, z + depth * .5),
                new Quisition(x - width * .5, y - height * .5, z + depth * .5),
                new Quisition(x - width * .5, y - height * .5, z - depth * .5),
                new Quisition(x + width * .5, y - height * .5, z - depth * .5)});
        setFaces(new int[]{
                1, 0, 3,//Top
                1, 3, 2,
                4, 5, 6,//Bottom
                4, 6, 7,
                5, 0, 1,//Left
                5, 1, 6,
                7, 2, 3,//Right
                7, 3, 4,
                4, 3, 0,//Back
                4, 0, 5,
                6, 1, 2,//Front
                6, 2, 7});
    }

    /**
     * Reload the quobject
     */
    public void reload(){
        super.reload();
        setDimensions(width, height, depth);
    }
}
