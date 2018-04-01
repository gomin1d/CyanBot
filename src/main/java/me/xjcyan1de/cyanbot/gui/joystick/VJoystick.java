package me.xjcyan1de.cyanbot.gui.joystick;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class VJoystick extends JPanel implements ChangeListener {
    private static final long serialVersionUID = 1L;
    private JLabel lblPosition;

    public VJoystick() {
        setLayout(new BorderLayout(0, 0));
        SimpleJoystick myJoystick = new SimpleJoystick(150);
        myJoystick.setPreferredSize(new Dimension(100, 100));
        myJoystick.addChangeListener(this);
        add(myJoystick, BorderLayout.CENTER);

        lblPosition = new JLabel("position");
        add(lblPosition, BorderLayout.SOUTH);
    }

    @Override
    public void stateChanged(ChangeEvent ev) {
        Point p = null;
        try {
            p = ((PointChangeEvent) ev).p;
        } catch (Exception e) {
            return;
        }
        lblPosition.setText("x=" + p.x + " y=" + p.y);
    }
}


