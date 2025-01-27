import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.prism.Qube;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;


public class Quingine {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture picture = new Quicture(window);
        Quworld world = new Quworld(picture);
        window.setSize(800,500);
        world.setMultiThread(4);

        picture.setDebugColor(Color.red);
        picture.getQuamera().setDebugColor(Color.red);
        picture.setBackgroundColor(Color.BLACK);
        window.setFps(0);
        picture.setPercentResolution(.4);
        window.setTitle("Quingine 25.1.27");
        world.getPlayer().setGravity(new Quisition());

        world.add(new Qube(5,0,0,10));

        world.enableDevMode();

        //Camera
        Quamera boardCam = new Quamera();
        AtomicBoolean down = new AtomicBoolean(false);
        double speed = .25;
        double rotSpeed = .02;
        world.startWorldThread();


        world.addQuworldTickListener((tickSpeed, currentTick) -> {
            if (!down.get() && window.isKeyDown(KeyEvent.VK_SPACE)) {
                world.getPlayer().hit(10);
                down.set(true);
            }
            if (!down.get() && window.isKeyDown(KeyEvent.VK_B)) {
                world.getPlayer().hit(250);
                down.set(true);
            }
            if (window.isKeyDown(KeyEvent.VK_C)) {
                boardCam.setPos(picture.getQuamera().getPos());
                boardCam.setRotation(picture.getQuamera().getYaw(), picture.getQuamera().getPitch(), picture.getQuamera().getRoll());
            }

            if (!window.isKeyDown(KeyEvent.VK_SPACE))
                down.set(false);
            if (!window.isKeyDown(KeyEvent.VK_B))
                down.set(false);
            if (window.isKeyDown(KeyEvent.VK_ALT))
                world.getPlayer().hit(7);
        });

        world.getPlayer().setMass(1000);

        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e4) {
            }
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
            world.getPlayer().setPos(picture.getQuamera().getPos());
        }
    }
}
