package quinn.world.obj.Quectangal;

/**
 * For the lazy people who don't want to type in all the numbers to make a Quectangular Quism.
 *
 * @author Quinn Graham
 */

public class Qube extends QuectangularQuism {
    /**
     * The home of being lazy
     * @param x middle
     * @param y of the
     * @param z qube
     * @param size how large
     */
    public Qube(double x, double y, double z, double size){
        super(x,y,z,size,size,size);
    }

    /**
     * Due to having all lengths be the same, you can set size and skip a few extra steps.
     * @param size how big
     */
    public void setSize(double size){
        setDimensions(size, size, size);
    }

}
