package me.xjcyan1de.cyanbot.gui.joystick;

import javax.swing.event.ChangeEvent;
import java.awt.*;

public class PointChangeEvent extends ChangeEvent {
    private static final long serialVersionUID = 1L;
    public Point p;

    public PointChangeEvent(Object source, Point p) {
        super(source);
        this.p = p;
    }
}
