package me.xjcyan1de.cyanbot.world;

public class BoundBox {

    protected double[] d; //тута всякая дата
    private Vector center; //Просто центр энтити
    private boolean solid;

    //у игрока баунд бокс размером [-0.3, 0, -0.3, 0.3, 1.62, 0.3]
    public BoundBox(double width, double height) {
        this(width, height, true);
    }

    public BoundBox(double width, double height, boolean solid) {
        this.solid = solid;
        center = new Vector();
        d = new double[3];
        d[0] = width * 0.5D;
        d[1] = height * 0.5D;
        d[2] = width * 0.5D;
    }

    public Vector getCenter() {
        return center;
    }

    public void update(Vector position) {
        this.center.setX(position.getX());
        this.center.setY(position.getY());
        this.center.setZ(position.getZ());
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean doesOverlap(BoundBox boundBox) {
        if (Math.abs(this.center.getX() - boundBox.center.getX()) > (this.d[0] + boundBox.d[0])) {
            return false;
        }
        if (Math.abs(this.center.getY() - boundBox.center.getY()) > (this.d[1] + boundBox.d[1])) {
            return false;
        }
        return !(Math.abs(this.center.getZ() - boundBox.center.getZ()) > (this.d[2] + boundBox.d[2]));
    }

    public boolean willOverlap(double x, double y, double z) {
        BoundBox temp = new BoundBox(this.d[0] * 2, this.d[1] * 2);
        temp.update(new Vector(x, y, z));
        double vx = temp.center.getX() - this.center.getX(), vy = temp.center.getY() - this.center.getY(), vz = temp.center.getZ() - this.center.getZ();
        if (Math.abs(vx) > (temp.d[0] + this.d[0])) {
            return false;
        }
        if (Math.abs(vy) > (temp.d[1] + this.d[1])) {
            return false;
        }
        return !(Math.abs(vz) > (temp.d[2] + this.d[2]));
    }
}
