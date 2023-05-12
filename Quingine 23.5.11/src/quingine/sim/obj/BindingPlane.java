package quingine.sim.obj;

import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.util.Quaphics;
import quingine.util.Quicture;

import java.awt.*;
import java.util.Arrays;

/**
 * This class allows you to make a binding plane that
 * attaches to a quobject to simulate a face.
 * @author Quinn Graham
 */

public class BindingPlane {

    /**
     * Initializing a binding plane means that is takes the pointers
     * to the points of the object you want this to bind to.
     * @param points the points that the plane will bind to.
     */
    public BindingPlane(Quisition[] points){
        this.points = points;
    }
    private Quisition[] points;

    /**
     * Get the current list of points that this plane is bind to.
     * @return the current list of points
     */
    public Quisition[] getPoints(){
        return points;
    }

    private boolean fill = true;

    /**
     * If that plane can fill itself with color
     * @param fill true if you want it to fill. False if not.
     */
    public void fill(boolean fill){
        this.fill = fill;
    }

    /**
     * Is the current plane filling or not?
     * @return true if it is filling, false if not.
     */
    public boolean fill(){
        return fill;
    }


    private boolean outline = false;
    /**
     * Set the current plane to outline.
     * @param outline true if outline, false if not
     */
    public void outline(boolean outline){
        this.outline = outline;
    }

    /**
     * Check if the plane is being outlined
     * @return true if being outlined. False if not.
     */
    public boolean outline(){
        return outline;
    }

    Color color = Color.black;

    /**
     * Set the current color the plane will use to fill itself.
     * That is if fill it set to true.
     * @param color what color you want.
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Get the current color the plane is using.
     * @return the current color.
     */
    public Color getColor(){
        return color;
    }

    private boolean visible = true;

    /**
     * Set a plane to be visible or not
     * @param visible true if visible, false if not
     */
    public void visible(boolean visible){
        this.visible = visible;
    }

    /**
     * Check if a plane is visible or not.
     * @return true if visible, false if not
     */
    public boolean visible(){
        return visible;
    }

    /**
     * Easy what to see the binding plane in string form.
     * @return a string.
     */
    @Override
    public String toString() {
        return "BindingPlane{" +
                "points=" + Arrays.toString(points) +
                '}';
    }

    /**
     * The plane paints itself using the points it was given.
     * @param q The current quicture the plane is on.
     */
    public void paint(Quicture q, Quisition cameraPos){
        if (!visible)
            return;
        //Translate Points
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++)
            newPoints[i] = q.getQuamera().translate(points[i], cameraPos);

        //Check if face is visible to camera
        Quisition nv = Math3D.getCrossProduct(newPoints[0], newPoints[1], newPoints[2]);
        double v = Math3D.getNorm(nv);
        nv.divide(v);
        if (Math3D.getDotProduct(nv, newPoints[0]) >= 0)
            return;

        

        //Lighting
        double lv = 1;
        if (q.getQuamera().flashlightOn()) {
            Quisition light = new Quisition(0,0,-1);
            Quisition light2 = new Quisition(newPoints[0].x,newPoints[0].y,-1);
            double l = Math3D.getNorm(light);
            double l2 = Math3D.getNorm(light2);
            light.divide(l);
            light2.divide(l2);
            lv = Math3D.getDotProduct(light, light2);
        }

        //Change to perspective
        for (int i = 0; i < newPoints.length; i++)
            newPoints[i] = q.getQuamera().perspective(q, newPoints[i]);

        //Draw
        int red = (int)Math.round(color.getRed()*(lv));
        int green = (int)Math.round(color.getGreen()*(lv));
        int blue = (int)Math.round(color.getBlue()*(lv));
        red = red < 20 ? 20 : Math.min(red, 255);
        green = green < 20 ? 20 : Math.min(green, 255);
        blue = blue < 20 ? 20 : Math.min(blue, 255);

        Quaphics.setColor(new Color(red,green,blue));
        if (fill)
            Quaphics.fillPolygon(newPoints, q);
        Quaphics.setColor(Color.black);
        if (outline)
            Quaphics.drawPolygon(newPoints, q);
    }
}
