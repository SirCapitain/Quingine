package quinn.world.obj.Quectangal;

/**
 * For the lazy people who don't want to type in all the numbers to make a prism.
 *
 * @author Quinn Graham
 */

public class Qube extends QuectangularQuism {
    public Qube(double x, double y, double z, double size){
        super(x,y,z,size,size,size);
    }
    public void setSize(double size){
        setDimensions(size, size, size);
    }

}
