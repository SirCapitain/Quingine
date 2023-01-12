package quinn.world.obj;

import java.awt.*;

/**
 * The 2D pla- I mean Qulane that makes up 3D objects.
 *
 * @author Quinn Graham
 */

public class Qulane extends Quobject {
    public double width, height;
    public static final int RECTANGLE = 0;
    public static final int TRIANGLE = 1;
    public Qulane(double width, double height, double x, double y, double z, int shape){
        super(x,y,z);
        setDimensions(width,height);
        if (shape == 1)
        setPoints(new Quisition[]{new Quisition(x, y-height/2, z),//Top
                                       new Quisition(x-width/2, y+height/2, z),//Bottom Left
                                       new Quisition(x+width/2, y+height/2, z)});//Bottom Right
        else
            setPoints(new Quisition[]{new Quisition(x-width/2, y-height/2, z),//Top Left
                    new Quisition(x-width/2, y+height/2, z),//Bottom Left
                    new Quisition(x+width/2, y+height/2, z),//Bottom Right
                    new Quisition(x+width/2, y-height/2, z)});//Top Right
    }
    public void setDimensions(double width, double height){
        this.height = height;
        this.width = width;
    }
    private Color fillColor = Color.BLACK;
    public void setFillColor(Color color){
        fillColor = color;
    }
    public Color getFillColor(){
        return fillColor;
    }

    private boolean fill;
    public void fill(boolean fill) {
        this.fill = fill;
    }
    public boolean fill(){
        return fill;
    }

    private boolean outline = true;
    public void outline(boolean outline){
        this.outline = outline;
    }
    public boolean outline(){
        return outline;
    }

    private Color outlineColor = Color.black;
    public void setOutlineColor(Color color){
        outlineColor = color;
    }
    public Color getOutlineColor(){
        return outlineColor;
    }
    @Override
    public void paint(Graphics g) {
        g.setColor(fillColor);
        if (fill) {
            int[] xPoints = new int[getPoints().length];
            int[] yPoints = new int[getPoints().length];
            for (int i = 0; i < getPoints().length; i++) {
                if (pic != null) {
                    xPoints[i] = (int) Math.round(pic.getScreenPosX(getPoints()[i].x));
                    yPoints[i] = (int) Math.round(pic.getScreenPosY(getPoints()[i].y));
                }else {
                    xPoints[i] = (int) Math.round(getPoints()[i].x);
                    yPoints[i] = (int) Math.round(getPoints()[i].y);
                }
            }
            g.fillPolygon(xPoints, yPoints, getPoints().length);
        }
        if (outline) {
            g.setColor(outlineColor);
            super.paint(g);
        }
        else
            super.paint(g);
    }
}
