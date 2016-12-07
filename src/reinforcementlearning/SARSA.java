package reinforcementlearning;

import java.util.Random;
/**
 *
 * @author Lizzie Herman, Ryan Freivalds
 */
public class SARSA extends Algorithm{
    private double prob;

    public SARSA(RaceTrack t, RaceCar c, TrackGUI g, double p) {
        super(t, c, g);
        prob = p;
    }
    
    public int[] findNextMove(){
        double explore = random.nextDouble();
        if(explore < prob){
            return super.findNextMove();
        }
        int[] accl = {0,0};
        //****************
        //***READ THIS:***
        //****************
        //Alright, this is what's going on. Q(s,a) is reward associated with an action state. 
        //Since there's so many actions that can take us too/from a state we're going to need to store these in an ever-expanding array list.
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
        int newXaccel = 0;
        int newYaccel = 0;
        int[] state = {car.getXPos(), car.getYPos(), car.getXVel(), car.getYVel() }; //state is in XPos, Ypos, Xvel, Yvel format.
        int[] newState = {-5000, -5000, -5000, -5000 };
        int[] action = {newXaccel, newYaccel};
        int[] newAction = {0, 0};
        //double OldQ = 0;
        double newQ = 0;
        int reward = -1; //NO IDEA. Should this be -1? The cost of any move but crossing the finish line? YES. //Needs to be a LARGE negative value for crashing? //how do we make it = the actual reward if it IS crossing the finishline?
        double epsilon = 0.1;
        double alpha=0.2;
        double gamma=0.9;
        //The sample code just has q as an empty array. Q = {}. WTF?
        //in "choose action" method q = [self.getQ(state, a) for a in self.actions]
        int[][] Q = {state, action};
        int qValue = -5000;
        //is the sample code using two different "q"s? A state-action and a value assocsiated with a state-action? I got no fucking clue
        //is it storing a value associated with that particular action-state combo?
        int[][] OldQ = {null,null};
        int OldQVal = -5000;
        int newQVal = -5000;
        //code for "learning" Q
        if (OldQ == {null,null} || OldQ == {null,null}) {
            newQVal = reward; 
        }
        else
        newQ = OldQ + alpha*(Q - OldQ);
        Q = reward(newAction) + gamma*Q[(newAction,newState)]; //r{{t+1}} == ??
        
        
        return accl;
    }

    public String toString(){
        return "SARSA " + super.toString();
    }
}
