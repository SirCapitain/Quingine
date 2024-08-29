package quingine.render.sim.env.obj.prism;

import quingine.render.sim.pos.Quisition;
import quingine.render.sim.env.obj.Quobject;

public class QuiangularQuism extends Quobject {

    private double width;
    private double height;
    private double depth;

    /**
     * Easy way of creating a triangular prism
     * @param width how far back it goes on the x axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the z axis
     * @param x center position of the prism in 3D world
     * @param y center position of the prism in 3D world
     * @param z center position of the prism in 3D world
     */
    public QuiangularQuism(double width, double height, double depth, double x, double y, double z) {
       super();
       setPos(x,y,z);
       setDimension(width,height,depth);
    }


    /**
     * Set the dimensions of the quism
     * @param width how far back it goes on the x axis
     * @param height how far up it goes on the y axis
     * @param depth how far sideways it goes on the z axis
     */
    public void setDimension(double width, double height, double depth){
        this.width = width;
        this.height = height;
        this.depth = depth;
        double x = getPos().x;
        double y = getPos().y;
        double z = getPos().z;
        setPoints(new Quisition[]{new Quisition(x, y + height * .5, z),
                new Quisition(x - width * .5, y - height * .5, z + depth * .5),
                new Quisition(x - width * .5, y - height * .5, z - depth * .5),
                new Quisition(x + width * .5, y - height * .5, z - depth * .5),
                new Quisition(x + width * .5, y - height * .5, z + depth * .5)});
        setFaces(new int[][]{
                new int[]{4, 1, 2},//Bottom
                new int[]{4, 2, 3},
                new int[]{2, 0, 3},//Front
                new int[]{1, 0, 2},//Left
                new int[]{3, 0, 4},//Right
                new int[]{4, 0, 1}});//Back
    }

    /**
     * Reload the quobject
     */
    public void reload(){
        super.reload();
        setDimension(width,height,depth);
    }
}
