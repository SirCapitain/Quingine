package quingine.render.sim.cam;

import quingine.render.sim.env.Quworld;
import quingine.render.sim.pos.Quisition;
import quingine.render.sim.Math3D;
import quingine.render.util.Quaphics;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Simulation of a camera in 3-D space.
 */

public class Quamera {

    private volatile Quisition position;
    private volatile double yaw, pitch, roll;
    private Quisition tempPos;
    private double tempYaw, tempPitch, tempRoll;

    private double fov = Math.PI * .5;
    private double zFar = 1000;
    private double zNear = .001;

    private boolean debug = true;
    private Font debugFont = new Font(Font.DIALOG, Font.PLAIN, 10);
    private Color debugColor = Color.black;

    private boolean update = true;

    private BufferedImage picture;
    private double[][] pixelsZ;
    private int width = 800;
    private int height = 500;
    private int unitsWide = 2;
    private int unitsTall = 2;
    private int backgroundColor = Color.BLACK.getRGB();

    private final Quisition LEFT_POS = new Quisition(-unitsWide / 2.0, 0, 0);
    private final Quisition LEFT_NORMAL = new Quisition(1, 0, 0);
    private final Quisition RIGHT_POS = new Quisition(unitsWide / 2.0, 0, 0);
    private final Quisition RIGHT_NORMAL = new Quisition(-1, 0, 0);
    private final Quisition UP_POS = new Quisition(0, unitsTall / 2.0, 0);
    private final Quisition UP_NORMAL = new Quisition(0, -1, 0);
    private final Quisition DOWN_POS = new Quisition(0, -unitsTall / 2.0, 0);
    private final Quisition DOWN_NORMAL = new Quisition(0, 1, 0);
    private final Quisition Z_FAR_POS = new Quisition(0,0, zFar);
    private final Quisition Z_FAR_NORMAL = new Quisition(0,0, -1);
    private final Quisition Z_NEAR_POS = new Quisition(0,0, zNear);
    private final Quisition Z_NEAR_NORMAL = new Quisition(0,0, 1);

    private int forward = KeyEvent.VK_W;
    private int backward = KeyEvent.VK_S;
    private int left = KeyEvent.VK_D;
    private int right = KeyEvent.VK_A;
    private int up = KeyEvent.VK_E;
    private int down = KeyEvent.VK_Q;
    private int lookUp = KeyEvent.VK_UP;
    private int lookDown = KeyEvent.VK_DOWN;
    private int lookLeft = KeyEvent.VK_LEFT;
    private int lookRight = KeyEvent.VK_RIGHT;
    private int lookClockwise = KeyEvent.VK_PAGE_DOWN;
    private int lookCounterClockwise = KeyEvent.VK_PAGE_UP;

    /**
     * Initializing the camera like this
     * will default all values to 0
     */
    public Quamera(){
        setPos(0,0,0);
        setRotation(0,0,0);
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
        if (update)
            position = new Quisition(Math.round(x*10000)/10000.0, Math.round(y*10000)/10000.0, Math.round(z*10000)/10000.0);
        tempPos = new Quisition(Math.round(x*10000)/10000.0, Math.round(y*10000)/10000.0, Math.round(z*10000)/10000.0);
    }

    /**
     * Get where the camera currently is.
     * @return position in 3D world of the camera.
     */
    public Quisition getPos(){
        return Objects.requireNonNullElseGet(position, () -> new Quisition(0, 0, 0));
    }

    /**
     * Change the camera position by a specific amount.
     * @param x change position by in 3D world
     * @param y change position by in 3D world
     * @param z change position by in 3D world
     */
    public void changePosBy(double x, double y, double z){
        setPos(tempPos.x + x, tempPos.y + y, tempPos.z + z);
    }


    /**
     * Set the rotation of the camera.
     * @param yaw rotation in 3D world
     * @param pitch rotation in 3D world
     * @param roll rotation in 3D world
     */
    public synchronized void setRotation(double yaw, double pitch, double roll){

        yaw -= ((int)(yaw/Math.TAU))*Math.TAU;
        pitch -= ((int)(pitch/Math.TAU))*Math.TAU;
        roll -= ((int)(roll/Math.TAU))*Math.TAU;

        yaw = Math.round(yaw*10000)/10000.0;
        pitch = Math.round(pitch*10000)/10000.0;
        roll = Math.round(roll*10000)/10000.0;

        if (update){
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }
        tempYaw = yaw;
        tempPitch = pitch;
        tempRoll = roll;
    }

    /**
     * Change the camera position by a specific amount.
     * @param yaw change rotation by in 3D world
     * @param pitch change rotation by in 3D world
     * @param roll change rotation by in 3D world
     */
    public void changeRotationBy(double yaw, double pitch, double roll){
        setRotation(tempYaw + yaw, tempPitch + pitch, tempRoll + roll);
    }

    /**
     * Check is the rotation of one quamera
     * equals the rotation of another.
     * @param quamera the quamera to test against
     * @return true is equal, false is not.
     */
    public boolean rotationEquals(Quamera quamera){
        return yaw == quamera.yaw && pitch == quamera.pitch && roll == quamera.roll;
    }

    /**
     * Move the quamera forward in respect to the yaw
     * @param dist distance to travel
     */
    public void moveForward(double dist){
        changePosBy(-dist * Math.sin(yaw), 0, dist * Math.cos(yaw));
    }

    /**
     * Move the quamera sideways in respect to yaw
     * @param dist distance to travel
     */
    public void moveSideways(double dist){
        changePosBy(dist * Math.cos(yaw), 0, dist * Math.sin(yaw));
    }


    /**
     * Set the controls for moving the quamera
     *
     * @param forward key to move quamera forward
     * @param backward key to move quamera backward
     * @param left key to move quamera left
     * @param right key to move quamera right
     * @param up key to move quamera up
     * @param down key to move quamera down
     */
    public void setMovementBinds(int forward, int backward, int left, int right, int up, int down){
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    /**
     * Set the controls for the quamera rotation
     *
     * @param up key to look up
     * @param down key to look down
     * @param left key to look left
     * @param right key to look right
     * @param clockwise key to look clockwise
     * @param counterClockwise key to look counterclockwise
     */
    public void setRotationBinds(int up, int down, int left, int right, int clockwise, int counterClockwise){
        lookUp = up;
        lookDown = down;
        lookLeft = left;
        lookRight = right;
        lookClockwise = clockwise;
        lookCounterClockwise = counterClockwise;
    }

    /**
     * Returns the currently used keybinds for the quamera movement.
     * @return array of keybinds: {forward, backward, left, right}
     */
    public int[] getKeybinds(){
        return new int[]{forward, backward, left, right, up, down,
                         lookUp, lookDown, lookLeft, lookRight, lookClockwise, lookCounterClockwise};
    }

    /**
     * Update the movement of the quamera based off of the speed given.
     *
     * @param movSpeed distance to travel
     * @param rotSpeed amount to rotate by
     * @param window which window to listen for keys
     */
    public void updateMovement(double movSpeed, double rotSpeed, Quindow window){
        if (window.isKeyDown(left))
            moveSideways(movSpeed);
        if (window.isKeyDown(forward))
           moveForward(movSpeed);
        if (window.isKeyDown(right))
            moveSideways(-movSpeed);
        if (window.isKeyDown(backward))
            moveForward(-movSpeed);
        if (window.isKeyDown(down))
            changePosBy(0, -movSpeed, 0);
        if (window.isKeyDown(up))
            changePosBy(0, movSpeed, 0);
        if (window.isKeyDown(lookUp))
            changeRotationBy(0, rotSpeed, 0);
        if (window.isKeyDown(lookDown))
            changeRotationBy(0, -rotSpeed, 0);
        if (window.isKeyDown(lookRight))
            changeRotationBy(-rotSpeed, 0, 0);
        if (window.isKeyDown(lookLeft))
            changeRotationBy(rotSpeed, 0, 0);
        if (window.isKeyDown(lookClockwise))
            changeRotationBy(0, 0, rotSpeed);
        if (window.isKeyDown(lookCounterClockwise))
            changeRotationBy(0, 0, -rotSpeed);
    }

    /**
     * Update the position of the quamera and
     * its rotation constantly.
     * @param update true if always updating, false it not.
     */
    public void updateComponents(boolean update){
        if (update)
            update();
        this.update = update;
    }

    /**
     * Update the components of the quamera.
     */
    public void update(){
        if (tempPos != null)
            position = new Quisition(tempPos);
        yaw = tempYaw;
        pitch = tempPitch;
        roll = tempRoll;
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

    /**
     * Set the fov of the camera
     * @param fov radians of the field of view.
     */
    public void setFov(double fov){
        this.fov = fov;
    }

    /**
     * Get the current value of the fov
     * @return fov.
     */
    public double getFov(){
        return fov;
    }

    /**
     * Set how far the camera can see.
     * @param zFar units of how far.
     */
    public void setZFar(double zFar){
        this.zFar = zFar;
        Z_FAR_POS.setPos(0,0,zFar);
    }

    /**
     * Set how close the camera can see.
     * @param zNear units of how near.
     */
    public void setZNear(double zNear){
        this.zNear = zNear;
        Z_NEAR_POS.setPos(0,0,zNear);
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
     * @param g the graphics from the quicture
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
     */
    public void translate(Quisition pos){
        Math3D.rotate(pos, position, yaw, pitch, roll);
        pos.subtract(this.position);
    }

    /**
     * Test if a set of points are visible to the quamera.
     * This used vectors, so it matters the order of the points in the array
     * This does not check for visibility from behind, too close, or too far from the
     * quamera but instead if the normal vector of a plane is pointing away.
     * @param points array of points
     * @return true for visible, false for not.
     */
    public boolean pointsAreVisible(ArrayList<Quisition> points){
        return (Math3D.getDotProduct(Math3D.getNormal(points.get(0), points.get(1), points.get(2)), points.get(0)) >= 0);
    }

    /**
     * Get the current vector the camera has.
     * @return double[]{vecX, vecY, vecZ}
     */
    public Quisition getVector(){
        return new Quisition(-Math.sin(yaw) * Math.cos(pitch), Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch));
    }

    /**
     * Clip a set of non-perspective points against the
     * zFar and zNear planes of the perspective frustum.
     * @param points list of non-perspective points
     */
    public void clipZPlanes(ArrayList<Quisition> points){
        //zNear
        Math3D.clipObject(points, Z_NEAR_POS, Z_NEAR_NORMAL);
        if (points.isEmpty())
            return;
        //zFar
        Math3D.clipObject(points, Z_FAR_POS, Z_FAR_NORMAL);
    }


    /**
     * Clip a set of perspective points against the screen matrix.
     * @param points points of a triangle(s) on an arraylist.
     */
    public void clipScreenEdges(ArrayList<Quisition> points){
        Math3D.clipObject(points, RIGHT_POS, RIGHT_NORMAL);//Right side of screen
        Math3D.clipObject(points,LEFT_POS, LEFT_NORMAL);//Left side of screen
        Math3D.clipObject(points, UP_POS, UP_NORMAL);//Top of screen
        Math3D.clipObject(points, DOWN_POS, DOWN_NORMAL);//Bottom of screen
    }


    /**
     * Creates a perspective frustum that transforms points to be more real.
     * @param pos position in 3D space.
     */
    public void perspective(Quisition pos){
        double w = 1/pos.z;
        if (pos.data != null) {
            pos.data[0] = w;
            pos.data[1] *= w;
            pos.data[2] *= w;
        }
        pos.setPos(
                (height/(double)width)*(pos.x/Math.tan(fov*.5))*w,
                (pos.y/(Math.tan(fov*.5)))*w,
                (pos.z*(zFar/(zFar-zNear)) - (zFar*zNear)/(zFar-zNear))*w);
    }

    /**
     * Get the current height in pixels
     * of the quamera
     * @return pixels high
     */
    public int getHeight(){
        return height;
    }

    /**
     * Get the current width in pixels
     * of the quamera
     * @return pixels wide
     */
    public int getWidth(){
        return width;
    }

    /**
     * Like the movie, but not really.
     * How may units across and tall you want the screen
     * @param wide units wide
     * @param tall units tall
     */
    public void setMatrix(int wide, int tall){
        unitsWide = wide;
        unitsTall = tall;
        LEFT_POS.setPos(-unitsWide / 2.0, 0, 0);
        RIGHT_POS.setPos(unitsWide / 2.0, 0, 0);
        UP_POS.setPos(0, unitsTall / 2.0, 0);
        DOWN_POS.setPos(0, -unitsTall / 2.0, 0);
    }

    /**
     * Get the units wide value
     * @return units wide the picture is
     */
    public int getUnitsWide(){
        return unitsWide;
    }

    /**
     * get the units tall
     * @return units tall the picture is
     */
    public int getUnitsTall(){
        return unitsTall;
    }

    /**
     * Refreshing the screen is so refreshing!
     * If called, this method deletes the old list
     * of pixels and creates a new one and resets
     * the picture.
     */
    public void refresh(){
        int width = this.width;
        int height = this.height;
        pixelsZ = new double[width][height];
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int w = 0; w < width; w++)
            for (int h = 0; h < height; h++) {
                pixelsZ[w][h] = Integer.MAX_VALUE;
                ((DataBufferInt) picture.getRaster().getDataBuffer()).getData()[h*width + w] = backgroundColor;
            }
    }

    /**
     * Setting the specific pixel on the screen.
     * @param x screen position
     * @param y screen position
     * @param z value between 1 & -1
     * @param color color you want the pixel to be.
     */
    public synchronized void setPixel(int x, int y, double z, int color){
        if (x >= pixelsZ.length || x < 0 || y >= pixelsZ[0].length || y < 0)
            return;
        if (pixelsZ[x][y] >= z && z > 0 && z < 1) {
            pixelsZ[x][y] = (float)z;
            ((DataBufferInt) picture.getRaster().getDataBuffer()).getData()[y*width + x] = color;
        }
    }

    /**
     * Set the resolution of the quamera
     * @param width pixels wide
     * @param height pixles tall
     */
    public void setResolution(int width, int height){
        this.height = height;
        this.width = width;
    }

    /**
     * Getting what position something is on the matrix to the screen.
     * @param x matrix position
     * @return x screen position
     */
    public int getScreenPosX(double x){
        return (int)Math.round((width*(unitsWide/2.0 + x))/unitsWide);
    }
    /**
     * Getting what position something is on the matrix to the screen.
     * @param y matrix position
     * @return y screen position
     */
    public int getScreenPosY(double y){
        return (int)Math.round((height*(unitsTall/2.0 - y))/unitsTall);
    }

    /**
     * Getting what position something is on the screen to the matrix.
     * @param x screen position
     * @return x matrix position
     */
    public double getMatrixPosX(double x){
        return unitsWide * x/width - unitsWide/2.0;
    }
    /**
     * Getting what position something is on the screen to the matrix.
     * @param y screen position
     * @return y matrix position
     */
    public double getMatrixPosY(double y){
        return -(unitsTall * y / height - unitsTall/2.0);
    }

    /**
     * Get the current picture the quamera has.
     * @return BufferedImage of the current image the quamera has.
     */
    public BufferedImage getPicture(){
        return picture;
    }

    /**
     * Update the picture the quamera holds
     * @param pic the Quicture to take a picture in.
     */
    public void takePicture(Quicture pic){
        refresh();
        Quworld world = pic.getWorld();
        if (world != null)
            world.paint(pic, this);
        for (int i = 0; i < pic.getQuomponents().size(); i++)
            pic.getQuomponents().get(i).paint(pic, this);

    }

    /**
     * Refresh and return the picture.
     * @param pic the Quicture being used
     * @return BufferedImage of current picture of quamera
     */
    public BufferedImage getNewPicture(Quicture pic){
        takePicture(pic);
        return getPicture();
    }

    /**
     * Set the background color of the image taken
     * by the quamera
     * @param color color integer
     */
    public void setBackgroundColor(int color){
        backgroundColor = color;
    }
}

