package quingine.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * A quindow to another world!
 * An easier way to set up a window
 * Utilizing JFrame.
 * @author Quinn Graham
 */

public class Quindow extends JFrame {
    /**
     * Initializing the window has never
     * been easier.
     */
    public Quindow(){
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
        setTitle("Quindow");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        update.start();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                for (int i = 0; i < keysDown.size(); i++)
                    if (keysDown.get(i).getKeyCode() == e.getKeyCode())
                        return;
                keysDown.add(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                for (int i = 0; i < keysDown.size(); i++)
                    if (keysDown.get(i).getKeyCode() == e.getKeyCode())
                        keysDown.remove(i);
            }
        });
    }

    private volatile ArrayList<KeyEvent> keysDown = new ArrayList<>();

    public ArrayList<KeyEvent> getKeysDown(){
        return keysDown;
    }

    public boolean isKeyDown(int keyCode){
        for (int i = 0; i < keysDown.size(); i++)
            if (keysDown.get(i).getKeyCode() == keyCode)
                return true;
        return false;
    }

    private int fps = 30;

    /**
     * Set what fps you want the window to run at.
     * @param fps frames per second
     */
    public void setFps(int fps){
        this.fps = fps;
        if (fps <= 0)
            wait = 0;
        else
            wait = 1000/fps;
    }

    /**
     * What is the fps set to?
     * @return the number the fps was set to
     */
    public int getFps(){
        return fps;
    }

    /**
     * Updates the screen constantly according to fps.
     */
    private int wait = 1000/fps;
    private Thread update = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(wait);
                getComponents()[0].repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

}
