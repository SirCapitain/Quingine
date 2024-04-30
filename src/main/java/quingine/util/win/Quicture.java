package quingine.util.win;

import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.pos.Quisition;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

/**
 * Every Quindow needs a Quicture to produce an image.
 * This class also sets the picture matrix for positioning.
 * @author Quinn Graham
 */

public class Quicture extends JPanel {

    private Quindow quindow;
    private Quamera camera = new Quamera();

    private int resW, resH;
    private boolean setRes = false;
    private double resP = 1;

    private volatile BufferedImage picture;
    private ArrayList<Quomponent> quomponents = new ArrayList<>();

    private int backgroundColor = Color.white.getRGB();

    private Quworld world = new Quworld();

    private long lastCheck;
    private int FPS, fpsCount;

    private Color debugColor = Color.black;
    private Font debugFont = new Font(Font.DIALOG, Font.PLAIN, 10);


    private JToolBar toolBar;
    private boolean showCrosshair = true;

    /**
     * Initializing the quicture
     * @param quindow the window you want to hold the quicture
     */
    public Quicture(Quindow quindow){
        quindow.add(this);
        this.quindow = quindow;
        setSize(quindow.getSize());
        setVisible(true);
        initializeToolBar();
        refresh();
    }

    /**
     * Retrieve what quindow a quicture is using.
     * @return the current quindow being used.
     */
    public Quindow getQuindow(){
        return quindow;
    }

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

    /**
     * Refreshing the screen is so refreshing!
     * If called, this method deletes the old list
     * of pixels and creates a new one and resets
     * the picture.
     */
    private void refresh(){
        int width = getResWidth();
        int height = getResHeight();
        picture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int w = 0; w < width; w++)
            for (int h = 0; h < height; h++)
                ((DataBufferInt) picture.getRaster().getDataBuffer()).getData()[h*getResWidth() + w] = backgroundColor;
    }

    /**
     * Setting the specific pixel on the screen.
     * @param x screen position
     * @param y screen position
     * @param z value between 1 & -1
     * @param color color you want the pixel to be.
     */
    public synchronized void setPixel(int x, int y, double z, int color){
        ((DataBufferInt) picture.getRaster().getDataBuffer()).getData()[y*getResWidth() + x] = color;
    }

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

    /**
     * Get the current image of the quicture
     * @return BufferedImage of current picture
     */
    public BufferedImage getPicture(){
        return picture;
    }

    /**
     * Get the current FPS of the program.
     * @return current FPS.
     */
    public int getFPS(){
        return FPS;
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
     * Set the background color of the picture
     * @param color any color for the background to be
     */
    public void setBackgroundColor(Color color){
        backgroundColor = color.getRGB();
    }

    /**
     * Get the current background color
     * @return background color
     */
    public int getBackgroundColor(){
        return backgroundColor;
    }

    /**
     * Set the resolution of the quicture
     * This will not change the actual size of it
     * but the size of which quomponents are loaded at.
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
            return (int)(resW*resP+.5);
        else
            return (int)(getWidth()*resP+.5);
    }

    /**
     * Get the height of pixels being used for the resolution
     * @return number of pixels high of the resolution
     */
    public int getResHeight(){
        if (setRes)
            return (int)(resH*resP+.5);
        else
            return (int)(getHeight()*resP+.5);
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

    /**
     * Set the resolution of the quicture based off a percent
     * of the current size of the window.
     * @param quality percent of size as a number between 0 and 1.
     */
    public void setPercentResolution(double quality){
        if (quality > 1 || quality <= 0 )
            return;
        resP = quality;
    }

    /**
     * Get the current percent of resolution being used.
     * @return a percent as a number between 0 and 1.
     */
    public double getPercentResolution(){
        return resP;
    }

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

    private void initializeToolBar(){
        toolBar = new JToolBar();
        JPanel panel = new JPanel();
        toolBarVisible(true);
        toolBar.setLayout(new BorderLayout());
        JButton quit = new JButton("quit");
        quit.addActionListener(e -> System.exit(0));
        JButton save = new JButton("save");
        save.addActionListener(e -> {
            if (world != null)
                world.save();
        });
        JSpinner res = new JSpinner(new SpinnerNumberModel(resP,0.05,1,.05));
        res.addChangeListener(e -> setPercentResolution((double)res.getValue()));

        JSpinner tS = new JSpinner(new SpinnerNumberModel(world.getTickSpeed(),1,Integer.MAX_VALUE,10));
        tS.addChangeListener(e -> world.setTickSpeed((int)tS.getValue()));
        tS.setPreferredSize(new Dimension(50, 20));

        JSpinner gS = new JSpinner(new SpinnerNumberModel(world.getQysicSpeed(),1,Integer.MAX_VALUE,10));
        gS.addChangeListener(e -> world.setQysicSpeed((int)gS.getValue()));
        gS.setPreferredSize(new Dimension(50, 20));

        panel.add(quit);
        panel.add(save);
        panel.add(res);
        panel.add(tS);
        panel.add(gS);
        toolBar.add(panel);
        toolBar.setFocusable(false);
        quit.setFocusable(false);
        save.setFocusable(false);
        res.setFocusable(false);
        panel.setFocusable(false);
        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Set the visibility of the toolbar.
     * @param visible true to show, false to hide.
     */
    public void toolBarVisible(boolean visible){
        toolBar.setVisible(visible);
    }

    /**
     * Get the toolbar being used.
     * @return jtoolbar.
     */
    public JToolBar getToolBar(){
        return toolBar;
    }

    /**
     * Show a box in the middle of the screen.
     * @param showCrossahair true to show, false to hide
     */
    public void showCrosshair(boolean showCrossahair){
        this.showCrosshair = showCrossahair;
    }

    /**
     * Check if the crosshair is showing or not.
     * @return true if showing, false is not.
     */
    public boolean showCrosshair(){
        return showCrosshair;
    }

    /**
     * mmmmmmm... paint...
     * @param g graphics
     */
    @Override
    public void paintComponent(Graphics g){
        quindow.update();
        long startTime = System.nanoTime();
        camera.setResolution(getResWidth(), getResHeight());

        //Set FPS currently at.
        if (lastCheck + 1000000000 < System.nanoTime()){
            FPS = fpsCount;
            lastCheck = System.nanoTime();
            fpsCount = 0;
        }

        camera.updateComponents(false);

        picture = camera.getNewPicture(this);

        //Draw image
        g.drawImage(picture,0,0,getWidth(),getHeight(),null);
        camera.updateComponents(true);


        //Camera Debug Screen
        camera.paint(g);

        //Quicture Debug Screen
        Font font = g.getFont();
        g.setColor(debugColor);
        g.setFont(debugFont);
        g.drawString("FPS: " + FPS, quindow.getWidth() - 70, 15);
        g.setFont(font);

        if (showCrosshair)
            g.fillRect(getWidth()/2 - 3, getHeight()/2 - 3, 6, 6);

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
