package quingine.render.util.dev.menus;

import quingine.render.sim.env.Quworld;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.dev.DevWindow;

import javax.swing.*;
import java.awt.*;

/**
 * The Modify menu in the Developer tools window
 */

public class ModifyMenu {

    public static JPanel menu;

    private static DevWindow devWin;

    private static JSpinner x, y, z;
    private static JSpinner stepSize;

    private static JScrollPane objScroll;
    private static JList<String> objList;

    /**
     * Initialize the Modify Menu
     * @param devWin the developer window being used
     */
    public static void init(DevWindow devWin) {
        ModifyMenu.devWin = devWin;
        menu = new JPanel();
        menu.setLayout(null);
        menu.setVisible(false);

        //StepSize
        stepSize = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE,Double.MAX_VALUE,1));
        menu.add(stepSize);
        devWin.setSizeOf(stepSize, .5,.1);
        devWin.setPosOf(stepSize, 1.36, .65);
        stepSize.addChangeListener(e -> {
            x.setModel(new SpinnerNumberModel((double)x.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
            y.setModel(new SpinnerNumberModel((double)y.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
            z.setModel(new SpinnerNumberModel((double)z.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
        });
        JLabel sl = new JLabel("Step Size - ");
        sl.setFont(new Font("Serif", Font.BOLD,11));
        menu.add(sl);
        devWin.setSizeOf(sl, .43,.11);
        devWin.setPosOf(sl, 1.01, .635);



        //X
        x = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
        menu.add(x);
        devWin.setSizeOf(x, .5,.1);
        devWin.setPosOf(x, 1.36, .2);
        x.addChangeListener(e -> {
            int index = objList.getSelectedIndex();
            if (index <= -1)
                return;
            devWin.getWorld().getQuobjects().get(objList.getSelectedIndex()).setX((double)x.getValue());
        });

        JLabel xl = new JLabel("X - ");
        menu.add(xl);
        devWin.setSizeOf(xl, .12,.11);
        devWin.setPosOf(xl, 1.2, .175);

        //Y
        y = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE,(double)stepSize.getValue()));
        menu.add(y);
        devWin.setSizeOf(y, .5,.1);
        devWin.setPosOf(y, 1.36, .35);
        y.addChangeListener(e ->{
            int index = objList.getSelectedIndex();
            if (index <= -1)
                return;
            devWin.getWorld().getQuobjects().get(objList.getSelectedIndex()).setY((double)y.getValue());
        });

        JLabel yl = new JLabel("Y - ");
        menu.add(yl);
        devWin.setSizeOf(yl, .12,.11);
        devWin.setPosOf(yl, 1.2, .325);

        //Z
        z = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE,(double)stepSize.getValue()));
        menu.add(z);
        devWin.setSizeOf(z, .5,.1);
        devWin.setPosOf(z, 1.36, .5);
        z.addChangeListener(e -> {
            int index = objList.getSelectedIndex();
            if (index <= -1)
                return;
            devWin.getWorld().getQuobjects().get(objList.getSelectedIndex()).setZ((Double)z.getValue());
        });

        JLabel zl = new JLabel("Z - ");
        menu.add(zl);
        devWin.setSizeOf(zl, .12,.11);
        devWin.setPosOf(zl, 1.2, .475);

        //Back
        JButton back = new JButton("Back");
        menu.add(back);
        devWin.setSizeOf(back, .5,.1);
        devWin.setPosOf(back, 1.2, 1.6);
        back.addActionListener(e -> {
            devWin.mainMenu();
        });

        //Object List
        objList = new JList<>();
        objList.addListSelectionListener(e -> {
            int index = objList.getSelectedIndex();
            if (index <= -1)
                return;
            Quisition pos = devWin.getWorld().getQuobjects().get(index).getPos();
            x.setValue(pos.x);
            y.setValue(pos.y);
            z.setValue(pos.z);
        });
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
        Quworld world = devWin.getWorld();
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
