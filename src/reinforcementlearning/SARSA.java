package reinforcementlearning;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Lizzie Herman, Ryan Freivalds
 */
public class SARSA extends Algorithm {

    private double prob;

    public SARSA(RaceTrack t, RaceCar c, TrackGUI g, double p) {
        super(t, c, g);
        prob = p;
    }

    public int[] findNextMove() {
        double explore = random.nextDouble();
        if (explore < prob) {
            return super.findNextMove();
        }
        int[] accl = {0, 0};
        //****************
        //***READ THIS:***
        //****************
        //Alright, this is what's going on. Q(s,a) is reward associated with an action state. 
        //Since there's so many actions that can take us to/from a state we're going to need to store these in an ever-expanding array list.
        //When the agent lands in a space, they search the array list to see if this is a situation it's been in before, if it's current Q(s,a) has a value.
        //If it does, the agent can see any rewards associated with actions it took in this state. It will avoid actions that yeilded a negative reward (crashing).
        //It will look for actions that yeled a positive reward (took it closer to the goal) and try to take those, but can still move randomly to explore, which will make more arraylist entires.
        //PROBLEMS:
        //when we start our agent knows NOTHING. How do we give the agent the "Right" reward for a move here?
        //If our reward is negative, our agent will explore like CRAZY avoiding any path it's taken before whenever possible.
        //If our reward is positive it would be easy for our agent to simply fall into an infinte loop of safe moves.
        //--Is agent training supposed to solve this? To make a path of waypoints for the agent to follow to the goal?
        //Agent training is very ineffeicent, requiring the agent to land on a particular state (square) with a particular action (current X and Y accel) 
        //to point it in the right direction. But what are the odds our agent will ever land on an exact square with that exact action?
        //I think this is where running it many, many times starting it close to the goal and moving it farther and farther away comes into play,
        // to fill in the arrayList with "good(as well as bad)" moves as we get closer to the goal so our agent "knows where to go"

        // TO-DO implement algorithm
        // Equasion details:
        //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
        //Q is the value for a state-action, updated by error
        //Q values represent the possible reward received in the next time step for taking action a in state s
        //Alpha  is the adjusted learning rate
        //gamma is the discounted future reward received from the next state-action observation.
        //
        //ALPHA - The learning rate determines to what extent the newly acquired information will override the old information. A factor of 0 will make the agent not learn anything, while a factor of 1 would make the agent consider only the most recent information.
        //GAMMA - The discount factor determines the importance of future rewards. A factor of 0 will make the agent "opportunistic" by only considering current rewards, while a factor approaching 1 will make it strive for a long-term high reward. If the discount factor meets or exceeds 1, the {\displaystyle Q} Q values may diverge.
        //REALLY GOOD EXPLINATION HERE: http://stackoverflow.com/questions/29879172/sarsa-implementation
        //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
        //r{{t+1}} == ??
        int newXaccel = 0;
        int newYaccel = 0;

        ArrayList stateAction = new ArrayList(); //needs to be an array of states, that hold arrays of actions?
        //double OldQ = 0;
        double newQ = 0;
        int defaultReward = -1; //Reward needs to be a function, or atleast vary, depending on what happens
        double epsilon = 0.1;
        double alpha = 0.2;
        double gamma = 0.9;
        //in "choose action" method q = [self.getQ(state, a) for a in self.actions]
        //int[][] Q = {state, action};

        /*
        My current stupid-ass non-working SARSA code may need to simply "return accel"
        when the code indicates to move, and update the reward for that state-action 
        at the START of the next time it's method is called 
        to work with the foundation of code we already have.
        */
            
        //""real"" code starts here-
        //UPDATE LAST STATE-ACTION RESULT HERE instead of farther down in the code?
        //Car lands in an action-state:
        ArrayList state = new ArrayList();
        ArrayList action = new ArrayList();
        int[] currentState = {car.getXPos(), car.getYPos()}; //state is in XPos, Ypos, 
        int[] currentAction = {car.getXVel(), car.getYVel()}; //action is in Xvel, Yvel format.
        //Have we seen this action-state before?
        if (stateAction.contains(currentState)) { //if we've been on this square before
            if (state.contains(currentAction)) { //and we had the same accelerations values now as when when we here before
                for (i = 0; i < action.get(currentAction).length; i++) { //for every action we recorded we took while having these accelerations 
                    if (action.get(currentAction)[i] > defaultReward ){ //if there was a "good" move
                        //do best move
                    }
                }
            }
            else //if we've been on this square, but have never had these current accelerations 
            {
                //explore (move randomly) and return the reward for doing so.
                state.add(currentAction); //add our CURRENT accelerations to our list of accelerations under our list holding the current state.
                //"move" goes here.
                state.get(currentAction) == Qcalculation)=(); //save the reward for our move to that particular state-action.
            }
        }
        else //We've NEVER been to this square before
        {
            stateAction.add(state); //add it to our list of square's we've visited
            stateAction.get(state).add(currentAction); //and add this particular set of CURRENT accelerations to that square's list
            //"move" randomly to explore.
            stateAction.get(state).get(currentAction) == Qcalculation(); //save the reward for our move to that particular state-action.
        }
        //repeat until goal?
        //end of code logic framework
+
        
        
        //example "translated" code for "learning" Q
        if (OldQ ==   {null
        ,null}   || OldQ == {null
        ,null})
        {
            newQVal = reward; //this code is to the effect of "if we've never seen this before, go with the default reward"
        }
        else
        newQ = OldQ + alpha*(Q - OldQ);
        Q = reward(newAction) + gamma * Q[(newAction, newState)]; //r{{t+1}} == ??

        return accl;
        
    }

    public int Qcalculation() { //least sure about this stuff
        int reward = -1; //default cost of a move
        // if( that would would cause a crash){
        reward = -1000;
        // }
        //  if(car would hit finishline){
        reward = 10000;
        // }
        ////Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
        return reward;
    }

    public String toString() {
        return "SARSA " + super.toString();
    }
}
