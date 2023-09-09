package quingine.util.win;

import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

/**
 * Every Quindow needs a Quicture to produce an image.
 * This class also sets the picture matrix for positioning.
 * @author Quinn Graham
 */

public class Quicture extends JPanel {
    /**
     * Initializing the quicture
     * @param quindow the window you want to hold the quicture
     */
    public Quicture(Quindow quindow){
        quindow.add(this);
        this.quindow = quindow;
        setSize(quindow.getSize());
        setVisible(true);
        setCamera(new Quamera());
        refresh();
    }

    private Quindow quindow;
    /**
     * Retrieve what quindow a quicture is using.
     * @return the current quindow being used.
     */
    public Quindow getQuindow(){
        return quindow;
    }

    private Quamera camera;
    /**
     * Set what camera the quicture is using
     * @param quamera a camera
     */
    public void setCamera(Quamera quamera){
        camera = quamera;
    }

    /**
     * Get what camera the quicture is using.
     * @return the current camera being used.
     */
    public Quamera getQuamera(){
        return camera;
    }

    private float[][] pixelsZ;
    private volatile BufferedImage picture;
    /**
     * Refreshing the screen is so refreshing!
     * If called, this method deletes the old list
     * of pixels and creates a new one and resets
     * the picture.
     */
    private void refresh(){
        int width = getResWidth();
        int height = getResHeight();
        pixelsZ = new float[width][height];
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int w = 0; w < width; w++)
            for (int h = 0; h < height; h++)
                pixelsZ[w][h] = Integer.MAX_VALUE;
    }

    /**
     * Setting the specific pixel on the screen.
     * @param x screen position
     * @param y screen position
     * @param z value between 1 & -1
     * @param color color you want the pixel to be.
     */
    public synchronized void setPixel(int x, int y, double z, Color color){
        if (x >= pixelsZ.length || x < 0 || y >= pixelsZ[0].length || y < 0)
            return;
        if (pixelsZ[x][y] >= z && z > 0 && z < 1) {
            pixelsZ[x][y] = (float)z;
            ((DataBufferInt) picture.getRaster().getDataBuffer()).getData()[y*getResWidth() + x] = color.getRGB();
        }
    }

    private int unitsWide = 2;
    private int unitsTall = 2;

    /**
     * Like the movie, but not really.
     * How may units across and tall you want the screen
     * @param wide units wide
     * @param tall units tall
     */
    public void setMatrix(int wide, int tall){
        unitsWide = wide;
        unitsTall = tall;
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
     * Getting what position something is on the matrix to the screen.
     * @param x matrix position
     * @return x screen position
     */
    public int getScreenPosX(double x){
        return (int)Math.round((getResWidth()*(unitsWide/2.0 + x))/unitsWide);
    }
    /**
     * Getting what position something is on the matrix to the screen.
     * @param y matrix position
     * @return y screen position
     */
    public int getScreenPosY(double y){
        return (int)Math.round((getResHeight()*(unitsTall/2.0 - y))/unitsTall);
    }

    /**
     * Getting what position something is on the screen to the matrix.
     * @param x screen position
     * @return x matrix position
     */
    public double getMatrixPosX(double x){
        return unitsWide * x/getResWidth() - unitsWide/2.0;
    }
    /**
     * Getting what position something is on the screen to the matrix.
     * @param y screen position
     * @return y matrix position
     */
    public double getMatrixPosY(double y){
        return -(unitsTall * y / getResHeight() - unitsTall/2.0);
    }

    private ArrayList<Quomponent> quomponents = new ArrayList<>();

    /**
     * Add what quomponents you want drawn.
     * @param quomponent the quomponent you want added.
     * @return true if successfully added, false if already present.
     */
    public boolean add(Quomponent quomponent){
        if (quomponents.contains(quomponent))
            return false;
        else
            return quomponents.add(quomponent);
    }

    /**
     * Remove what quomponent you don't want it to draw.
     * @param quomponent the quomponent you want to remove
     * @return true if successfully removed, false is already removed.
     */
    public boolean remove(Quomponent quomponent){
        return quomponents.remove(quomponent);
    }

    /**
     * Get the current list of quomponents.
     * @return current list of quomponents.
     */
    public synchronized ArrayList<Quomponent> getQuomponents(){
        return quomponents;
    }

    private long lastCheck;
    private int FPS, fpsCount;

    /**
     * Get the current FPS of the program.
     * @return current FPS.
     */
    public int getFPS(){
        return FPS;
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

    private Color debugColor = Color.black;

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

    private int resW, resH;
    private boolean setRes = false;

    /**
     * Set the resolution of the quicture
     * This will not change the actual size of it
     * but the size of which quomponents are loaded at.
     *
     * If set to 0 or below the system will default to the resolution of the window
     * @param width pixels wide
     * @param height pixels high
     */
    public void setResolution(int width, int height){
        if (width <= 0 && height <= 0) {
            setRes = false;
            return;
        }
        setRes = true;
        resW = width;
        resH = height;
    }

    /**
     * Get the width of pixels being used for the resolution
     * @return number of pixels wide of the resolution
     */
    public int getResWidth(){
        if (setRes)
            return (int)(resW*res3D+.5);
        else
            return (int)(getWidth()*res3D+.5);
    }

    /**
     * Get the height of pixels being used for the resolution
     * @return number of pixels high of the resolution
     */
    public int getResHeight(){
        if (setRes)
            return (int)(resH*res3D+.5);
        else
            return (int)(getHeight()*res3D+.5);
    }

    /**
     * Set the resolution of the screen based of a percent
     * of the size of the current monitor resolution being used.
     * @param quality percent of size as a number between 0 and 1
     */
    public void setMaxResolutionQuality(double quality){
        if (quality > 1 || quality <= 0 )
            return;
        setResolution((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*quality+.5), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*quality+.5));
    }

    private double res3D = 1;

    /**
     * Set the resolution of the quicture based off a percent
     * of the current size of the window.
     * @param quality percent of size as a number between 0 and 1.
     */
    public void set3DResolution(double quality){
        if (quality > 1 || quality <= 0 )
            return;
        res3D = quality;
    }

    /**
     * Get the current percent of resolution being used.
     * @return a percent as a number between 0 and 1.
     */
    public double get3DResolution(){
        return res3D;
    }

    private Quworld world = new Quworld();

    /**
     * Set the current quworld the quicture will draw
     * @param world the Quworld to be drawn.
     */
    public void setQuworld(Quworld world){
        this.world = world;
    }

    /**
     * Get the current quworld being used
     * @return current Quworld being used by the quicture
     */
    public Quworld getWorld(){
        return world;
    }

    /**
     * mmmmmmm... paint...
     * @param g graphics
     */
    @Override
    public void paintComponent(Graphics g){
        quindow.update();
        long startTime = System.nanoTime();
        refresh();

        //Set FPS currently at.
        if (lastCheck + 1000000000 < System.nanoTime()){
            FPS = fpsCount;
            lastCheck = System.nanoTime();
            fpsCount = 0;
        }

        Quamera tempCam = new Quamera(camera);

        //Paint
        world.paint(this, tempCam);
        for (int i = 0; i < quomponents.size(); i++)
            quomponents.get(i).paint(this, tempCam);

        //Draw image
        g.drawImage(picture,0,0,getWidth(),getHeight(),null);


        //Camera Debug Screen
        tempCam.paint(g);

        //Quicture Debug Screen
        Font font = g.getFont();
        g.setColor(debugColor);
        g.setFont(debugFont);
        g.drawString("FPS: " + FPS, quindow.getWidth() - 70, 15);
        g.setFont(font);

        //Increase FPS
        fpsCount++;

        if (quindow.getFps() <= 0)
            return;
        long wait = 1000/quindow.getFps() - (System.nanoTime() - startTime)/1000000;
        if (wait > 0)
            try {
                Thread.sleep(wait);
            }catch (Exception e){e.printStackTrace();}
    }
}
