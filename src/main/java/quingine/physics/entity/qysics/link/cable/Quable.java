package quingine.physics.entity.qysics.link.cable;

import quingine.physics.MathPhys;
import quingine.physics.entity.QollidableQuobject;
import quingine.physics.entity.qysics.particle.ContactGenerator;
import quingine.render.sim.Math3D;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.ExtendableQuism;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;

/**
 * Links two QollidableQuobjects together with a stiff cable!
 */

public class Quable extends QollidableQuobject {

    protected QollidableQuobject particleA;
    protected QollidableQuobject particleB;
    private double maxLength = 7;
    private double restitution = .5;

    /**
     * Create a new cable that is linked to two QollidableQuobjects
     * @param particleA this QollidableQuobject
     * @param particleB and this QollidableQuobject
     */
    public Quable(QollidableQuobject particleA, QollidableQuobject particleB){
        setLinks(particleA, particleB);
    }

    /**
     * Set the links yourself. Changes which QollidableQuobjects are linked together
     * with the cable
     * @param particleA a QollidableQuobject
     * @param particleB the other QollidableQuobject
     */
    public void setLinks(QollidableQuobject particleA, QollidableQuobject particleB){
        this.particleA = particleA;
        this.particleB = particleB;
    }

    /**
     * Get the QollidableQuobjects that are link together on the cable
     * @return new QollidableQuobject[]{particleA, particleB}
     */
    public QollidableQuobject[] getLinks(){
        return new QollidableQuobject[]{particleA, particleB};
    }

    /**
     * Set the bounciness of the cable. Yes... cables are bouncy
     * @param restitution a double between 0-1. I do not recommend going outside that.
     */
    public void setRestitution(double restitution){
        this.restitution = restitution;
    }

    /**
     * Get the boinginess of the cable.
     * @return a number that represents the boinginess
     */
    public double getRestitution(){
        return restitution;
    }

    /**
     * Set the max length of the cable
     * @param maxLength a double to represent the distance of how long the cable is.
     */
    public void setMaxLength(double maxLength){
        this.maxLength = maxLength;
    }

    /**
     * Get the max length of the cable.
     * @return a number that tells you the max length of the cable.... I do not need to explain this...
     */
    public double getMaxLength(){
        return maxLength;
    }

    /**
     * Get the current length of the cable.
     * @return current distance between both particles that are linked on the cable
     */
    public double getLength(){
        return particleA.getPos().getDistance(particleB.getPos());
    }

    /**
     * Update the particles on how a cable works
     * @param world the quword the QollidableQuobject is in.
     */
    @Override public void update(Quworld world) {
        double length = getLength();
        if (length < maxLength)
            return;
        Quisition normal = Math3D.calcNormalDirectionVector(particleB.getPos(), particleA.getPos());
        ContactGenerator.calculateVelocities(particleA, particleB, MathPhys.calcSeparatingVelocity(particleA.getVelocity(), particleB.getVelocity(), normal), normal, restitution);
        ContactGenerator.resolveCollision(particleA, particleB, normal, length-maxLength);
    }

    /**
     * Dude... this stuff is delicious! You should try some!
     * @param q this current quicture that is wanting to draw this quomponent.
     * @param c the thing doing the looking
     */
    @Override
    public void paint(Quicture q, Quamera c){
        if (getQuobject() == null)
            return;
        if (getQuobject() instanceof ExtendableQuism link)
            link.setConnections(particleA.getPos(), particleB.getPos());
        super.paint(q, c);
    }
}
