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
    
    public double[] findNextMove(){
        double explore = random.nextDouble();
        if(explore < prob){
            return super.findNextMove();
        }
        double[] accl = {0,0};
        
       // TO-DO implement algorithm
          // TO-DO implement algorithm
         //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
        //Q is the value for a state-action, updated by error
        //Q values represent the possible reward received in the next time step for taking action a in state s
        //Alpha  is the adjusted learning rate
        //gamma is the discounted future reward received from the next state-action observation.
        //
        //ALPHA - The learning rate determines to what extent the newly acquired information will override the old information. A factor of 0 will make the agent not learn anything, while a factor of 1 would make the agent consider only the most recent information.
        //GAMMA - The discount factor determines the importance of future rewards. A factor of 0 will make the agent "opportunistic" by only considering current rewards, while a factor approaching 1 will make it strive for a long-term high reward. If the discount factor meets or exceeds 1, the {\displaystyle Q} Q values may diverge.
        //LOOK AT THE CODE ATTACHED TO THIS FUCKING WEB PAGE IF IT STILL ISN'T MAKING SENSE: https://studywolf.wordpress.com/2013/07/01/reinforcement-learning-sarsa-vs-q-learning/
        // OR EXAMPLE CODE HERE: https://github.com/studywolf/blog/tree/master/RL/SARSA%20vs%20Qlearn%20cliff
        
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
