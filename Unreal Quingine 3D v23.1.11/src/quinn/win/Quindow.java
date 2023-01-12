package quinn.win;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.SQLSyntaxErrorException;

/**
 * The window the is also a Quindow.
 * Quindow > JFrame
 * @author Quinn Graham
 */

public class Quindow extends JFrame {
    public Quindow(){
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setTitle("Quindow");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(addComponentListener());
        setLayout(null);
    }

    private Quicture pic;

    public Quicture getQuicture(){
        return pic;
    }

    @Override
    public Component add(Component component){
        if (component instanceof Quicture) {
            pic = (Quicture) component;
            pic.setSize(getWidth(), getHeight());
            pic.quindow = this;
        }
        return super.add(component);
    }

    private ComponentListener addComponentListener(){
        return new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (pic.getUnitsWide()/pic.getUnitsTall() >= 1) {
                    int height = (int)((getWidth() / ((double)pic.getUnitsWide() / pic.getUnitsTall()))+.5);
                    pic.setBounds(0, (getHeight() - height) / 2, getWidth(), height);
                }else{
                    int width = (int)((getHeight() * ((double)pic.getUnitsWide() / pic.getUnitsTall()))+.5);
                    pic.setBounds((getWidth() - width)/2, 0, width, getHeight());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        };
    }

}
