package quingine.render.util.dev.menus;

import quingine.render.sim.Math3D;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.light.LightSource;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.util.dev.DevWindow;
import quingine.render.util.win.Quomponent;

import javax.swing.*;
import java.awt.*;

/**
 * The Modify menu in the Developer tools window
 * Look... UI design is not my top skill.
 * Nor is optimization of said UI.
 * Gimme a break. First time here.
 */

public class ModifyMenu {

    public static JPanel menu;

    private static DevWindow devWin;

    private static JSpinner x, y, z;
    private static JSpinner stepSize;
    private static JSpinner yaw, pitch, roll;
    private static JSpinner stepSizeR;
    private static JSpinner objSize;
    private static JSpinner power;


    private static JLabel sl, xl, yl, zl;
    private static JLabel ywl, ptl, rll;
    private static JLabel sizel;
    private static JLabel powerl;

    private static JButton st, so;
    private static JButton back;

    private static JTextField nameField;

    private static JCheckBox al, av;

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
        initPositionSpinners();
        initPositionLabels();
        initRotationSpinners();
        initRotationLabels();
        initButtons();
        initLists();
        initAttributes();
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

    /**
     * Set if the position spinners are visible or not
     * @param visible true if visible, false if not
     */
    public static void positionVisible(boolean visible){
        x.setVisible(visible);
        xl.setVisible(visible);
        y.setVisible(visible);
        yl.setVisible(visible);
        z.setVisible(visible);
        zl.setVisible(visible);
    }

    /**
     * Set if the rotation spinners are visible or not for the quobject
     * @param visible true if visible, false if not
     */
    public static void rotationVisible(boolean visible){
        yaw.setVisible(visible);
        ywl.setVisible(visible);
        pitch.setVisible(visible);
        ptl.setVisible(visible);
        roll.setVisible(visible);
        rll.setVisible(visible);
        stepSizeR.setVisible(visible);
    }

    /**
     * Set the attributes for a quobject to be visible or not
     * @param visible true if visible, false if not.
     */
    public static void attributesVisible(boolean visible){
        al.setVisible(visible);
        av.setVisible(visible);
        sizel.setVisible(visible);
        objSize.setVisible(visible);
    }


    private static void initPositionSpinners(){
        //StepSize
        stepSize = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE,Double.MAX_VALUE,1));
        menu.add(stepSize);
        devWin.setSizeOf(stepSize, .4,.1);
        devWin.setPosOf(stepSize, 1.44, .65);
        stepSize.addChangeListener(e -> {
            x.setModel(new SpinnerNumberModel((double)x.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
            y.setModel(new SpinnerNumberModel((double)y.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
            z.setModel(new SpinnerNumberModel((double)z.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
        });

        //X
        x = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSize.getValue()));
        menu.add(x);
        devWin.setSizeOf(x, .4,.1);
        devWin.setPosOf(x, 1.4441, .2);
        x.addChangeListener(e -> {
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            quom.setX((double)x.getValue());
        });

        //Y
        y = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE,(double)stepSize.getValue()));
        menu.add(y);
        devWin.setSizeOf(y, .4,.1);
        devWin.setPosOf(y, 1.44, .35);
        y.addChangeListener(e ->{
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            quom.setY((double)y.getValue());
        });

        //Z
        z = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE,(double)stepSize.getValue()));
        menu.add(z);
        devWin.setSizeOf(z, .4,.1);
        devWin.setPosOf(z, 1.44, .5);
        z.addChangeListener(e -> {
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            quom.setZ((Double)z.getValue());
        });
    }
    private static void initRotationSpinners(){
        //StepSize
        stepSizeR = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE,Double.MAX_VALUE,1));
        menu.add(stepSizeR);
        devWin.setSizeOf(stepSizeR, .4,.1);
        devWin.setPosOf(stepSizeR, .65, .65);
        stepSizeR.addChangeListener(e -> {
            yaw.setModel(new SpinnerNumberModel((double)yaw.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSizeR.getValue()));
            pitch.setModel(new SpinnerNumberModel((double)pitch.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSizeR.getValue()));
            roll.setModel(new SpinnerNumberModel((double)roll.getValue(), Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSizeR.getValue()));
        });

        //Yaw
        yaw = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE, (double)stepSizeR.getValue()));
        menu.add(yaw);
        devWin.setSizeOf(yaw, .4,.1);
        devWin.setPosOf(yaw, .8, .2);
        yaw.addChangeListener(e -> {
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            double[] rot = Math3D.quaternionToEuler(obj.getRotation());
            obj.setRotation(Math.toRadians((double)yaw.getValue()),rot[1],rot[2]);
        });

        //Pitch
        pitch = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE,(double)stepSizeR.getValue()));
        menu.add(pitch);
        devWin.setSizeOf(pitch, .4,.1);
        devWin.setPosOf(pitch, .8, .35);
        pitch.addChangeListener(e ->{
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            double[] rot = Math3D.quaternionToEuler(obj.getRotation());
            obj.setRotation(rot[0], Math.toRadians((double)pitch.getValue()), rot[2]);
        });

        //Roll
        roll = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,Double.MAX_VALUE,(double)stepSizeR.getValue()));
        menu.add(roll);
        devWin.setSizeOf(roll, .4,.1);
        devWin.setPosOf(roll, .8, .5);
        roll.addChangeListener(e -> {
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            double[] rot = Math3D.quaternionToEuler(obj.getRotation());
            obj.setRotation(rot[0],rot[1],Math.toRadians((double)roll.getValue()));
        });
    }
    private static void initPositionLabels(){
        sl = new JLabel("Step Size");
        sl.setFont(new Font("Serif", Font.BOLD,12));
        menu.add(sl);
        devWin.setSizeOf(sl, .43,.11);
        devWin.setPosOf(sl, 1.08, .637);


        xl = new JLabel("X ");
        menu.add(xl);
        devWin.setSizeOf(xl, .15,.11);
        devWin.setPosOf(xl, 1.35, .175);

        yl = new JLabel("Y ");
        menu.add(yl);
        devWin.setSizeOf(yl, .15,.11);
        devWin.setPosOf(yl, 1.35, .325);

        zl = new JLabel("Z ");
        menu.add(zl);
        devWin.setSizeOf(zl, .15,.11);
        devWin.setPosOf(zl, 1.35, .475);
    }
    private static void initRotationLabels(){
        ywl = new JLabel("Yaw ");
        menu.add(ywl);
        devWin.setSizeOf(ywl, .25,.11);
        devWin.setPosOf(ywl, .6, .175);

        ptl = new JLabel("Pitch ");
        menu.add(ptl);
        devWin.setSizeOf(ptl, .25,.11);
        devWin.setPosOf(ptl, .6, .325);

        rll = new JLabel("Roll ");
        menu.add(rll);
        devWin.setSizeOf(rll, .25,.11);
        devWin.setPosOf(rll, .6, .475);
    }
    private static void initButtons(){
        //Texture
        st = new JButton("Set Texture");
        st.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(st);
        devWin.setSizeOf(st, .6,.1);
        devWin.setPosOf(st, 1.25, .8);
        st.addActionListener(e ->{
            String texture = JOptionPane.showInputDialog(menu,
                    "Texture Name + filetype (Must be in textures folder)", null);
            if (texture == null)
                return;
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            obj.setTexture(texture);
        });

        //Object
        so = new JButton("Set Quobject");
        so.setFont(new Font("Serif", Font.BOLD,10));
        menu.add(so);
        devWin.setSizeOf(so, .6,.1);
        devWin.setPosOf(so, .64, .8);
        so.addActionListener(e ->{
            String object = JOptionPane.showInputDialog(menu,
                    "Object File (must be in objects folder)", null);
            if (object == null)
                return;
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            obj.loadQuobjectFile(object);
        });

        //Name
        nameField = new JTextField();
        menu.add(nameField);
        devWin.setSizeOf(nameField, 1.22,.1);
        devWin.setPosOf(nameField, .64, .95);
        nameField.addActionListener(e -> {
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            quom.setName(nameField.getText());
            refreshObjectList();
        });

        //Object Size
        objSize = new JSpinner(new SpinnerNumberModel(1, Double.MIN_VALUE,Double.MAX_VALUE,.1));
        menu.add(objSize);
        devWin.setSizeOf(objSize, .5,.1);
        devWin.setPosOf(objSize, 1.36, 1.1);
        objSize.addChangeListener(e -> {
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            obj.setSize((double)objSize.getValue());
        });

        sizel = new JLabel("Size - ");
        menu.add(sizel);
        devWin.setSizeOf(sizel, .25,.1);
        devWin.setPosOf(sizel, .9, 1.1);

        power = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE,Double.MAX_VALUE,1));
        menu.add(power);
        devWin.setSizeOf(power, .5,.1);
        devWin.setPosOf(power, 1.36, 1.1);
        power.addChangeListener(e -> {
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            if (quom instanceof LightSource ls)
                ls.setPower((double)power.getValue());
        });
        power.setVisible(false);
        powerl = new JLabel("Power - ");
        menu.add(powerl);
        powerl.setVisible(false);
        devWin.setSizeOf(powerl, .35,.1);
        devWin.setPosOf(powerl, .85, 1.1);

        //Back
        back = new JButton("Back");
        menu.add(back);
        devWin.setSizeOf(back, .5,.1);
        devWin.setPosOf(back, 1.2, 1.7);
        back.addActionListener(e -> devWin.mainMenu());
    }
    private static void initLists(){
        //Object List
        objList = new JList<>();
        objList.addListSelectionListener(e -> {
            Quomponent quom = getSelectedQuomponent();
            if (quom == null)
                return;
            if (quom instanceof Quobject obj) {
                rotationVisible(true);
                attributesVisible(true);
                so.setVisible(true);
                st.setVisible(true);
                power.setVisible(false);
                powerl.setVisible(false);
                objSize.setValue(obj.getSize());
                double[] rot = Math3D.quaternionToEuler(obj.getRotation());
                yaw.setValue(Math.toDegrees(rot[0]));
                pitch.setValue(Math.toDegrees(rot[1]));
                roll.setValue(Math.toDegrees(rot[2]));
                al.setSelected(obj.alwaysLit());
                av.setSelected(obj.isVisible());
            } else if (quom instanceof LightSource ls) {
                rotationVisible(false);
                attributesVisible(false);
                power.setVisible(true);
                powerl.setVisible(true);
                so.setVisible(false);
                st.setVisible(false);
                power.setValue(ls.getPower());
            }
            x.setValue(quom.getPos().x);
            y.setValue(quom.getPos().y);
            z.setValue(quom.getPos().z);
            nameField.setText(quom.getName());
        });
        objScroll = new JScrollPane(objList);
        devWin.setSizeOf(objScroll, .6,1.7);
        devWin.setPosOf(objScroll, 0,.1);
        refreshObjectList();
        menu.add(objScroll);
        devWin.update();
    }
    private static void initAttributes(){
        //Always Lit
        al = new JCheckBox();
        menu.add(al);
        al.setText("Always Lit");
        devWin.setSizeOf(al, .6, .1);
        devWin.setPosOf(al, .64, 1.2);
        al.addActionListener(e -> {
           Quobject obj = getSelectedQuobject();
           if (obj == null)
               return;
           obj.alwaysLit(al.isSelected());
        });

        //Visible
        av = new JCheckBox();
        menu.add(av);
        av.setText("Is Visible");
        devWin.setSizeOf(av, .6, .1);
        devWin.setPosOf(av, 1.3, 1.2);
        av.addActionListener(e -> {
            Quobject obj = getSelectedQuobject();
            if (obj == null)
                return;
            obj.isVisible(av.isSelected());
        });
    }
}