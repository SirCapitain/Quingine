import quingine.sim.light.LightSource;
import quingine.sim.obj.Quobject;
import quingine.sim.obj.prism.QuectangularQuism;
import quingine.util.Quicture;
import quingine.util.Quindow;

import java.awt.event.KeyEvent;


public class main {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture quicture = new Quicture(window);
        window.setSize(600,400);
        quicture.getQuamera().setzNear(.001);
        quicture.getQuamera().setzFar(400);
        window.setFps(30);
        quicture.getQuamera().setFov(60);
        quicture.getQuamera().flashlightOn(true);


        Quobject man = new Quobject("man.obj",10,-5,15,1);
        quicture.add(man);
//        man.outline(true);

        Quobject gem = new Quobject("gem.obj",10,-5,10,.25);
        quicture.add(gem);

        QuectangularQuism big = new QuectangularQuism(1,1,10, 0,-1,20);
        quicture.add(big);
//        big.outline(true);

        Quobject staff = new Quobject("staff.obj", -10,-10,7,.1);
        quicture.add(staff);

        Quobject coolCube = new Quobject("dogguinblock.obj", 0, 0 ,5,.5);
        coolCube.setTexture("Dogguin.png");
        quicture.add(coolCube);
//        coolCube.alwaysLit(true);

        Quobject cage = new Quobject("Nicolas Cage cage.obj", 0, 10, 10,1);
        cage.setTexture("cgae.jpg");
        quicture.add(cage);
//        cage.alwaysLit(true);

        LightSource light = new LightSource(-10,11,7);
        quicture.add(light);

        double speed = .01;
        double rotSpeed = .1;

        while(true) {
            coolCube.setPos(light.getPos());
            try{
                Thread.sleep(1);
            }
            catch (Exception e){}
            if (window.isKeyDown(KeyEvent.VK_D))
                quicture.getQuamera().changePosBy(speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())), 0, speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_W))
                quicture.getQuamera().changePosBy(-speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())), 0, speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_A))
                quicture.getQuamera().changePosBy(-speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())), 0, -speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_S))
                quicture.getQuamera().changePosBy(speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())), 0, -speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_Q))
                quicture.getQuamera().changePosBy(0, -speed, 0);
            if (window.isKeyDown(KeyEvent.VK_E))
                quicture.getQuamera().changePosBy(0, speed, 0);
            if (window.isKeyDown(KeyEvent.VK_UP))
                quicture.getQuamera().changeRotationBy(0, rotSpeed, 0);
            if (window.isKeyDown(KeyEvent.VK_DOWN))
                quicture.getQuamera().changeRotationBy(0, -rotSpeed, 0);
            if (window.isKeyDown(KeyEvent.VK_RIGHT))
                quicture.getQuamera().changeRotationBy(-rotSpeed, 0, 0);
            if (window.isKeyDown(KeyEvent.VK_LEFT))
                quicture.getQuamera().changeRotationBy(rotSpeed, 0, 0);
            if (window.isKeyDown(KeyEvent.VK_PAGE_UP))
                quicture.getQuamera().changeRotationBy(0, 0, rotSpeed);
            if (window.isKeyDown(KeyEvent.VK_PAGE_DOWN))
                quicture.getQuamera().changeRotationBy(0, 0, -rotSpeed);
            if (window.isKeyDown(KeyEvent.VK_L))
                light.getPos().changeZBy(-.01);
            if (window.isKeyDown(KeyEvent.VK_O))
                light.getPos().changeZBy(.01);
            if (window.isKeyDown(KeyEvent.VK_K))
                light.getPos().changeXBy(-.01);
            if (window.isKeyDown(KeyEvent.VK_SEMICOLON))
                light.getPos().changeXBy(.01);
            if (window.isKeyDown(KeyEvent.VK_I))
                light.getPos().changeYBy(.01);
            if (window.isKeyDown(KeyEvent.VK_P))
                light.getPos().changeYBy(-.01);
        }
    }
}
