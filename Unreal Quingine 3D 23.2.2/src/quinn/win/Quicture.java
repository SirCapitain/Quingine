package quinn.win;

import quinn.world.Quomponent;
import quinn.world.Quworld;
import quinn.world.obj.Quisition;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        refresh();
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
    public int getScreenPosX(double x){
        return (int)Math.round((getX()-(getQuindow().getWidth() - getWidth())/2.0)+(getWidth()*(x/getUnitsWide())));
    }
    public int getScreenPosY(double y){
        return (int)Math.round((getY()-(getQuindow().getHeight() - getHeight())/2.0)+(getHeight()*(y/getUnitsTall())));
    }
    public Quisition getScreenQuisition(Quisition quisition){
        return new Quisition(getScreenPosX(quisition.x), getScreenPosY(quisition.y), quisition.z);
    }
    public double getMatrixPosX(double x){
        return unitsWide*(x - (getX() - (getQuindow().getWidth() - getWidth())/2.0))/getWidth();
    }
    public double getMatrixPosY(double y){
        return unitsTall*(y - (getY() - (getQuindow().getHeight() - getHeight())/2.0))/getHeight();
    }
    public Quisition getMatrixQuisistion(Quisition quisition){
        return new Quisition(getMatrixPosX(quisition.x), getMatrixPosY(quisition.y), quisition.z);
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
     * The camera is build right into the Quicture.
     */
    private Quisition camera = new Quisition(0,0,0);

    /**
     * Position you want to camera to be located at.
     * @param x coordinate of camera.
     * @param y coordinate of camera.
     * @param z coordinate of camera.
     */
    public void setCameraPos(double x, double y, double z){
        setCameraPos(new Quisition(x,y,z));
    }
    public void setCameraPos(Quisition quisition){
        camera = quisition;
    }

    /**
     * Change the camera position by a certain amount.
     * @param x change by
     * @param y change by
     * @param z change by
     */
    public void changeCameraPos(double x, double y, double z){
        setCameraPos(camera.x + x, camera.y + y, camera.z + z);
    }
    public void changeCameraPos(Quisition quisition){
        setCameraPos(camera.x + quisition.x, camera.y + quisition.y, camera.z + quisition.z);
    }
    public Quisition getCameraPos(){
        return camera;
    }


    /**
     * Quixels are the way of the future.
     * Everything on this engine is rendered by
     * specific quixels!
     */

    private Quixel[][] quixels;
    private BufferedImage picture;

    public void refresh(){
        quixels = new Quixel[getWidth()][getHeight()];
        for (int w = 0; w < getWidth(); w++)
            for (int h = 0; h < getHeight(); h++)
                quixels[w][h] = new Quixel(w, h, Integer.MAX_VALUE);
        picture = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

    }
    public void setQuixel(int x, int y, double z, Color color, boolean override){
        try {
            if (override || (quixels[x][y].z > z && z > camera.z)) {
                quixels[x][y] = new Quixel(x, y, z, color);
                picture.setRGB(x, y, color.getRGB());
            }
        }catch (Exception ex){}
    }
    public void setQuixel(int x, int y, double z, Color color){
        setQuixel(x,y,z,color,false);
    }
    public void setQuixel(Quisition quisition, Color color){
        setQuixel((int)(quisition.x + .5), (int)(quisition.y+.5), quisition.z, color);
    }
    public Quixel getQuixel(int x, int y){
        return quixels[x][y];
    }

    /**
     * Painting is cool and draws cool stuff.
     * @param g graphics of this Quicture.
     */
    @Override
    public void paintComponent(Graphics g){
        refresh();
        if (lastCheck + 1000000000 < System.nanoTime()){
            FPS = fpsCount;
            lastCheck = System.nanoTime();
            fpsCount = 0;
        }
        for (int i = 0; i < quomponents.size(); i++)
            quomponents.get(i).draw();
        g.drawImage(picture,0,0,null);
        g.setColor(Color.black);
        if (showFPS)
            g.drawString("FPS: " + FPS, quindow.getWidth() - 70, 15);
        fpsCount++;
        g.setColor(Color.blue);
    }
}
