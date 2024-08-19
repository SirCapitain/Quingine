package quingine.render.sim.env.obj;

import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.obj.prism.Qube;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;

/**
 * A quism that connects two points with
 * a rectangular prism.
 */

public class ExtendableQuism extends Qube {

    private Quisition pointA, pointB;

    /**
     * A rectangular prism that has two points on either end
     * to make it look like a cable.
     * @param size the size of the cable
     */
    public ExtendableQuism(double size){
        super(size,0,0,0);
    }

    /**
     * Set the points the cable is between
     * @param pointA first Quisition
     * @param pointB second Quisition
     */
    public void setConnections(Quisition pointA, Quisition pointB){
        this.pointA = pointA;
        this.pointB = pointB;
    }

    /**
     * Get the points the cable is connected to
     * @return new Quisition[]{pointA, pointB}
     */
    public Quisition[] getConnectionPoints(){
        return new Quisition[]{pointA, pointB};
    }

    /**
     * Paint the cable
     * @param pic this current quicture that is wanting to draw this quomponent.
     * @param camera the camera painting the picture
     */
    @Override
    public void paint(Quicture pic, Quamera camera) {
        Quisition[] points = new Quisition[getPoints().length];
        Quisition vecA = new Quisition(pointA);
        vecA.subtract(getPos());
        Quisition vecB = new Quisition(pointB);
        vecB.subtract(getPos());
        boolean j = true;
        for (int i = 0; i < getPoints().length; i++) {
            points[i] = new Quisition(getPoints()[i]);
            if (j) {
                points[i].add(vecA);
                if (i == 3)
                    j = false;
            }
            else
                points[i].add(vecB);
        }

        int[] planes = new int[]{1,0,3,1,3,2,4,5,6,4,6,7,5,0,1,5,1,6,7,2,3,7,3,4,4,3,0,4,0,5,6,1,2,6,2,7};
        for (int i = 0; i < planes.length; i+=3)
            paintPlane(pic, camera,new Quisition[]{points[planes[i]], points[planes[i+1]], points[planes[i+2]]}, null);
    }
}
