package me.xjcyan1de.cyanbot.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {

    public static String SEP = File.separator;
    private static Yaml yaml = new Yaml();

    private boolean first = false;

    private File file;
    private Map map;

    public Config(String path) {
        file = new File("." + SEP + path);
        this.reload();
    }

    /**
     * Получить или вставить новое значение в конфиг
     * @param path путь
     * @param def  значение по уполчанию
     * @param <T>
     * @return значние их конфига или значение по умолчанию
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrSet(String path, T def) {
        if(!this.contains(path)) {
            this.setAndSave(path, def);
            return def;
        } else {
            return (T) this.get(path);
        }
    }

    /**
     * Получить или вставить новое значение в конфиг типа <code>? extends Number</code><br>
     * Добавлено в связи с тем, что часто возникают прроблемы с ClassCastException (int -> long, double - float)
     * @param path путь
     * @param def  значение по уполчанию
     * @return значние их конфига или значение по умолчанию
     */
    public Number getOrSetNumber(String path, Number def) {
        if(!this.contains(path)) {
            this.setAndSave(path, def);
            return def;
        } else {
            return ((Number) this.get(path));
        }
    }

    public File getFile() {
        return file;
    }

    public void reload() {
        try {
            if (!file.exists()) {
                this.first = true;
                file.createNewFile();
            }
            FileInputStream inputStream = new FileInputStream(file);
            this.map = yaml.loadAs(inputStream, Map.class);
            if (map == null)
                map = new LinkedHashMap();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefault(String path, Object o) {
        if (this.first) {
            this.setIfNotExist(path, o);
        }
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public String getString(String path) {
        Object o = this.get(path);
        return o == null ? null : o.toString();
    }

    public Object get(String path) {
        Object current = this.map;
        for (String next : path.split("\\.")) {
            current = ((Map)current).get(next);
        }
        return current;
    }

    public void setIfNotExist(String path, Object o) {
        if (!this.contains(path)) {
            this.setAndSave(path, o);
        }
    }

    public void setAndSave(String path, Object o) {
        this.set(path, o);
        this.save();
    }

    public boolean contains(String path) {
        try {
            Object current = this.map;
            for (String next : path.split("\\.")) {
                current = ((Map)current).get(next);
            }
            return current != null;
        } catch (NullPointerException | ClassCastException e) {
            return false;
        }
    }

    public void set(String path, Object o) {
        Map current = this.map;
        String[] data = path.split("\\.");
        for (int i = 0; i < data.length - 1; i++) {
            String next = data[i];
            Map old = current;
            current = (Map) current.get(next);
            if (current == null) {
                old.put(next, current = new LinkedHashMap());
            }
        }
        if (o != null) {
            current.put(data[data.length - 1], o);
        } else {
            current.remove(data[data.length - 1]);
        }
    }

    public void save() {
        try {
            String data = yaml.dumpAs(this.map, null, DumperOptions.FlowStyle.BLOCK);
            FileOutputStream outputStream = new FileOutputStream(this.file);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String path) {
        return (boolean) this.get(path);
    }

    public boolean isFirst() {
        return first;
    }
}
