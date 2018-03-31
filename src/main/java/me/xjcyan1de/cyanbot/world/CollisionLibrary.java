package me.xjcyan1de.cyanbot.world;

public class CollisionLibrary {
    /*Static method library for checking collisions
     * Accessed staticly with CollisionLibrary.method();
     */

    //Simple overlap test, this should return true if these two boxes overlap
    public static boolean doesOverlap(final BoundBox box1, final BoundBox box2) {
        if (Math.abs(box1.getCenter().getX() - box2.getCenter().getX()) > (box1.d[0] + box2.d[0])) {
            return false;
        }
        if (Math.abs(box1.getCenter().getY() - box2.getCenter().getY()) > (box1.d[1] + box2.d[1])) {
            return false;
        }
        return !(Math.abs(box1.getCenter().getZ() - box2.getCenter().getZ()) > (box1.d[2] + box2.d[2]));
    }

    /**
     * The first box is the box that we are and the box two is the box that we are colliding with
     **/
    public static Vector willOverlapVec(final BoundBox box1, final BoundBox box2, double x, double y, double z) {
        Vector result = new Vector();//Creates new vector, remember that vector's default is 0,0,0
        BoundBox temp = new BoundBox(box1.d[0] * 2, box1.d[1] * 2); //A temp object copy of the box that we can use to test the future
        temp.update(new Vector(x, y, z)); //our future coords
        double vx = temp.getCenter().getX() - box2.getCenter().getX(), vy = temp.getCenter().getY() - box2.getCenter().getY(), vz = temp.getCenter().getZ() - box2.getCenter().getZ();
        if (Math.abs(vx) > (temp.d[0] + box2.d[0])) { //if the distance from centre to centre is bigger than centre plus width then we obviously are coliding
            //Calculate the distance
            result.setX(vx);
        }
        if (Math.abs(vy) > (temp.d[1] + box2.d[1])) {
            result.setY(vy);
        }
        if (Math.abs(vz) > (temp.d[2] + box2.d[2])) {
            result.setZ(vz);
        }
        return result;
    }

    public static boolean willOverlap(final BoundBox box1, final BoundBox box2, double x, double y, double z) {
        BoundBox temp = new BoundBox(box2.d[0] * 2, box2.d[1] * 2); //A temp object copy of the box that we can use to test the future
        temp.update(new Vector(x, y, z)); //our future coords
        double vx = temp.getCenter().getX() - box2.getCenter().getX(), vy = temp.getCenter().getY() - box2.getCenter().getY(), vz = temp.getCenter().getZ() - box2.getCenter().getZ();
        if (Math.abs(vx) > (temp.d[0] + box2.d[0])) { //if the distance from centre to centre is bigger than centre plus width then we obviously are coliding
            //Calculate the distance
            return false;
        }
        if (Math.abs(vy) > (temp.d[1] + box2.d[1])) {
            return false;
        }
        return !(Math.abs(vz) > (temp.d[2] + box2.d[2]));
    }

}
