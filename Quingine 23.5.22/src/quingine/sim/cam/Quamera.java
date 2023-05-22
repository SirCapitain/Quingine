package quingine.sim.cam;

import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.sim.obj.Quobject;
import quingine.util.Quicture;

import java.awt.*;

/**
 * Simulation of a camera in 3-D space.
 * @author Quinn Graham
 */

public class Quamera {
    /**
     * Initializing the camera like this
     * will default all values to 0
     */
    public Quamera(){
        setPos(0,0,0);
        setRotation(0,0,0);
    }

    /**
     * Copies the data of one quamera to a new quamera.
     * @param quamera the quamera you want to copy over.
     */
    public Quamera(Quamera quamera){
        setPos(quamera.getPos());
        setRotation(quamera.yaw, quamera.pitch, quamera.roll);
        flashlightOn(quamera.flashlightOn());
        setLightPower(quamera.getLightPower());
        setzFar(quamera.getzFar());
        setzNear(quamera.getzNear());
        setFov(quamera.getFov());
        setDebugColor(quamera.getDebugColor());
        setDebugFont(quamera.getDebugFont());
        debug(quamera.debug());
    }

    /**
     * Initializing this way will default rotation
     * values to 0
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     */
    public Quamera(double x, double y, double z){
        setPos(x,y,z);
    }

    /**
     * The cool way of initializing a camera
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     * @param yaw rotation in 3D world
     * @param pitch rotation in 3D world
     * @param roll rotation in 3D world
     */
    public Quamera(double x, double y, double z, double yaw, double pitch, double roll){
        setPos(x,y,z);
        setRotation(yaw, pitch, roll);
    }

    /**
     * Initialize camera according to a quisition.
     * Rotational values default to 0
     * @param quisition position in 3D world
     */
    public Quamera(Quisition quisition){
        setPos(quisition);
        setRotation(0,0,0);
    }

    /**
     * Initializing a camera according to a quisition
     * and rotation.
     * @param quisition position in 3D world
     * @param yaw rotation in 3D world
     * @param pitch rotation in 3D world
     * @param roll rotation in 3D world
     */
    public Quamera(Quisition quisition, double yaw, double pitch, double roll){
        setPos(quisition);
        setRotation(yaw, pitch, roll);
    }

    private Quisition position;

    /**
     * Setting the position of the camera
     * @param pos position in 3D world
     */
    public void setPos(Quisition pos){
        if (position == null)
            position = new Quisition(0,0,0);
        position.setPos(pos);
    }

    /**
     * Setting the position of the camera
     * @param x position in 3D world
     * @param y position in 3D world
     * @param z position in 3D world
     */
    public void setPos(double x, double y, double z){
        position = new Quisition(Math.round(x*1000)/1000.0, Math.round(y*1000)/1000.0, Math.round(z*1000)/1000.0);
    }

    /**
     * Get where the camera currently is.
     * @return position in 3D world of the camera.
     */
    public Quisition getPos(){
        return position;
    }

    /**
     * Change the camera position by a specific amount.
     * @param x change position by in 3D world
     * @param y change position by in 3D world
     * @param z change position by in 3D world
     */
    public void changePosBy(double x, double y, double z){
        setPos(position.x + x, position.y + y, position.z + z);
    }

    private double yaw, pitch, roll;

    /**
     * Set the rotation of the camera.
     * @param yaw rotation in 3D world
     * @param pitch rotation in 3D world
     * @param roll rotation in 3D world
     */
    public synchronized void setRotation(double yaw, double pitch, double roll){
        this.yaw = Math.round(yaw*1000)/1000.0;
        this.pitch = Math.round(pitch*1000)/1000.0;
        this.roll = Math.round(roll*1000)/1000.0;

        this.yaw -= (int)(this.yaw / 360)*360;
        this.pitch -= (int)(this.pitch / 360)*360;
        this.roll -= (int)(this.roll / 360)*360;
    }

    /**
     * Change the camera position by a specific amount.
     * @param yaw change rotation by in 3D world
     * @param pitch change rotation by in 3D world
     * @param roll change rotation by in 3D world
     */
    public void changeRotationBy(double yaw, double pitch, double roll){
        setRotation(this.yaw + yaw, this.pitch + pitch, this.roll + roll);
    }

    public boolean rotationEquals(Quamera quamera){
        return yaw == quamera.yaw && pitch == quamera.pitch && roll == quamera.roll;
    }

    /**
     * Get the yaw value
     * @return yaw
     */
    public double getYaw(){
        return yaw;
    }
    /**
     * Get the pitch value
     * @return pitch
     */
    public double getPitch() {
        return pitch;
    }
    /**
     * Get the roll value
     * @return roll
     */
    public double getRoll() {
        return roll;
    }

    private int fov = 60;

    /**
     * Set the fov of the camera
     * @param fov degrees of the field of view.
     */
    public void setFov(int fov){
        this.fov = fov;
    }

    /**
     * Get the current value of the fov
     * @return fov.
     */
    public int getFov(){
        return fov;
    }

    private double zFar = 1000;
    private double zNear = 1;

    /**
     * Set how far the camera can see.
     * @param zFar units of how far.
     */
    public void setzFar(double zFar){
        this.zFar = zFar;
    }

    /**
     * Set how close the camera can see.
     * @param zNear units of how near.
     */
    public void setzNear(double zNear){
        this.zNear = zNear;
    }

    /**
     * Get the current value of how far the camera can see.
     * @return units of how far the camera can see.
     */
    public double getzFar(){
        return zFar;
    }

    /**
     * Get the value of how near the camera can see.
     * @return units of how near the camera can see.
     */
    public double getzNear(){
        return zNear;
    }

    private boolean debug = true;

    /**
     * Show the current rotation and position of the camera
     * @param debug true for one, false for off
     */
    public void debug(boolean debug){
        this.debug = debug;
    }

    /**
     * Check whether the debug mode is on or off.
     * @return true for one, false for off
     */
    public boolean debug(){
        return debug;
    }

    private Font debugFont = new Font(Font.DIALOG, Font.PLAIN, 10);

    /**
     * Set the font of the camera debug screen
     * @param font java font
     */
    public void setDebugFont(Font font){
        debugFont = font;
    }

    /**
     * Get the current font the debug screen is using
     * @return current font being used.
     */
    public Font getDebugFont(){
        return debugFont;
    }

    private Color debugColor = Color.white;

    /**
     * Set the color of the debug screen
     * @param color java color
     */
    public void setDebugColor(Color color){
        debugColor = color;
    }

    /**
     * Get the current color of the debug screen
     * @return current color being used.
     */
    public Color getDebugColor(){
        return debugColor;
    }

    /**
     * Paint the debug screen of the camera
     * @param g
     */
    public void paint(Graphics g){
        Color color = g.getColor();
        Font font = g.getFont();
        g.setFont(debugFont);
        g.setColor(debugColor);
        g.drawString("X: " + position.x + "   Y: " + position.y + "   Z: " + position.z, 5,15);
        g.drawString("Yaw: " + yaw + "   Pitch: " + pitch + "   Roll: " + roll, 5, 30);
        g.setFont(font);
        g.setColor(color);
    }

    /**
     * Translate a quisition to the proper place
     * @param pos quisition
     * @return translated quisition
     */
    public Quisition translate(Quisition pos){
        Quisition newPos = new Quisition(pos);
        Math3D.rotate(newPos, position, yaw, pitch, roll);
        newPos.subtract(this.position);
        return newPos;
    }

    private boolean flashlightOn = true;

    /**
     * Set the quamera to have a flashlight on or off
     * @param flashlightOn true for on, false for off
     */
    public void flashlightOn(boolean flashlightOn){
        this.flashlightOn = flashlightOn;
    }

    /**
     * Check if the quamera has the flashlight on
     * @return true if on, false if off.
     */
    public boolean flashlightOn(){
        return flashlightOn;
    }

    private double lightPower = 1;

    /**
     * Set how powerful the flashlight is. 1 is default
     * @param power amount of power
     */
    public void setLightPower(double power){
        lightPower = power;
    }

    /**
     * returns the current about of power being used for the flashlight
     * @return current about of power
     */
    public double getLightPower(){
        return lightPower;
    }

    /**
     * Get the current light level of a set of points
     * @param point the point that needs to be lit.
     * @return a value from 0 - 1 for the current light level.
     */
    public double getLightLevel(Quisition point){
        if (!flashlightOn)
            return 0;//                               The current light direction                          Current position of the point
        return Math3D.getDotProduct(Math3D.normalize( new Quisition(0,0,-1)), Math3D.normalize(new Quisition(point.x,point.y,-1))) * lightPower;
    }

    /**
     * Test if a set of points are visible to the quamera.
     * This used vectors so it matters the order of the points in the array
     * @param points array of points
     * @return true for visible, false for not.
     */
    public boolean pointsAreVisible(Quisition[] points){
        return Math3D.getDotProduct(Math3D.normalize(Math3D.getCrossProduct(points[0], points[1], points[2])), points[0]) >= 0;
    }


    /**
     * Creates a perspective frustum that transforms points to be more real.
     * @param q this quicture that the frustum is on.
     * @param pos position in 3D space.
     * @return the new position of the point
     */
    public Quisition perspective(Quicture q, Quisition pos){
        Quisition newPos = new Quisition(pos);
        if (newPos.z <= 0 || newPos.z > zFar || pos.z == position.z)
            return new Quisition(Double.NaN, Double.NaN, Double.NaN);
        newPos.setPos(
                (q.getHeight()/(double)q.getWidth())*(newPos.x/Math.tan(Math.toRadians(fov*.5)))/newPos.z,
                (newPos.y/(Math.tan(Math.toRadians(fov*.5))))/newPos.z,
                (newPos.z*(zFar/(zFar-zNear)) - (zFar*zNear)/(zFar-zNear))/newPos.z);
        return newPos;
    }
}

