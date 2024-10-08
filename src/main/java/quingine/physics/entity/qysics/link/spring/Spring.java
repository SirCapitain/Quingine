package quingine.physics.entity.qysics.link.spring;

import quingine.physics.entity.QollidableQuobject;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.obj.prism.ExtendableQuism;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;

import java.awt.*;

/**
 * A blueprint for other types of springs
 */

public class Spring extends QollidableQuobject {

    private double restLength, constant;
    private ExtendableQuism cable;

    /**
     * Something springy
     * @param restLength the length at which the spring desire to be in
     * @param constant the amount of force it applies over the change in length
     */
    public Spring(double restLength, double constant){
        setRestLength(restLength);
        setConstant(constant);
        ExtendableQuism e = new ExtendableQuism(.5);
        e.setFullColor(Color.blue.getRGB());
        e.outline(true);
        e.alwaysLit(true);
        setQuobject(e);
    }

    /**
     * Get the rest length of the spring
     * @return the length at which the spring desires to rest in
     */
    public double getRestLength() {
        return restLength;
    }

    /**
     * Set the length at which the spring rests in
     * @param restLength the length at which the spring wants to be at
     */
    public void setRestLength(double restLength) {
        this.restLength = restLength;
    }

    /**
     * Get the amount of force the spring applies over the change in length
     * @return the force it applies over the distance
     */
    public double getConstant() {
        return constant;
    }

    /**
     * Set the cable that will be the spring.
     * @param cable a ExtendableQuism
     */
    public void setCable(ExtendableQuism cable){
        this.cable = cable;
        setQuobject(cable);
    }

    /**
     * Get the current cable in use
     * @return the ExtendableQuism in use.
     */
    public ExtendableQuism getCable(){
        return cable;
    }

    /**
     * Set the amount of force the spring exerts when the length is changed
     * @param constant the amount of force the spring applies when the length is changed.
     */
    public void setConstant(double constant) {
        this.constant = constant;
    }

    /**
     * Paint the spring and the quobject that is
     * acting as the cable
     * @param q this current quicture that is wanting to draw this quomponent.
     * @param camera the quamera being used
     * @param pointA the first end of the spring
     * @param pointB the second end of the spring
     */
    public void paint(Quicture q, Quamera camera, Quisition pointA, Quisition pointB){
        if (cable == null)
            return;
        cable.setConnections(pointA, pointB);
        super.paint(q, camera);
    }
}
