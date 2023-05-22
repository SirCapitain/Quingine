package quingine.sim.obj;

import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.sim.cam.Quamera;
import quingine.util.Quaphics;
import quingine.util.Quicture;
import quingine.util.Quindow;

import java.awt.*;
import java.util.ArrayList;
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

    private Color outlineColor = Color.black;

    /**
     * Set the color that outlines will appear as
     * @param color Color you want.
     */
    public void setOutlineColor(Color color){
        outlineColor = color;
    }

    /**
     * Get the current outline color being used
     * @return current outline color being used.
     */
    public Color getOutlineColor(){
        return outlineColor;
    }

    Color color = Color.white;

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
     * @param camera The current camera being used.
     */
    public void paint(Quicture q, Quamera camera){
        if (!visible)
            return;
        //Translate Points
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++)
            newPoints[i] = camera.translate(points[i]);

        //Check if face is visible to camera
        if(camera.pointsAreVisible(newPoints))
            return;

        //Lighting
        double lv = camera.getLightLevel(newPoints[0]);

        //Make clipping plane at zNear and clip objects that reach over
        newPoints = Math3D.clipObject(newPoints, new Quisition(0,0,camera.getzNear()), new Quisition(0,0,1));
        if (newPoints == null)
            return;


        //Change to perspective
        for (int i = 0; i < newPoints.length; i++)
            newPoints[i] = camera.perspective(q, newPoints[i]);

        //Color
        int red = (int)Math.round(color.getRed()*(lv));
        int green = (int)Math.round(color.getGreen()*(lv));
        int blue = (int)Math.round(color.getBlue()*(lv));
        red = red < 20 ? 20 : Math.min(red, 255);
        green = green < 20 ? 20 : Math.min(green, 255);
        blue = blue < 20 ? 20 : Math.min(blue, 255);

        //Draw
        for (int i = 0; i < newPoints.length / 3; i++) {//Specifically only draw triangles.
            Quaphics.setColor(new Color(red,green,blue));
            if (fill)
                Quaphics.fillPolygon(new Quisition[]{newPoints[i * 3], newPoints[i * 3 + 1], newPoints[i * 3 + 2]}, q);
            Quaphics.setColor(outlineColor);
            if (outline)
                Quaphics.drawPolygon(new Quisition[]{newPoints[i * 3], newPoints[i * 3 + 1], newPoints[i * 3 + 2]}, q);
        }

    }
}
