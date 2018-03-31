package me.xjcyan1de.cyanbot.utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JFrameUtils {

    public static <T> List<T> getAllItems(JComboBox<T> comboBox) {
        final ArrayList<T> objects = new ArrayList<>(comboBox.getItemCount());
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            objects.add(comboBox.getItemAt(i));
        }
        return objects;
    }
}
