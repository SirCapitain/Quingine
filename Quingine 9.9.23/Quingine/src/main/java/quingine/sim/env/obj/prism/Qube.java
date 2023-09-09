package quingine.sim.env.obj.prism;

/**
 * Basic class to make a cube.
 */

public class Qube extends QuectangularQuism {
    /**
     * Easy way of creating a cube
     * @param size all lengths will be the same size
     * @param x center position of cube in 3D world
     * @param y center position of cube in 3D world
     * @param z center position of cube in 3D world
     */
    public Qube(double size, double x, double y, double z){
        super(size, size, size, x, y, z);
    }
}
