import quinn.win.Quicture;
import quinn.win.Quindow;
import quinn.world.Quomponent;
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
 * A program that simulates 3D objects.
 *
 */
public class Main {
    public static void main(String[] args) {
        Quindow quindow = new Quindow();
        quindow.setTitle("3D Graphics Test");
        Quicture quicture = new Quicture(quindow);
        quicture.showFPS(true);
        Quworld quworld = new Quworld(quicture);
        quicture.setQuictureMartix(70,35);
//        Quyramid quism = new Quyramid(30,20,-5,5,5,5);
        QuectangularQuism quism = new QuectangularQuism(35,20,-35,5,5,50);
        quworld.add(quism);
        quworld.setFov(30);
        quism.fill(true);
        quism.outlined(true);
        quism.isPerspective(true);
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
                    quism.setPos(quism.getPos().x, quism.getPos().y, quism.getPos().z+1);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    quism.setPos(quism.getPos().x, quism.getPos().y,quism.getPos().z-1);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    quworld.changeCameraPos(.1,0,0);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    quworld.changeCameraPos(-.1,0,0);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
}
