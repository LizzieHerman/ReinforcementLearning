package reinforcementlearning;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Lizzie Herman
 * Connor O'Leary
 */
public class ValueIteration extends Algorithm{
    Random random = new Random();
    ArrayList<int[]> values;
    RaceTrack t;
    RaceCar c;

    public ValueIteration(RaceTrack tIn, RaceCar cIn, TrackGUI gIn) {
        super(tIn, cIn, gIn);
        t = tIn;
        c = cIn;
        
        values = initializeStateValues();
    }
    
    public int[] findNextMove(){
    	
        int[] accl = {0,0};
        //int[] carState = {c.getXPos(),c.getYPos(),c.getXVel(),c.getYVel()};
        int[] currState={0,0,0,0,0};
        for(int [] state : values){
            if(matches(c, state)){
            	currState=state;
                break;
            }
        }
        //int currVal = currState[4];
        RaceCar car = new RaceCar(c.getXPos(),c.getXPos());
        int[] maxState = {0,0,0,0,0};
        boolean found=false;
        // Find each possible next state based on possible accelerations and select best acceleration
        for(int accX = -1; accX<2; accX++){
            for(int accY = -1; accY<2; accY++){
                
                car.setVel(c.getXVel()+accX,c.getYVel()+accY);
                car.setPos(c.getXVel()+accX+c.getXPos(),c.getYVel()+accY+c.getYPos());
                
                for(int[] nextState:values){ // find the corresponding value
                    if(matches(car, nextState)){
                        if(currState[4]>maxState[4]){ // best next value
                        	//System.out.println(maxState[4]);
                        	found=true;
                            maxState=nextState;
                            accl[0]=accX;
                            accl[1]=accY;
                        }
                        break;
                    }
                }
            }
        }
        
        // Calculate and set updated state value
        double gamma = .01;
        // The surrounding values shoot down to 1
        System.out.println(currState[4]);
        int[] newState = {currState[0],currState[1],currState[2],currState[3], (int) (currState[4]*gamma*maxState[4])+1};
        values.set(values.indexOf(currState),newState);
        //System.out.println("next value: " + newState[4]);
        
        return accl;
    }
    
    public boolean matches(RaceCar car, int[] state){
        if (state[0]==car.getXPos() && state[1]==car.getYPos() && state[2]==car.getXVel() && state[3]==car.getYVel())
            return true;
        return false;
    }
    
    public String toString(){
        return "Value Iteration " + super.toString();
    }
    
    //initialize each state to a random value, except the final and initial state
    public ArrayList<int[]> initializeStateValues(){
        ArrayList<int[]> values = new ArrayList<int[]>();
        char[][] track = t.getCopyOfTrack();
        int length = track.length;
        int width = track[0].length;
        //int[] temp = {0,0,0,0,0};
        for(int x=0;x<length*width; x++){
            if(t.cellSafe(x%length,x/length)){
                if(track[x%length][x/length]=='F'){
                    //for each possible velocity
                    for(int velX=0; velX<121; velX++){
                        int[] temp = {x%length,x/length,velX%11-5,velX/11-5, 100};
                        values.add(temp);
                    }
                }
                if(track[x%length][x/length]=='S'){
                	for(int velX=0; velX<121; velX++){
	                    int[] temp = {x%length,x/length,velX%11-5,velX/11-5, 1};
	                    values.add(temp);
                	}
                }
                for(int velX=0; velX<121; velX++){
                    int[] temp = {x%length,x/length,velX%11-5,velX/11-5, random.nextInt(100)+1};
                    values.add(temp);
                }
            }
        }
        return values;     
    }
    public static void printValues(){
    	
    }
}

/*
The first algorithm we will look at is value iteration. The essential idea behind value iteration
is this: if we knew the true value of each state, our decision would
be simple: always choose the action that maximizes expected utility. But we dont itinially
know a states true value; we only know its immediate reward. But, for example,
a state might have low initial reward but be on the path to a high-reward state.
The true value (or utility) of a state is the immediate reward for that state, plus the expected
discounted reward if the agent acted optimally from that point on:
U(s)=R(s)+maxas U(s)
Note that the value for each state can potentially depend on all of its neighbors values. If our
state space is acyclic, we can use dynamic programming to solve this. Otherwise, we use value iteration.

1. Assign each state a random value
2. For each state, calculate its new U based on its neighbors utilities.
3. Update each states U based on the calculation above: Ui+1(s)=R(s)maxs U(s)
4. if no value changes by more than, halt.
This algorithm is guaranteed to converge to the optimal (within ) solutions.
*/
