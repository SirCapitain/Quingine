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
        quindow.setQuicture(this);
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

    /**
     * List of quomponents that are a part of the Quicture for it to draw.
     */
    private ArrayList<Quomponent> quomponents = new ArrayList<>();

    /**
     * Add any quomponent to the quicture so it can draw it.
     *
     * @param quomponent that you want to add to the quicture
     * @return true if successfully added, false it failed.
     */
    public boolean add(Quomponent quomponent){
        if (!quomponents.contains(quomponent)) {
            quomponent.setQuicture(this);
            return quomponents.add(quomponent);
        }
        return false;
    }

    /**
     * Sometime you just got to remove something.
     *
     * @param quomponent you want to remove
     * @return true if successfully removed, false if failed.
     */
    public boolean remove(Quomponent quomponent){
        return quomponents.remove(quomponent);
    }

    /**
     * The mapping matrix for the quobjects to be on.
     * This dynamically changes with window size.
     */
    private int unitsWide=70;
    private int unitsTall=35;

    /**
     * Like taking the red pill but cooler.
     * @param unitsWide How many units wide you want to screen to be.
     * @param unitsTall How many units tall you want the screen to be/
     */
    public void setQuictureMartix(int unitsWide, int unitsTall){
        this.unitsWide = unitsWide;
        this.unitsTall = unitsTall;
        quindow.resizeQuicture();
    }

    public int getUnitsWide() {
        return unitsWide;
    }

    public int getUnitsTall() {
        return unitsTall;
    }

    /**
     * Getting the screen position of a position.
     * Due to the matrix, positions are not the pixel positions for the screen.
     * This method is necessary to change the positions so it can be drawn.
     * @param x coordinate that you need to get the screen position of.
     * @return the new position.
     */
    public double getScreenPosX(double x){
        return (getX()-(getQuindow().getWidth() - getWidth())/2.0)+(getWidth()*(x/getUnitsWide()));
    }
    public double getScreenPosY(double y){
        return (getY()-(getQuindow().getHeight() - getHeight())/2.0)+(getHeight()*(y/getUnitsTall()));
    }
    /**
     * The current fps that your game is running at.
     */
    private long lastCheck;
    private int FPS, fpsCount;
    public int getFPS(){
        return FPS;
    }

    /**
     * Painting is cool and draws cool stuff.
     * @param g graphics of this Quicture.
     */
    @Override
    public void paintComponent(Graphics g){
        if (lastCheck + 1000000000 < System.nanoTime()){
            FPS = fpsCount;
            lastCheck = System.nanoTime();
            fpsCount = 0;
        }
        for (int i = 0; i < quomponents.size(); i++) {
            quomponents.get(i).paint(g);
            g.setColor(Color.black);
        }
        if (showFPS)
            g.drawString("FPS: " + FPS, quindow.getWidth() - 70, 15);
        fpsCount++;
        g.setColor(Color.blue);
    }
}
