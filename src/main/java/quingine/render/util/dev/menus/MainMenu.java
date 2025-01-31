package quingine.render.util.dev.menus;

import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.env.obj.prism.Qube;
import quingine.render.util.dev.DevWindow;

import javax.swing.*;
import java.awt.*;

/**
 * The Main menu for the developer tools windows
 */

public class MainMenu {

    public static JPanel menu;

    private static DevWindow devWin;
    private static Quworld world;

    private static JScrollPane objScroll;
    private static JList<String> objList;

    /**
     * Initialize the Main Menu
     * @param devWin the developer window being used
     */
    public static void init(DevWindow devWin){
        menu = new JPanel();
        MainMenu.devWin = devWin;
        MainMenu.world = devWin.getWorld();
        menu.setLayout(null);

        //Add
        JButton aQ = new JButton("Add");
        aQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(aQ);
        aQ.addActionListener(e -> {
            Quobject quobject = new Qube(5,0,0,0);
            world.add(quobject);
            refreshObjectList();
            devWin.update();
        });
        devWin.setPosOf(aQ, 1.2,.2);
        devWin.setSizeOf(aQ, .5,.1);

        //Remove
        JButton rQ = new JButton("Remove");
        rQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(rQ);
        rQ.addActionListener(e -> {
            int index = objList.getSelectedIndex();
            if (world.getQuobjects().isEmpty() || index <= -1)
                return;
            world.remove(world.getQuobjects().get(index));
            refreshObjectList();
            devWin.update();
        });
        devWin.setPosOf(rQ, 1.2,.4);
        devWin.setSizeOf(rQ, .5,.1);

        //Modify
        JButton mQ = new JButton("Modify");
        mQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(mQ);
        mQ.addActionListener(e -> {
            devWin.modifyMenu();
            devWin.update();
            ModifyMenu.refreshObjectList();
        });
        devWin.setPosOf(mQ, 1.2,.6);
        devWin.setSizeOf(mQ, .5,.1);

        //Copy
        JButton cQ = new JButton("Copy");
        cQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(cQ);
        cQ.addActionListener(e -> {
            devWin.update();
        });
        devWin.setPosOf(cQ, 1.2,.8);
        devWin.setSizeOf(cQ, .5,.1);

        //Paste
        JButton pQ = new JButton("Paste");
        pQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(pQ);
        pQ.addActionListener(e -> {
            devWin.update();
        });
        devWin.setPosOf(pQ, 1.2,1);
        devWin.setSizeOf(pQ, .5,.1);

        //Load
        JButton lQ = new JButton("Load");
        lQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(lQ);
        lQ.addActionListener(e -> {
            String load = JOptionPane.showInputDialog(menu,
                    "Name of Quworld to load (Must be in quworlds folder)", null);
            load = load.concat(".quworld");
            world.load(load);
            refreshObjectList();
            devWin.update();
        });
        devWin.setPosOf(lQ, 1.2,1.2);
        devWin.setSizeOf(lQ, .5,.1);

        //Save
        JButton sQ = new JButton("Save");
        sQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(sQ);
        sQ.addActionListener(e -> {
            world.save();
            devWin.update();
        });
        devWin.setPosOf(sQ, 1.2,1.4);
        devWin.setSizeOf(sQ, .5,.1);

        //Object List
        objList = new JList<>();
        objScroll = new JScrollPane(objList);
        devWin.setSizeOf(objScroll, 1,1.7);
        devWin.setPosOf(objScroll, 0,.1);
        refreshObjectList();
        menu.add(objScroll);
        devWin.update();
    }

    /**
     * Refresh the list of quobjects in the world.
     */
    public static void refreshObjectList(){
        String[] data = new String[world.getQuobjects().size()];
        for (int i = 0; i < world.getQuobjects().size(); i++) {
            String name = world.getQuobjects().get(i).getName();
            if (name == null)
                name = "Quobject: " + i;
            data[i] = name;
        }
        objList.setListData(data);
    }
}
