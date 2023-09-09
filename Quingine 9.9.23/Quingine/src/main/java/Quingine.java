import quingine.sim.Texquisition;
import quingine.sim.env.Quworld;
import quingine.sim.env.light.LightSource;
import quingine.sim.env.obj.Quobject;
import quingine.sim.env.obj.prism.Qube;
import quingine.util.win.Quicture;
import quingine.util.win.Quindow;

import java.awt.event.KeyEvent;


public class Quingine {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture picture = new Quicture(window);
        Quworld world = new Quworld(picture);
        window.setSize(500,300);
        picture.getQuamera().setzNear(.001);
        picture.getQuamera().setzFar(800);
        window.setFps(0);
        picture.getQuamera().setFov(60);
        picture.getQuamera().flashlightOn(true);
        picture.getQuamera().setLightPower(1.2);
        world.setMultiThread(10);


//        picture.setResolution(16,9);
//        picture.setMaxResolutionQuality(.45);
        picture.set3DResolution(.5);

        window.setTitle("Quingine 23.9.9");

        int size = 100;

        for (int w = -size/2; w < size/2; w++)
            for (int d = -size/2; d < size/2; d++){
                Qube cube = new Qube(1,w,-1,d);
                for (int i = 0; i < cube.getPlanes().size(); i++) {
                    cube.getPlanes().get(i).setTexPoints(new Texquisition[]{new Texquisition(0, 0), new Texquisition(0, 1), new Texquisition(1, 1)});
                    i++;
                    cube.getPlanes().get(i).setTexPoints(new Texquisition[]{new Texquisition(0, 0), new Texquisition(1, 1), new Texquisition(1, 0)});
                }
                cube.setTexture("cobble.png");
                world.add(cube);
            }


        Qube cube = new Qube(1,0,1,0);
        for (int i = 0; i < cube.getPlanes().size(); i++) {
            cube.getPlanes().get(i).setTexPoints(new Texquisition[]{new Texquisition(0, 0), new Texquisition(0, 1), new Texquisition(1, 1)});
            i++;
            cube.getPlanes().get(i).setTexPoints(new Texquisition[]{new Texquisition(0, 0), new Texquisition(1, 1), new Texquisition(1, 0)});
        }
        cube.setTexture("Dogguin.png");
        world.add(cube);

        LightSource light = new LightSource();
        world.add(light);
        light.setPower(5);
        cube.alwaysLit(true);

        double speed = .01;
        double rotSpeed = .1;


        while(true) {
            light.setPos(cube.getPos());
            try{
                Thread.sleep(1);
            }
            catch (Exception e){e.printStackTrace();}
            if (window.isKeyDown(KeyEvent.VK_D))
                picture.getQuamera().changePosBy(speed * Math.cos(Math.toRadians(picture.getQuamera().getYaw())), 0, speed * Math.sin(Math.toRadians(picture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_W))
                picture.getQuamera().changePosBy(-speed * Math.sin(Math.toRadians(picture.getQuamera().getYaw())), 0, speed * Math.cos(Math.toRadians(picture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_A))
                picture.getQuamera().changePosBy(-speed * Math.cos(Math.toRadians(picture.getQuamera().getYaw())), 0, -speed * Math.sin(Math.toRadians(picture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_S))
                picture.getQuamera().changePosBy(speed * Math.sin(Math.toRadians(picture.getQuamera().getYaw())), 0, -speed * Math.cos(Math.toRadians(picture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_Q))
                picture.getQuamera().changePosBy(0, -speed, 0);
            if (window.isKeyDown(KeyEvent.VK_E))
                picture.getQuamera().changePosBy(0, speed, 0);
            if (window.isKeyDown(KeyEvent.VK_UP))
                picture.getQuamera().changeRotationBy(0, rotSpeed, 0);
            if (window.isKeyDown(KeyEvent.VK_DOWN))
                picture.getQuamera().changeRotationBy(0, -rotSpeed, 0);
            if (window.isKeyDown(KeyEvent.VK_RIGHT))
                picture.getQuamera().changeRotationBy(-rotSpeed, 0, 0);
            if (window.isKeyDown(KeyEvent.VK_LEFT))
                picture.getQuamera().changeRotationBy(rotSpeed, 0, 0);
            if (window.isKeyDown(KeyEvent.VK_L))
                cube.setPos(cube.getPos().x, cube.getPos().y, cube.getPos().z-speed);
            if (window.isKeyDown(KeyEvent.VK_O))
                cube.setPos(cube.getPos().x, cube.getPos().y, cube.getPos().z+speed);
            if (window.isKeyDown(KeyEvent.VK_K))
                cube.setPos(cube.getPos().x-speed, cube.getPos().y, cube.getPos().z);
            if (window.isKeyDown(KeyEvent.VK_SEMICOLON))
                cube.setPos(cube.getPos().x+speed, cube.getPos().y, cube.getPos().z);
            if (window.isKeyDown(KeyEvent.VK_I))
                cube.setPos(cube.getPos().x, cube.getPos().y-speed, cube.getPos().z);
            if (window.isKeyDown(KeyEvent.VK_P))
                cube.changePosBy(0,speed,0);
        }
    }
}
