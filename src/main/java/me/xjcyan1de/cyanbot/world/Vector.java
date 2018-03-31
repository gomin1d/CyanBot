package me.xjcyan1de.cyanbot.world;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
        x = 0D;
        y = 0D;
        z = 0D;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getDistSQ(Vector vector) {
        double deltax = this.x - vector.x;
        double deltay = this.y - vector.y;
        double deltaz = this.z - vector.z;
        return deltax * deltax + deltay * deltay + deltaz * deltaz;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public double sizeSquared() {
        return x * x + y * y + z * z;
    }

    public double size() {
        return Math.sqrt(this.sizeSquared());
    }

    public Vector norm() {
        double size = this.size();
        this.x /= size;
        this.y /= size;
        this.z /= size;
        return this;
    }

    public void setSize(double size) {
        this.norm();
        this.mult(size);
    }

    private Vector mult(double mult) {
        this.x *= mult;
        this.y *= mult;
        this.z *= mult;
        return this;
    }
}
