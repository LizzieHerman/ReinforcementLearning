package reinforcementlearning;

import java.util.ArrayList;

/**
 *
 * @author Lizzie Herman
 * Connor O'Leary
 */
public class ValueIteration extends Algorithm{
    Random random = new Random();
    ArrayList<int[]> values;

    public ValueIteration(RaceTrack t, RaceCar c, TrackGUI g) {
        super(t, c, g);
        values = initializeStateValues();
    }
    
    public int[] findNextMove(){
        int[] accl = {0,0};
        int[] carState = {c.getXPos(),c.getYPos(),c.getXVel(),c.getYVel()}
        int[] state, nextState;
        for(state:values){
            if(matches(state))
                break;
        }
        int currVal = state[4];
        RaceCar car = new RaceCar(c.getXPos(),c.getXPos());
        int[] maxState = {0,0,0,0,0};
        // Find each possible next state based on possible accelerations and select best acceleration
        for(int accX = -1; accX<2; accX++){
            for(int accY = -1; accY<2; accY++){
                
                car.setVel(c.getXVel()+accX,c.getYVel()+accY);
                car.setPos(c.getXVel()+accX+c.getXPos(),c.getYVel()+accY+c.getYPos());
                
                for(nextState:values){
                    if(matches(car, nextState)){
                        if(state[4]>maxState[4]){ // best next value
                            maxState=nextState;
                            accl={accX,accY);
                        }
                    break;
                    }
                }
            }
        }
        
        // Calculate and set updated state value
        int gamma = .5;
        int[] newState = {state.getXPos(),state.getYPos(),state.getXVel(),state.getYVel(), state[4]*gamma*maxState[4]}
        values.set(values.indexOf(state),newState);
        
        
        return accl;
    }
    
    public boolean matches(RaceCar car, int[] state){
        if (state[0]==car.getXPos() && state[1]==car.getYPos() && state[2]==car.getXVel() && state[3]==car.getYVel()){
            return true;
        return false;
    }
    
    public String toString(){
        return "Value Iteration " + super.toString();
    }
    
    //initialize each state to a random value, except the final and initial state
    public ArrayList<Integer> initializeStateValues(){
        ArrayList<int[]> values = new ArrayList<int[]>();
        int length = t.track.length;
        int width = t.track[0].length;
        int[] temp = {0,0,0,0,0};
        for(int x=0;x<length*width){
            if(t.safeCell(x%length,x/length)){
                if(t.track[x%length][x/length]=='F'){
                    //for each possible velocity
                    for(int velX=0; velX<121; velX++){
                        temp = {x%length,x/length,velX%11-5,velX/11-5, 100};
                        values.add(temp);
                    }
                }
                if(t.track[x%length][x/length]=='S'){
                    temp = {x%length,x/length,velX%11-5,velX/11-5, random.nextInt(100)+1};
                    values.add(temp);
                }
                for(int velX=0; velX<121; velX++){
                    temp = {x%length,x/length,velX%11-5,velX/11-5, random.nextInt(100)+1};
                    values.add(temp);
                }
            }
        }
        return values;     
    }
}

/*
The first algorithm we will look at is value iteration. The essential idea behind value iteration
is this: if we knew the true value of each state, our decision would
be simple: always choose the action that maximizes expected utility. But we don’t itinially
know a state’s true value; we only know its immediate reward. But, for example,
a state might have low initial reward but be on the path to a high-reward state.
The true value (or utility) of a state is the immediate reward for that state, plus the expected
discounted reward if the agent acted optimally from that point on:
U(s)=R(s)+γmaxa􏰀s′ U(s′)
Note that the value for each state can potentially depend on all of its neighbors’ values. If our
state space is acyclic, we can use dynamic programming to solve this. Otherwise, we use value iteration.

1. Assign each state a random value
2. For each state, calculate its new U based on its neighbor’s utilities.
3. Update each state’s U based on the calculation above: Ui+1(s)=R(s)γmaxs′ U(s′)
4. if no value changes by more than δ, halt.
This algorithm is guaranteed to converge to the optimal (within δ) solutions.
*/
