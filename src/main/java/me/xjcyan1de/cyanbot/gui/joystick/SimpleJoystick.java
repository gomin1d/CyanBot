package me.xjcyan1de.cyanbot.gui.joystick;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleJoystick extends JPanel {
    private static final long serialVersionUID = 1L;
    /**
     * Maximum value for full horiz or vert position where centered is 0
     */
    private int joyOutputRange;
    /**
     * max x and y value, in pixels
     */
    private int joyRadius;
    /**
     * Joystick displayed Center, in pixels
     */
    private int joyCenterX, joyCenterY;
    /**
     * joystick output position scaled to given joyOutputRange
     */
    private Point position = new Point();
    /**
     * joystick x axis value in pixels
     */
    private int dx = 0;
    /**
     * joystick y axis value in pixels
     */
    private int dy = 0;

    /**
     * @param joyOutputRange
     */
    public SimpleJoystick(final int joyOutputRange) {
        this.joyOutputRange = joyOutputRange;
        setBackground(new Color(226, 226, 226));
        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && cursorChanged(e.getX(), e.getY())) {
                    SwingUtilities.getRoot(SimpleJoystick.this).repaint();
                    fireStateChanged();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cursorChanged(joyCenterX, joyCenterY);
                SwingUtilities.getRoot(SimpleJoystick.this).repaint();
                fireStateChanged();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && cursorChanged(e.getX(), e.getY())) {
                    SwingUtilities.getRoot(SimpleJoystick.this).repaint();
                    fireStateChanged();
                }
            }
        };
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
    }

    private boolean cursorChanged(int mouseX, int mouseY) {
        if (joyRadius == 0) return false;

        dx = mouseX - joyCenterX;
        dy = mouseY - joyCenterY;
        if (dx > joyRadius) dx = joyRadius;
        if (dy > joyRadius) dy = joyRadius;
        if (dx < -joyRadius) dx = -joyRadius;
        if (dy < -joyRadius) dy = -joyRadius;

        position.x = joyOutputRange * dx / joyRadius;
        position.y = -joyOutputRange * dy / joyRadius;

        return true;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int joyWidth = getSize().width;
        int joyHeight = getSize().height;
        joyRadius = Math.min(joyWidth, joyHeight) / 2;
        if (joyRadius == 0) return;

        joyCenterX = joyWidth / 2;
        joyCenterY = joyHeight / 2;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int diameter;

        //background
        g2.setColor(Color.LIGHT_GRAY);
        diameter = joyRadius * 2;
        g2.fillOval(joyCenterX - diameter / 2, joyCenterY - diameter / 2, diameter, diameter);

        g2.setColor(Color.RED);
        diameter = 40;
        g2.fillOval(joyCenterX + dx - diameter / 2, joyCenterY + dy - diameter / 2, diameter, diameter);

        //thumb
        g2.setColor(Color.GRAY);
        diameter = 20;
        g2.fillOval(joyCenterX - diameter / 2, joyCenterY - diameter / 2, diameter, diameter);
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    protected void fireStateChanged() {
        ChangeEvent e = new PointChangeEvent(this, position);
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
}

