package reinforcementlearning;

import java.util.ArrayList;

/**
 *
 * @author Lizzie Herman
 * Connor O'Leary
 */
public class ValueIteration extends Algorithm{
    Random random = new Random();

    public ValueIteration(RaceTrack t, RaceCar c, TrackGUI g) {
        super(t, c, g);
        ArrayList<int[]> values = initializeStateValues();
    }
    
    public int[] findNextMove(){
        int[] accl = {0,0};
        
        // TO-DO implement algorithm
        
        return accl;
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
U(s)=R(s)+γmaxa􏰀s′ T(s,a,s′)U(s′)
Note that the value for each state can potentially depend on all of its neigh- bors’ values. If our
state space is acyclic, we can use dynamic programming to solve this. Otherwise, we use value iteration.
3
1. Assign each state a random value
2. For each state, calculate its new U based on its neighbor’s utilities.
3. Update each state’s U based on the calculation above: Ui+1(s)=R(s)γmaxs′ U(s′)
4. if no value changes by more than δ, halt.
This algorithm is guaranteed to converge to the optimal (within δ) solutions.
*/
