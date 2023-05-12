package quingine.util;

import quingine.sim.Quisition;
import quingine.sim.cam.Quamera;
import quingine.sim.cam.QuameraListener;
import quingine.sim.obj.Quobject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
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
    private Quamera cameraOld;
    /**
     * Set what camera the quicture is using
     * @param quamera a camera
     */
    public void setCamera(Quamera quamera){
        camera = quamera;
        cameraOld = new Quamera(camera);
    }

    /**
     * Get what camera the quicture is using.
     * @return the current camera being used.
     */
    public Quamera getQuamera(){
        return camera;
    }

    private Color background = Color.black;

    /**
     * Set the color of the background
     * @param color the desired background color
     */
    public void setBackground(Color color){
        background = color;
    }

    /**
     * Get the current background color
     * @return color of background
     */
    public Color getBackground(){
        return background;
    }
    private Quixel[][] pixels;
    private BufferedImage picture;
    /**
     * Refreshing the screen is so refreshing!
     * If called, this method deletes the old list
     * of pixels and creates a new one and resets
     * the picture.
     */
    private void refresh(){
        pixels = new Quixel[getWidth()][getHeight()];
        picture = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int w = 0; w < getWidth(); w++)
            for (int h = 0; h < getHeight(); h++) {
                pixels[w][h] = new Quixel(w, h, Integer.MAX_VALUE);
                picture.setRGB(w, h, background.getRGB());
            }
    }

    /**
     * Setting the specific pixel on the screen.
     * @param x screen position
     * @param y screen position
     * @param z value between 1 & -1
     * @param color color you want the pixel to be.
     */
    public void setPixel(int x, int y, double z, Color color){
        setPixel(new Quixel(x,y,z,color));
    }

    /**
     * Easier way of setting the pixel
     * @param quixel which is a pixel
     */
    public void setPixel(Quixel quixel){
        try {
            if (pixels[quixel.x][quixel.y].z >= quixel.z && quixel.z > 0 && quixel.z < 1) {
                pixels[quixel.x][quixel.y] = quixel;
                picture.setRGB(quixel.x, quixel.y, quixel.getColor().getRGB());
            }
        }catch (Exception e){}
    }

    /**
     * Takes what ever quisition is put through and translates it through the matrix.
     * @param quisition current place in the matrix
     * @param color Roy G. Biv
     */
    public void setPixel(Quisition quisition, Color color){
        setPixel(new Quixel(getScreenPosX(quisition.x), getScreenPosY(quisition.y), quisition.z, color));
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
        return (int)Math.round((getWidth()*(unitsWide/2.0 + x))/unitsWide);
    }
    /**
     * Getting what position something is on the matrix to the screen.
     * @param y matrix position
     * @return y screen position
     */
    public int getScreenPosY(double y){
        return (int)Math.round((getHeight()*(unitsTall/2.0 - y))/unitsTall);
    }

    /**
     * Getting what position something is on the screen to the matrix.
     * @param x screen position
     * @return x matrix position
     */
    public double getMatrixPosX(double x){
        return unitsWide * x/getWidth() - unitsWide/2.0;
    }
    /**
     * Getting what position something is on the screen to the matrix.
     * @param y screen position
     * @return y matrix position
     */
    public double getMatrixPosY(double y){
        return -(unitsTall * y / getHeight() - unitsTall/2.0);
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
        if (quomponents.contains(quomponent))
            return quomponents.remove(quomponent);
        else
            return false;
    }

    /**
     * Get the current list of quomponents.
     * @return current list of quomponents.
     */
    public ArrayList<Quomponent> getQuomponents(){
        return quomponents;
    }

    private ArrayList<QuameraListener> quameraListeners = new ArrayList<>();

    /**
     * Add a quameralistener to your quicture.
     * @param listener quameralistner
     * @return true if added, false it already there.
     */
    public boolean addQuameraListener(QuameraListener listener){
        if (quameraListeners.contains(listener))
            return false;
        else
            return quameraListeners.add(listener);
    }

    /**
     * Remove a quameralistener from your quicture
     * @param listener quameralistener
     * @return true if removed, false not on list before.
     */
    public boolean removeQuameraListener(QuameraListener listener){
        if (quameraListeners.contains(listener))
            return quameraListeners.remove(listener);
        else
            return false;
    }

    /**
     * Get current list of quameralisteners being used.
     * @return current list of quameralisteners.
     */
    public ArrayList<QuameraListener> getQuameraListeners(){
        return quameraListeners;
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
     * mmmmmmm... paint...
     * @param g graphics
     */
    @Override
    public void paintComponent(Graphics g){
        refresh();
        for (QuameraListener listener : quameraListeners){
            if (!camera.rotationEquals(cameraOld) || (!camera.getPos().equals(cameraOld.getPos())))
                listener.updated(camera.getPos().x - cameraOld.getPos().x, camera.getPos().y - cameraOld.getPos().y, camera.getPos().z - cameraOld.getPos().z, camera.getYaw() - cameraOld.getYaw(), camera.getPitch() - cameraOld.getPitch(), camera.getRoll());
            if (!camera.rotationEquals(cameraOld))
                listener.rotated(camera.getYaw() - cameraOld.getYaw(), camera.getPitch() - cameraOld.getPitch(), camera.getRoll() - cameraOld.getRoll());
            if (!camera.getPos().equals(cameraOld.getPos()))
                listener.moved(camera.getPos().x - cameraOld.getPos().x, camera.getPos().y - cameraOld.getPos().y, camera.getPos().z - cameraOld.getPos().z);
        }
        cameraOld = new Quamera(camera);
        if (lastCheck + 1000000000 < System.nanoTime()){
            FPS = fpsCount;
            lastCheck = System.nanoTime();
            fpsCount = 0;
        }
        for (int i = 0; i < quomponents.size(); i++)
            quomponents.get(i).paint(this);
        g.drawImage(picture,0,0,this);
        camera.paint(g);
        Font font = g.getFont();
        g.setColor(debugColor);
        g.setFont(debugFont);
        g.drawString("FPS: " + FPS, quindow.getWidth() - 70, 15);
        g.setFont(font);
        fpsCount++;
    }
}
