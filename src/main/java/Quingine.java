import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quindow;

import java.awt.*;


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
        window.setTitle("Quingine 25.2.17");

//        world.load("test.quworld");

        int size = 20;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++){
                Quobject obj = new Quobject("RubixCube", i*6-10, -15,j*6+10, 100);
                world.add(obj);
            }

//        world.enableDevMode();

        //Camera
        double speed = .25;
        double rotSpeed = .02;

        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {}
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
            world.getPlayer().setPos(picture.getQuamera().getPos());
        }
    }
}
