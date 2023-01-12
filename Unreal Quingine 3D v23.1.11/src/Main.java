import quinn.win.Quicture;
import quinn.win.Quindow;
import quinn.world.Quworld;
import quinn.world.obj.Quectangal.Qube;
import quinn.world.obj.Quectangal.QuectangularQuism;
import quinn.world.obj.Quiangle.QuiagularQuism;
import quinn.world.obj.Quiangle.Quyramid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Quinn Graham
 * A program that simulates 3d objects.
 *
 * Distance formula; 2tan(percieve angle size)*distance = physical size.
 *
 * todo
 * Get rid of to degrees
 */
public class Main {
    public static void main(String[] args) {
        Quindow quindow = new Quindow();
        quindow.setTitle("3D Graphics Test");
        Quicture quicture = new Quicture(quindow);
        quicture.showFPS(true);
        Quworld quworld = new Quworld(quicture);
//        Quisition point = new Quisition(500,500,0);
//        quworld.add(point);
//        point.isVisible(true);
//        Qulane plane = new Qulane(200, 200, 500,500,0, Qulane.TRIANGLE);
//        quworld.add(plane);
//        plane.setFillColor(Color.blue);
//        Quyramid quism = new Quyramid(500,500,0,300,410,190);
        Qube quism = new Qube(5,5,0,5);
        quworld.add(quism);
        quism.fill(true);
        quism.setFaceColor(Color.blue, QuectangularQuism.BACK);
        quism.setFaceColor(Color.RED, QuectangularQuism.FRONT);
        quism.setFaceColor(Color.green, QuectangularQuism.RIGHT);
        quism.setFaceColor(Color.yellow, QuectangularQuism.LEFT);
        quism.setFaceColor(Color.CYAN, QuectangularQuism.TOP);
        quism.setFaceColor(Color.magenta, QuectangularQuism.BOTTOM);
//        quism.setFaceColor(Color.blue, QuiagularQuism.BACK);
//        quism.setFaceColor(Color.RED, QuiagularQuism.FRONT);
//        quism.setFaceColor(Color.green, QuiagularQuism.RIGHT);
//        quism.setFaceColor(Color.yellow, QuiagularQuism.LEFT);
//        quism.setFaceColor(Color.magenta, QuiagularQuism.BASE);
        quindow.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    quism.rotate(-1, 0,0, 1);;
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    quism.rotate(1, 0, 0, 1);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    quism.rotate(-1, 0,1, 0);;
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    quism.rotate(1, 0, 1, 0);
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    quism.rotate(1, 1,0, 0);;
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    quism.rotate(-1, 1, 0, 0);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    quworld.setCameraPos(0,0,.1);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    quworld.setCameraPos(0,0,-.1);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    quworld.setCameraPos(.1,0,0);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    quworld.setCameraPos(-.1,0,0);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        while (true) {
            SwingUtilities.updateComponentTreeUI(quindow);
        }
    }
}
