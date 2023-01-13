package quinn.win;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * The window the is also a Quindow.
 * Quindow > JFrame
 * @author Quinn Graham
 */

public class Quindow extends JFrame {
    /**
     * Initialization of the Quindow will be the best part of your life.
     */
    public Quindow(){
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setTitle("Quindow");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(addComponentListener());
        setLayout(null);
        autoUpdate.start();

    }

    /**
     * If you want draw stuff, your are gonna need a Quicture.
     */
    private Quicture pic;
    public Quicture getQuicture(){
        return pic;
    }

    /**
     * The fps is quite important for every program.
     * Higher FPS can cause more CPU usage.
     */
    private int fps = 60;
    public void setFpsLimit(int fps){
        this.fps = fps;
    }
    public int getSetFps(){
        return fps;
    }

    /**
     * Quickly update the Quindow
     */
    public void update(){
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Automatically updates the Quindow according to the framerate.
     */
    public Thread autoUpdate = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                try{
                    Thread.sleep(2000/fps);
                    update();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    });


    /**
     * To draw stuff the Quindow needs to have a quicture.
     *
     * @param quicture the quicture being added to the quindow
     * @return the quicture used
     */
    public Quicture setQuicture(Quicture quicture){
        pic = quicture;
        pic.quindow = this;
        resizeQuicture();
        add(pic);
        return pic;
    }

    /**
     * Property resizes the quicture according to the width and height of the quindow.
     */
    protected void resizeQuicture(){
        if (pic != null) {
            if (pic.getUnitsWide() / pic.getUnitsTall() >= 1) {
                int height = (int) ((getWidth() / ((double) pic.getUnitsWide() / pic.getUnitsTall())) + .5);
                pic.setBounds(0, (getHeight() - height) / 2, getWidth(), height);
            } else {
                int width = (int) ((getHeight() * ((double) pic.getUnitsWide() / pic.getUnitsTall())) + .5);
                pic.setBounds((getWidth() - width) / 2, 0, width, getHeight());
            }
        }
    }

    /**
     * Listens for the quindow being resized.
     */
    private ComponentListener addComponentListener(){
        return new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeQuicture();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        };
    }

}
