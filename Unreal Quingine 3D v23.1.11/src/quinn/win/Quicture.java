package quinn.win;

import quinn.world.Quomponent;
import quinn.world.Quworld;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Adding a canvas to draw on for the Quindow.
 * No canvas, no picture, no fun.
 * @author Quinn Graham
 */

public class Quicture extends JPanel {
    public Quicture(){}
    public Quicture(Quindow quindow){
        setSize(quindow.getWidth(), quindow.getHeight());
        quindow.add(this);
        setVisible(true);
    }

    /**
     * You may get this Quindow but not set it.
     */
    protected Quindow quindow;
    public Quindow getQuindow(){
        return quindow;
    }

    /**
     * Showing off how well you computer runs.
     * Also shows off my poorly optimized code.
     */
    private boolean showFPS;
    public void showFPS(boolean show){
        showFPS = show;
    }
    public boolean showingFPS()
    {
        return showFPS;
    }

    private ArrayList<Quomponent> quomponents = new ArrayList<>();
    public boolean add(Quomponent quomponent){
        if (!quomponents.contains(quomponent)) {
            quomponent.setQuicture(this);
            return quomponents.add(quomponent);
        }
        return false;
    }
    public boolean remove(Quomponent quomponent){
        return quomponents.remove(quomponent);
    }

    /**
     * The mapping matrix for the objects
     */
    private int unitsWide=70;
    private int unitsTall=35;

    public void setQuictureMartix(int unitsWide, int unitsTall){
        this.unitsWide = unitsWide;
        this.unitsTall = unitsTall;
    }

    public int getUnitsWide() {
        return unitsWide;
    }

    public int getUnitsTall() {
        return unitsTall;
    }

    public double getScreenPosX(double x){
        return ((getQuindow().getWidth() - getWidth())/2.0)+(getWidth()*(x/getUnitsWide()));
    }
    public double getScreenPosY(double y){
        return ((getQuindow().getHeight() - getHeight())/2.0)+(getHeight()*(y/getUnitsTall()));
    }
    /**
     * Gotta paint.
     */
    private long lastCheck;
    private int FPS, fpsCount;
    @Override
    public void paintComponent(Graphics g){
        if (lastCheck + 1000000000 < System.nanoTime()){
            FPS = fpsCount;
            lastCheck = System.nanoTime();
            fpsCount = 0;
        }
        for (int i = 0; i < quomponents.size(); i++) {
            quomponents.get(i).paint(g);
        }
        if (showFPS)
            g.drawString("FPS: " + FPS, quindow.getWidth()-70,15);
        fpsCount++;
        g.setColor(Color.blue);
    }
}
