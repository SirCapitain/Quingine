package quingine.render.util.dev.menus;

import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.light.LightSource;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.util.dev.DevWindow;
import quingine.render.util.win.Quomponent;

import javax.swing.*;
import java.awt.*;

/**
 * The Main menu for the developer tools windows
 * At least there is a menu that works.
 * Be grateful for what you get.
 */

public class MainMenu {

    public static JPanel menu;

    private static DevWindow devWin;
    private static Quworld world;

    private static JScrollPane objScroll;
    private static JList<String> objList;

    private static Quobject copy;

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
            int input = JOptionPane.showOptionDialog(menu, "Which type would you like to add?", "Add", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Quobject", "Light Source"}, null);
            if (input == 0) {
                String obj = JOptionPane.showInputDialog(menu,
                        "Name of quobject folder. (Must be in objects folder)", null);
                if (obj == null)
                    return;
                world.add(new Quobject(obj, world.getPlayer().getPos(), 1));
            }
            if (input == 1) {
                world.add(new LightSource(world.getPlayer().getPos()));
            }
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
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            world.remove(quom);
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
            copy = getSelectedQuobject();
            devWin.update();
        });
        devWin.setPosOf(cQ, 1.2,.8);
        devWin.setSizeOf(cQ, .5,.1);

        //Paste
        JButton pQ = new JButton("Paste");
        pQ.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(pQ);
        pQ.addActionListener(e -> {
            if (copy == null)
                return;
            world.add(new Quobject(copy));
            refreshObjectList();
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
            if (load == null)
                return;
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
            String save = JOptionPane.showInputDialog(menu,
                    "Name of Quworld to save as.", "Untitled");
            if (save == null)
                return;
            world.save(save);
            devWin.update();
        });
        devWin.setPosOf(sQ, 1.2,1.4);
        devWin.setSizeOf(sQ, .5,.1);

        //Object List
        objList = new JList<>();
        objScroll = new JScrollPane(objList);
        devWin.setSizeOf(objScroll, 1,1.7);
        devWin.setPosOf(objScroll, 0,.1);
        menu.add(objScroll);
        devWin.update();
        refreshObjectList();
    }

    /**
     * Refresh the list of quobjects in the world.
     */
    public static void refreshObjectList(){
        Quworld world = devWin.getWorld();
        String[] data = new String[world.getQuobjects().size() + world.getLightSources().size()];
        for (int i = 0; i < world.getQuobjects().size(); i++) {
            String name = world.getQuobjects().get(i).getName();
            if (name == null)
                name = "Quobject: " + i;
            data[i] = name;
        }
        for (int i = 0; i < world.getLightSources().size(); i++) {
            String name = world.getLightSources().get(i).getName();
            if (name == null)
                name = "Light: " + i;
            data[i + world.getQuobjects().size()] = name;
        }
        objList.setListData(data);
    }

    /**
     * Get the currently selected quobject in the list.
     * @return currently selected quobject, null if none
     */
    public static Quobject getSelectedQuobject(){
        int index = objList.getSelectedIndex();
        if (index <= -1 || index >= devWin.getWorld().getQuobjects().size())
            return null;
        return devWin.getWorld().getQuobjects().get(objList.getSelectedIndex());
    }

    /**
     * Get the currently selected quomponent on the list.
     * @return currently selected quomponent, null if none
     */
    public static Quomponent getSelectedQuomponent(){
        int index = objList.getSelectedIndex();
        if (index <= -1)
            return null;
        int size = devWin.getWorld().getQuobjects().size();
        if (index < size)
            return devWin.getWorld().getQuobject(objList.getSelectedIndex());
        else
            return devWin.getWorld().getLightSources().get(objList.getSelectedIndex() - size);
    }
}