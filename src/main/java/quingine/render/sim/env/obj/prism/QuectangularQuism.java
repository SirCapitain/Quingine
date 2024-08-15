package quingine.render.sim.env.obj.prism;

import quingine.render.sim.pos.Quisition;
import quingine.render.sim.env.obj.Quobject;

import java.awt.*;

public class QuectangularQuism extends Quobject {

    public final int TOP = 0;
    public final int BOTTOM = 2;
    public final int LEFT = 4;
    public final int RIGHT = 6;
    public final int BACK = 8;
    public final int FRONT = 10;

    /**
     * Easy way of creating a rectangular prism
     * @param width how far back it goes on the z axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the x axis
     * @param x center position of rectangular prism in 3D world
     * @param y center position of rectangular prism in 3D world
     * @param z center position of rectangular prism in 3D world
     */
    public QuectangularQuism(double width, double height, double depth, double x, double y, double z) {
        super(new Quisition[]{new Quisition(x - width * .5, y + height * .5, z + depth * .5),
                new Quisition(x - width * .5, y + height * .5, z - depth * .5),
                new Quisition(x + width * .5, y + height * .5, z - depth * .5),
                new Quisition(x + width * .5, y + height * .5, z + depth * .5),
                new Quisition(x + width * .5, y - height * .5, z + depth * .5),
                new Quisition(x - width * .5, y - height * .5, z + depth * .5),
                new Quisition(x - width * .5, y - height * .5, z - depth * .5),
                new Quisition(x + width * .5, y - height * .5, z - depth * .5)}, x, y, z);
        setFaces(new int[][]{
                new int[]{1, 0, 3},//Top
                new int[]{1, 3, 2},
                new int[]{4, 5, 6},//Bottom
                new int[]{4, 6, 7},
                new int[]{5, 0, 1},//Left
                new int[]{5, 1, 6},
                new int[]{7, 2, 3},//Right
                new int[]{7, 3, 4},
                new int[]{4, 3, 0},//Back
                new int[]{4, 0, 5},
                new int[]{6, 1, 2},//Front
                new int[]{6, 2, 7}});
    }
}
