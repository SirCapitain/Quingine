package quingine.util.win;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * A quindow to another world!
 * An easier way to set up a window
 * Utilizing JFrame.
 * @author Quinn Graham
 */

public class Quindow extends JFrame {

    private int fps = 30;

    private volatile ArrayList<KeyEvent> keysDown = new ArrayList<>();

    /**
     * Initializing the window has never
     * been easier.
     */
    public Quindow(){
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
        setTitle("Quindow");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public ArrayList<KeyEvent> getKeysDown(){
        return keysDown;
    }

    public boolean isKeyDown(int keyCode){
        for (int i = 0; i < keysDown.size(); i++)
            if (keysDown.get(i).getKeyCode() == keyCode)
                return true;
        return false;
    }

    /**
     * Set what fps you want the window to run at.
     * @param fps frames per second
     */
    public void setFps(int fps){
        this.fps = fps;
    }

    /**
     * What is the fps set to?
     * @return the number the fps was set to
     */
    public int getFps(){
        return fps;
    }

    /**
     * update the quindow.
     */
    public void update(){
        getComponents()[0].repaint();
    }

}
