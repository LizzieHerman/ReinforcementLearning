package reinforcementlearning;

import java.util.Random;

/**
 *
 * @author Lizzie Herman
 */
public class RaceCar {
    private int xpos;
    private int ypos;
    private int xvel;
    private int yvel, facing;
    private int[] accel, facingVal;
    
    public RaceCar(int x, int y){
        xpos = x;
        ypos = y;
        xvel = 0;
        yvel = 0;
        this.facingVal = new int[]{0,0}; //Set up variable to watch for a facing change
    }
    
    public int getXPos(){
        return xpos;
    }

    public int getYPos(){
        return ypos;
    }

    public int getXVel(){
        return xvel;
    }

    public int getYVel(){
        return yvel;
    }
    
    public void setPos(int x, int y){
        xpos = x;
        ypos = y;
    }
    
    public void setVel(int x, int y){
        xvel = x;
        yvel = y;
    }
    
    // t is the amount of time that happened during that step not total elapsed time
    public int[] accelerate(int xacc, int yacc, double t){
        Random random = new Random();
        double accelerate = random.nextDouble();
        // 80% chance of actually accelerating
        if(accelerate < 0.8){
            xvel += (xacc * t);
            yvel += (yacc * t);
        }
        xpos += (xvel * t);
        ypos += (yvel * t);
        int[] pos = {xpos, ypos};
        return pos;
    }
    
    public boolean accelNotWork(int xacc, int yacc, int t){
        if(Math.abs(xacc) > 1 || Math.abs(yacc) > 1) return true;
        if(Math.abs(xvel + (xacc * t)) > 5 || Math.abs(yvel + (yacc * t)) > 5) return true;
        return false;
    }
    
    public void startFacing(char symbol){
    	if(symbol == 'L'){
    		this.facing = 2;
    	}else{
    		this.facing = 1;
    	}
    }
    
    public int getFacing(){
    	return this.facing;
    }
    
    public void setAccel(int[] accel){
    	this.accel = accel;
    }
    
    public void updateFacing(int xvel, int yvel){
    	if(xvel == 0 && yvel == 0){
    		if(this.accel[0] == 0){
    			this.facingVal = new int[]{7, this.accel[1]}; //Start watching the y velocity
    		}else{
    			this.facingVal = new int[]{6, this.accel[0]}; //Start watching the x velocity
    		}
    	}else if(this.facingVal[0] == 7){  //7 = y direction
    		this.facingVal[1] += this.accel[1]; //Update the value being watched - y velocity
    		if(this.facingVal[1] == 0){
    			if((int)Math.signum(xvel) == 1){ //Determine the direction of movement
    				this.facing = 2;
    			}else{
    				this.facing = 0;
    			}
    			this.facingVal = new int[]{6, xvel}; //Set value to watch to x and the x velocity value
    		}
    	}else{  //6 = x direction
    		this.facingVal[1] += this.accel[0]; //Update the value being watched - x velocity
    		if(this.facingVal[1] == 0){
    			if((int)Math.signum(yvel) == 1){ //Determine the direction of movement
    				this.facing = 1;
    			}else{
    				this.facing = 3;
    			}
    			this.facingVal = new int[]{7, yvel}; //Set value to watch to y and the y velocity value
    		}
    	}
    }
}
