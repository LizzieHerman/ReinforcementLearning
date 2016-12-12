package reinforcementlearning;

import java.util.*;

/**
 *
 * @author Lizzie Herman
 */
public class Algorithm {
    protected RaceTrack track;
    protected RaceCar car;
    private double time;
    private int cost;
    private TrackGUI gui;
    private int[] orgPos;
    protected Random random;
    private int crash;
    private int move;
    protected ArrayList<int[]> possMoves = new ArrayList<int[]>();
    protected char[][] knownTrack;
    
    public Algorithm(RaceTrack t, RaceCar c, TrackGUI g){
        track = t;
        car = c;
        gui = g;
        orgPos = new int[2];
        random = new Random();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int[] temp = {i,j};
                possMoves.add(temp);
            }
        }
        int[] size = track.getSize();
        knownTrack = new char[size[1]][size[0]];
    }
    
    public void runCar(boolean hit){
        crash = 0;
        move = 0;
        time = 0;
        cost = 0;
        orgPos[0] = car.getXPos();
        orgPos[1] = car.getYPos();
        boolean finished = false;
        int lastxp, lastyp;
        int i=0;
        while(! finished){
        	if(i>10000){
        		System.out.println("ran too slow");
        		break;
        	}
        	i++;
            lastxp = car.getXPos();
            lastyp = car.getYPos();
            knownTrack[lastxp][lastyp] = '.';
            gui.updateTrack(car.getXPos(), car.getYPos(), car.getXVel(), car.getYVel(), time, cost);
            int[] accl = findNextMove(car);
            if(car.accelNotWork(accl[0], accl[1], 1)) continue;
            cost++;
            time += 1;
            car.accelerate(accl[0], accl[1], 1);
            if(track.notCrash(car.getXPos(), car.getYPos(), lastxp, lastyp)){
                move++;
                if(track.getCell(car.getXPos(), car.getYPos()) == 'F'){
                    //System.out.println("last position: (" + lastxp + "," + lastyp + ")");
                    System.out.print("final position: (" + car.getXPos() + "," + car.getYPos() + ")");
                    System.out.print(" Number of times acceleration was possible: " + move);
                    System.out.println(" Number of crashes to get here: " + crash);
                    cost--;
                    finished = true;
                }
            } else {
                crash++;
                try {
                    knownTrack[car.getXPos()][car.getYPos()] = '#';
                } catch(IndexOutOfBoundsException e){
                    //System.out.println("Car Jumped completely off the board");
                }
                
            // car.setPos(lastxp, lastyp); car.setVel(0, 0);
                //System.out.println("Car crashed into wall trying to get to (" + car.getXPos() + "," + car.getYPos() + ")");
                if(hit){
                    hitWall();
                    //System.out.println("Car sent to (" + car.getXPos() + "," + car.getYPos() + ")");
                } else {
                    hitWall(lastxp, lastyp);
                    //System.out.println("Car sent to original position (" + orgPos[0] + "," + orgPos[1] + ")");
                }
                if(! track.cellSafe(car.getXPos(), car.getYPos())){
                    //gui.updateTrack(car.getXPos(), car.getYPos(), car.getXVel(), car.getYVel(), time, cost);
                    System.out.println("Program messed up");
                    return;
                }
            }
        }
    }
    
    // when sent back to starting position
    public void hitWall(){
        car.setPos(orgPos[0], orgPos[1]);
        car.setVel(0, 0);
        car.setReset(true);
    }
    
    // when sent back to closest empty site of crash
    // x1, y1 are position before crash, car is set to pos of crash
    public void hitWall(int x1, int y1){
        car.setVel(0, 0);
        int x2 = car.getXPos();
        int y2 = car.getYPos();
        int lastx = orgPos[0];
        int lasty = orgPos[1];
        int changex = Math.abs(x1-x2);
        int changey = Math.abs(y1-y2);
        int x,y;
        if(changex == 0){
            lastx = x1;
            if(y1<y2){
                for(; y1 < y2; y1++){
                    if(track.cellSafe(lastx, y1)) lasty = y1;
                    else break;
                }
            } else {
                for(; y1 > y2; y1--){
                    if(track.cellSafe(lastx, y1)) lasty = y1;
                    else break;
                    
                }
            }
            //System.out.println("horizontal: " + x2 + " " + y2);
        } else if(changey == 0){
            lasty = y1;
            if(x1<x2){
                for(; x1 < x2; x1++){
                    if(track.cellSafe(x1, lasty)) lastx = x1;
                    else break;
                }
            } else {
                for(; x1 > x2; x1--){
                    if(track.cellSafe(x1, lasty)) lastx = x1;
                    else break;
                    
                }
            }
            //System.out.println("vertical: " + x2 + " " + y2);
        } else {
            double slope = changey / changex;
            double yd;
            y = y1;
            yd = y1;
            x = x1;
            if(x1<x2){
                for(; x < x2; x++){
                    if(y1<y2) yd += slope;
                    else yd -= slope;
                    y = (int) yd;
                    if(!((y-1) < y1 && (y-1) < y2) && (y-1) == (int)(yd-slope)){
                        if(track.cellSafe(x, y-1)){
                            lastx = x;
                            lasty = y-1;
                        } else break;
                    }
                    if(track.cellSafe(x, y)){
                        lastx = x;
                        lasty = y;
                    } else break;
                    if(!((y+1) > y1 && (y+1) > y2) && (y+1) == (int)(yd+slope)){
                        if(track.cellSafe(x, y+1)){
                            lastx = x;
                            lasty = y+1;
                        } else break;
                    }
                }
            } else {
                for(; x > x2; x--){
                    if(y1<y2) yd += slope;
                    else yd -= slope;
                    y = (int) yd;
                    if(!((y-1) < y1 && (y-1) < y2) && (y-1) == (int)(yd-slope)){
                        if(track.cellSafe(x, y-1)){
                            lastx = x;
                            lasty = y-1;
                        } else break;
                    }
                    if(track.cellSafe(x, y)){
                        lastx = x;
                        lasty = y;
                    } else break;
                    if(!((y+1) > y1 && (y+1) > y2) && (y+1) == (int)(yd+slope)){
                        if(track.cellSafe(x, y+1)){
                            lastx = x;
                            lasty = y+1;
                        } else break;
                    }
                }
            }
            //System.out.println("slope: " + x2 + " " + y2);
        }
        car.setPos(lastx, lasty);
    }
    
    public int getCost(){
        return cost;
    }
    
    public double getTime(){
        return time;
    }
    
    // this method returns a double array of accel it wants to do
    // this is the method we need to change
    public int[] findNextMove(RaceCar car2){
        int a = random.nextInt(possMoves.size());
        int[] accl = possMoves.get(a);
        return accl;
    }
    
    public String toString(){
        return "Algorithm\nOriginal Position: (" + orgPos[0] + "," + orgPos[1] + ") Time of Trial: " + time + " Cost of Trial: " + cost;
    }
}
