package me.xjcyan1de.cyanbot.tasks;

import com.adamki11s.pathing.AStar;
import com.adamki11s.pathing.PathingResult;
import com.adamki11s.pathing.Tile;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.world.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class MoveTask {
    private Player player;

    public MoveTask(Player player) {
        this.player = player;
    }

    public void runPathing(final Location start, final Location end, final int range) {
        try {
            //create our pathfinder
            AStar path = new AStar(player.getWorld(), start, end, range);
            //get the list of nodes to walk to as a Tile object
            ArrayList<Tile> route = path.iterate();
            //get the result of the path trace
            PathingResult result = path.getPathingResult();

            switch (result) {
                case SUCCESS:
                    //Path was successful. Do something here.
                    moveAlong(start, route);
                    System.out.println("Path was found! :D");
                    break;
                case NO_PATH:
                    //No path found, throw error.
                    System.out.println("No path found!");
                    //c.chat.sendMessage("No path was found :(");
                    break;
                default:
                    //c.chat.sendMessage("Well... appearently we didn't find a path and we did... at the same time");
                    break;
            }
        } catch (AStar.InvalidPathException e) {
            //InvalidPathException will be thrown if start or end block is air
            if (e.isEndNotSolid()) {
                //System.out.println("End block is not walkable");
                //c.chat.sendMessage("Unable to walk on the block you are standing on.");
            }
            if (e.isStartNotSolid()) {
                System.out.println("Start block is not walkable");
            }
        }
    }

    private void moveAlong(final Location start, ArrayList<Tile> tiles) {
        Timer timer = new Timer();
        final Iterator<Tile> itr = tiles.iterator();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (itr.hasNext()) {
                    Tile t = itr.next();
                    Location loc = t.getLocation(start);
                    //c.gui.addText("Position is: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
                    //c.chat.sendMessage("Trying to move to: " + loc);
                    calcMovement(new Location(loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5));
             /* if(next != null){
               Location loc = next.getLocation(start);
               next = itr.next();
               Location locs = next.getLocation(start);
               double newYaw = 0, newPitch = 0;
               double xDiff = locs.getX() - c.location.getX();
			   double yDiff = locs.getY() - (c.location.getY() - 1);
			   double zDiff = locs.getZ() - c.location.getZ();
			   double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
			   double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
			   newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
			   newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
			   if(zDiff < 0.0) {
				 newYaw = newYaw + Math.abs(180 - newYaw) * 2;
			   }
			   c.yaw = (float) newYaw;
			   c.pitch = (float) newPitch;
			   if(!itr.hasNext()){
				   next = null;
			   }
               calcMovement(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
              }else{
               Tile t = itr.next();
               next = itr.next();
               Location locs = next.getLocation(start);
               double newYaw = 0, newPitch = 0;
               double xDiff = locs.getX() - c.location.getX();
			   double yDiff = locs.getY() - (c.location.getY() - 1);
			   double zDiff = locs.getZ() - c.location.getZ();
			   double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
			   double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
			   newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
			   newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
			   if(zDiff < 0.0) {
				 newYaw = newYaw + Math.abs(180 - newYaw) * 2;
			   }
               Location loc = t.getLocation(start);
               calcMovement(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
              }*/
                } else {
                    this.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 100);
    }

    /**
     * Take the delta: deltax deltay deltaz
     * Divide the delta by the amount of smaller steps you want to make
     * Then apply the divided difference to the current position in increments and  most importantly when we are done we will move to the TARGET POSITION USING NO DELTAS
     * Note that the divided increments can be changed to allow for speed modifications, or the entire loop can be slowed,
     * this is a question of preference although in order to standardize the walking speed it should probably be done via a
     * timed loop to prevent slower PCs running slower than faster ones (it will mess up the timing on the for loop)
     **/

    public boolean calcMovement(Location l) {
        boolean canGo = false;
        //checks that the place we are going to is safe
        if (!player.getWorld().getBlockAt(l).getRelative(0, 1, 0).getBoundBox().isSolid()) {//start by checking if we can go there
            //now check if the head is safe
            if (!player.getWorld().getBlockAt(l).getRelative(0, 2, 0).getBoundBox().isSolid()) {
                //yay it seems clear, so now we can go there
            	/*int steps = 3; //the amount of steps to take
            	double deltax = (l.getX() - c.location.getX())/steps;
            	double deltay = (l.getY()+1 - c.location.getY());
            	double deltaz = (l.getZ() - c.location.getZ())/steps;
            	while(--steps > 0){
            		c.location.setX(c.location.getX() + deltax);
            		c.location.setY(c.location.getY() + deltay);
            		c.location.setZ(c.location.getZ() + deltaz);
            		tick();
            	}*/
                player.getLoc().setX(l.getX());
                player.getLoc().setY(l.getY() + 1);// HAHAHAHAHAH WOW
                player.getLoc().setZ(l.getZ());
                //c.chat.sendMessage("Location: " + l.getBlockX() + ", " + (l.getBlockY()+1) + "," + l.getBlockZ() + " " + c.location.getX() + ", " + c.location.getY() + ", " + c.location.getZ());
                canGo = true;
            }
        }//if we can't go there then we will return false
        return canGo;
    }


    public void goToLocation(int x, int y, int z) {
        Location l = player.getLoc().clone().add(0, -1, 0);
        Location loc = new Location(x, y - 1, z);
        runPathing(l, loc, 100);
    }
}
