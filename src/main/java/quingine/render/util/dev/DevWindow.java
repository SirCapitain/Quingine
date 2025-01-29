package quingine.render.util.dev;

import quingine.render.sim.env.Quworld;
import quingine.render.util.dev.menus.MainMenu;
import quingine.render.util.dev.menus.ModifyMenu;
import quingine.render.util.win.Quindow;

import javax.swing.*;
/**
 * THE Developer window tool. Holds menus and stuff.
 * Quite useful for developing stuff.
 */

public class DevWindow extends Quindow {

    private Quworld world;
    private int unitsWide = 2;
    private int unitsHigh = 2;

    /**
     * Create a new developer window
     * @param world the quworld being edited with the window
     */
    public DevWindow(Quworld world) {
        this.world = world;
        init();
    }

    /**
     * Initializing the window and the menus
     */
    private void init(){
        setSize(300,500);
        setName("Dev");
        MainMenu.init(this);
        add(MainMenu.menu);
        ModifyMenu.init(this);
        add(ModifyMenu.menu);

    }

    /**
     * Set the size of a component based on the units wide and high of the window
     * @param component any JComponent
     * @param unitsWide how wide is the component in units
     * @param unitsHigh how tall is the component in units
     */
    public void setSizeOf(JComponent component, double unitsWide, double unitsHigh){
        component.setSize((int)(unitsWide/this.unitsWide*getWidth() + .5), (int)(unitsHigh/this.unitsHigh*getHeight() + .5));
    }

    /**
     * Set the position of a component based on the units wide and high of the window
     * @param component any JComponent
     * @param posX position of component in units
     * @param posY position of component in units
     */
    public void setPosOf(JComponent component, double posX, double posY){
        component.setLocation((int)(posX/unitsWide*getWidth()), (int)(posY/unitsHigh*getHeight()));
    }

    /**
     * Get the current quworld being edited.
     * @return current Quworld
     */
    public Quworld getWorld(){
        return world;
    }

    /**
     * Pull up the modify menu.
     */
    public void modifyMenu(){
        MainMenu.menu.setVisible(false);
        ModifyMenu.menu.setVisible(true);
        ModifyMenu.refreshObjectList();
    }

    /**
     * Pull up the main menu
     */
    public void mainMenu(){
        MainMenu.menu.setVisible(true);
        MainMenu.refreshObjectList();
        ModifyMenu.menu.setVisible(false);
    }

    /**
     * No download required! Just update whenever!
     */
    @Override
    public void update() {
        SwingUtilities.updateComponentTreeUI(this);
    }
}
