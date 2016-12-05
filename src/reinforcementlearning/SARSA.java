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
        /* EXAMPLE CODE
        def __init__(self, actions, epsilon=0.1, alpha=0.2, gamma=0.9):
          def getQ(self, state, action):
        return self.q.get((state, action), 0.0)
    def learnQ(self, state, action, reward, value):
        oldv = self.q.get((state, action), None)
        if oldv is None:
            self.q[(state, action)] = reward 
        else:
            self.q[(state, action)] = oldv + self.alpha * (value - oldv)
    def chooseAction(self, state):
        if random.random() < self.epsilon:
            action = random.choice(self.actions)
        else:
            q = [self.getQ(state, a) for a in self.actions]
            maxQ = max(q)
            count = q.count(maxQ)
            if count > 1:
                best = [i for i in range(len(self.actions)) if q[i] == maxQ]
                i = random.choice(best)
            else:
                i = q.index(maxQ)
            action = self.actions[i]
        return action
    def learn(self, state1, action1, reward, state2, action2):
        qnext = self.getQ(state2, action2)
        self.learnQ(state1, action1, reward, reward + self.gamma * qnext)
        */
        
        //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
        int newXaccel = 0;
        int newYaccel = 0;
        int[] state = {car.getXPos(), car.getYPos(), car.getXVel(), car.getYVel() }; //state is in XPos, Ypos, Xvel, Yvel format.
        int[] newState = {-5000, -5000, -5000, -5000 };
        int[] action = {newXaccel, newYaccel};
        int[] newAction = {0, 0};
        double OldQ = 0;
        double newQ = 0;
        double reward = 0.1; //is this the "epsilon" declared in the sample code?
        double alpha=0.2;
        double gamma=0.9;
        double Q = 0;
        
        if (OldQ == 0) {
            newQ[(state, action)] = reward; 
        }
        else
        newQ = OldQ + alpha*(Q - OldQ);
        Q = reward(newAction) + gamma*Q[(newAction,newState)];
        
        
        return accl;
    }

    public String toString(){
        return "SARSA " + super.toString();
    }
}
