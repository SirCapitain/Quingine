package quingine.render.sim.env.obj.prism;

import quingine.render.sim.pos.Quisition;
import quingine.render.sim.env.obj.Quobject;

import java.awt.*;

public class QuiangularQuism extends Quobject {

    public final int BOTTOM = 0;
    public final int FRONT = 2;
    public final int LEFT = 3;
    public final int RIGHT = 4;
    public final int BACK = 5;

    /**
     * Easy way of creating a triangular prism
     * @param width how far back it goes on the z axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the x axis
     * @param x center position of rectangular prism in 3D world
     * @param y center position of rectangular prism in 3D world
     * @param z center position of rectangular prism in 3D world
     */
    public QuiangularQuism(double width, double height, double depth, double x, double y, double z) {
        super(new Quisition[]{new Quisition(x, y + height * .5, z),
                new Quisition(x - width * .5, y - height * .5, z + depth * .5),
                new Quisition(x - width * .5, y - height * .5, z - depth * .5),
                new Quisition(x + width * .5, y - height * .5, z - depth * .5),
                new Quisition(x + width * .5, y - height * .5, z + depth * .5)}, x, y, z);
        setFaces(new int[][]{
                new int[]{4, 1, 2},//Bottom
                new int[]{4, 2, 3},
                new int[]{2, 0, 3},//Front
                new int[]{1, 0, 2},//Left
                new int[]{3, 0, 4},//Right
                new int[]{4, 0, 1}});//Back
    }
}
