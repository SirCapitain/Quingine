package quinn.world.obj;

import quinn.win.Quicture;
import quinn.world.Quomponent;

import java.awt.*;


/**
 * Holds The info for a 3D object using Quisitions.
 * @author Quinn Graham
 */

public class Quobject extends Quomponent {

    /**
     *
     * @param x coordinate of object center.
     * @param y coordinate of object center.
     * @param z coordinate of object center.
     * @param points list of points that make the quobject a quobject.
     */
    public Quobject(double x, double y, double z, Quisition[] points){
        setPos(x, y, z);
        this.points = points;
    }
    public Quobject(){}
    public Quobject(double x, double y, double z){
        setPos(x,y,z);
    }
    public Quobject(Quisition quisition, Quisition[] points){
        setPos(quisition);
        this.points = points;
    }

    /**
     * Setting the position of the shape also requires moving all the points.
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    @Override
    public void setPos(double x, double y, double z){
        setPos(new Quisition(x,y,z));
    }
    @Override
    public void setPos(Quisition quisition){
        if (points != null)
            for (Quisition point : points)
                point.setPos(point.x + getPos().x - quisition.x, point.y + getPos().y - quisition.y,point.z + getPos().z - quisition.z);
        this.quisition = quisition;
    }

    /**
     * A list of points that the shape contains to make it
     * look like a shape.
     */
    private Quisition[] points;
    public Quisition[] getPoints(){
        return points;
    }
    public void setPoints(Quisition[] points){
        this.points = points;
    }

    /**
     * Rotating a quobject requires rotating all the points that are a part of it.
     * @param theta Degrees turned
     * @param vx Vector x. rotates around x
     * @param vy Vector y. rotates around y
     * @param vz Vector z. rotates around z
     *
     * NOTE! x^2 + y^2 + z^2 = 1! Rotating otherwise may cause issues!
     */
    public void rotate(double theta, double vx, double vy, double vz){
        rotate(theta, vx, vy, vz, quisition);
    }
    public void rotate(double theta, double vx, double vy, double vz, Quisition quisition){
        for (Quisition point : points)
            point.rotate(theta, vx, vy,vz, quisition);
        this.quisition.rotate(theta, vx, vy, vz, quisition);
    }
    public void rotate(double theta, double vx, double vy, double vz, double x, double y, double z){
        rotate(theta, vx, vy, vz, new Quisition(x, y, z));
    }

    /**
     * *sigh* they all grown way to quick...
     *
     * If a quobject is a part of a 3D quobject then this quobject will know
     * who that is.
     */
    private Quobject3D parentQuobject;

    /**
     * @param quobject parent quobject3D that is holding this quobject.
     */
    public void setParentQuobject(Quobject3D quobject){
        parentQuobject = quobject;
    }
    public Quobject3D getParentQuobject(){
        return parentQuobject;
    }

    /**
     * Show all colors of the rainbow!
     * Fill your quobject with any color you can think of.
     */
    private Color fillColor = Color.BLACK;
    public void setFillColor(Color color){
        fillColor = color;
    }
    public Color getFillColor(){
        return fillColor;
    }

    /**
     * Do you want to fill it with a color though?
     * Sometimes, these shapes are just so transparent.
     */
    private boolean fill;
    public void fill(boolean fill) {
        this.fill = fill;
    }
    public boolean fill(){
        return fill;
    }

    /**
     * Outlining a shapes offers a much better
     * viewing pleasure for the eyes.
     * Unless you are weird and hate the outline.
     */
    private boolean outline = true;
    public void outlined(boolean outlined){
        outline = outlined;
    }
    public boolean outlined(){
        return outline;
    }

    /**
     * Just a black outline getting you down?
     * Well be sad no more! You can change the color
     * if this outline to all the colors of the rainbow!
     * WHOA! That's cool!
     */
    private Color outlineColor = Color.black;
    public void setOutlineColor(Color color){
        outlineColor = color;
    }
    public Color getOutlineColor(){
        return outlineColor;
    }

    /**
     *Perspective is cool but at times can make weird stuff happen.
     * Disable if you don't like perspective.
     */
    private boolean isPerspective = true;
    public void isPerspective(boolean isPerspective){
        this.isPerspective = isPerspective;
    }
    public boolean isPerspective(){
        return isPerspective;
    }

    /**
     * You can paint without pain.
     * @param g graphics from Quicture
     */
    @Override
    public void paint(Graphics g) {
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            if (pic != null && parentQuobject != null && isPerspective) {
                xPoints[i] = (int) Math.round(pic.getScreenPosX(getParentQuobject().calcDist(getPoints()[i]).x + world.getCameraPos().x));
                yPoints[i] = (int) Math.round(pic.getScreenPosY(getParentQuobject().calcDist(getPoints()[i]).y + world.getCameraPos().y));
            }else if (pic != null && isPerspective) {
                xPoints[i] = (int) Math.round(pic.getScreenPosX(calcDist(getPoints()[i]).x + world.getCameraPos().x));
                yPoints[i] = (int) Math.round(pic.getScreenPosY(calcDist(getPoints()[i]).y + world.getCameraPos().y));
            }else if (pic != null) {
                xPoints[i] = (int) Math.round(pic.getScreenPosX(getPoints()[i].x + world.getCameraPos().x));
                yPoints[i] = (int) Math.round(pic.getScreenPosY(getPoints()[i].y + world.getCameraPos().y));
            }else if (parentQuobject != null && isPerspective) {
                xPoints[i] = (int) Math.round(parentQuobject.calcDist(getPoints()[i]).x + world.getCameraPos().x);
                yPoints[i] = (int) Math.round(parentQuobject.calcDist(getPoints()[i]).y + world.getCameraPos().y);
            }else if (isPerspective){
                xPoints[i] = (int) Math.round(calcDist(getPoints()[i]).x + world.getCameraPos().x);
                yPoints[i] = (int) Math.round(calcDist(getPoints()[i]).y + world.getCameraPos().y);
            }else{
                xPoints[i] = (int) Math.round(getPoints()[i].x + world.getCameraPos().x);
                yPoints[i] = (int) Math.round(getPoints()[i].y + world.getCameraPos().y);
            }
        }

        if (fill) {
            g.setColor(fillColor);
            g.fillPolygon(xPoints, yPoints, points.length);
        }
        if (outline) {
            g.setColor(outlineColor);
            g.drawPolygon(xPoints, yPoints, points.length);
        }
    }
}
