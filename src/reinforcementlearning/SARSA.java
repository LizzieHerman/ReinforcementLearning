package reinforcementlearning;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * 
 * @author Lizzie Herman, Ryan Freivalds
 */
public class SARSA extends Algorithm {
	private GridSquare[][] board;  //Holds the GridSquares, same size as the track
	private GridSquare s, s1;
	private RaceCar car;
	private double prob, alpha, gamma, tempQ;
	private int steps, a, a1;
	private int[] nextCell;
	private String key;

	public SARSA(RaceTrack t, RaceCar c, TrackGUI g, double p) {
		super(t, c, g);
		prob = p;
		this.steps = 1;
		this.car = c;
		this.board = new GridSquare[t.getSize()[0]][t.getSize()[1]];
		this.nextCell = new int[2];
		
		//Set up the grid to hold q values
		for(int i = 0; i < t.getSize()[0]; i++){
			for(int j = 0; j < t.getSize()[1]; j++){
				board[i][j] = new GridSquare(t.getSymbol(i,j));
			}
		}
		
		this.car.startFacing(t.getName());
		this.alpha = 1;  //Learning rate will decay as steps increases
		this.gamma = p;
		this.s = board[c.getXPos()][c.getYPos()];  //Set starting point
		this.a = chooseAction(s);  //Choose action a
	}

//	public int[] findNextMove(){
//        double explore = random.nextDouble();
//        if (explore < prob){
//        	return super.findNextMove();
//        }
//        int[] accl = {0, 0};
//        //****************
//        //***READ THIS:***
//        //****************
//        //Alright, this is what's going on. Q(s,a) is reward associated with an action state. 
//        //Since there's so many actions that can take us to/from a state we're going to need to store these in an ever-expanding array list.
//        //When the agent lands in a space, they search the array list to see if this is a situation it's been in before, if it's current Q(s,a) has a value.
//        //If it does, the agent can see any rewards associated with actions it took in this state. It will avoid actions that yeilded a negative reward (crashing).
//        //It will look for actions that yeled a positive reward (took it closer to the goal) and try to take those, but can still move randomly to explore, which will make more arraylist entires.
//        //PROBLEMS:
//        //when we start our agent knows NOTHING. How do we give the agent the "Right" reward for a move here?
//        //If our reward is negative, our agent will explore like CRAZY avoiding any path it's taken before whenever possible.
//        //If our reward is positive it would be easy for our agent to simply fall into an infinte loop of safe moves.
//        //--Is agent training supposed to solve this? To make a path of waypoints for the agent to follow to the goal?
//        //Agent training is very ineffeicent, requiring the agent to land on a particular state (square) with a particular action (current X and Y accel) 
//        //to point it in the right direction. But what are the odds our agent will ever land on an exact square with that exact action?
//        //I think this is where running it many, many times starting it close to the goal and moving it farther and farther away comes into play,
//        // to fill in the arrayList with "good(as well as bad)" moves as we get closer to the goal so our agent "knows where to go"
//
//        // TO-DO implement algorithm
//        // Equasion details:
//        //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
//        //Q is the value for a state-action, updated by error
//        //Q values represent the possible reward received in the next time step for taking action a in state s
//        //Alpha  is the adjusted learning rate
//        //gamma is the discounted future reward received from the next state-action observation.
//        //
//        //ALPHA - The learning rate determines to what extent the newly acquired information will override the old information. A factor of 0 will make the agent not learn anything, while a factor of 1 would make the agent consider only the most recent information.
//        //GAMMA - The discount factor determines the importance of future rewards. A factor of 0 will make the agent "opportunistic" by only considering current rewards, while a factor approaching 1 will make it strive for a long-term high reward. If the discount factor meets or exceeds 1, the {\displaystyle Q} Q values may diverge.
//        //REALLY GOOD EXPLINATION HERE: http://stackoverflow.com/questions/29879172/sarsa-implementation
//        //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
//        //r{{t+1}} == ??
//        int newXaccel = 0;
//        int newYaccel = 0;
//
//        ArrayList stateAction = new ArrayList(); //needs to be an array of states, that hold arrays of actions?
//        //double OldQ = 0;
//        double newQ = 0;
//        int defaultReward = -1; //Reward needs to be a function, or atleast vary, depending on what happens
//        double epsilon = 0.1;
//        double alpha = 0.2;
//        double gamma = 0.9;
//        //in "choose action" method q = [self.getQ(state, a) for a in self.actions]
//        //int[][] Q = {state, action};
//
//        /*
//        My current stupid-ass non-working SARSA code may need to simply "return accel"
//        when the code indicates to move, and update the reward for that state-action 
//        at the START of the next time it's method is called 
//        to work with the foundation of code we already have.
//        */
//            
//        //""real"" code starts here-
//        //UPDATE LAST STATE-ACTION RESULT HERE instead of farther down in the code?
//        //Car lands in an action-state:
//        ArrayList state = new ArrayList();
//        ArrayList action = new ArrayList();
//        int[] currentState = {car.getXPos(), car.getYPos()}; //state is in XPos, Ypos, 
//        int[] currentAction = {car.getXVel(), car.getYVel()}; //action is in Xvel, Yvel format.
//        //Have we seen this action-state before?
//        if (stateAction.contains(currentState)) { //if we've been on this square before
//            if (state.contains(currentAction)) { //and we had the same accelerations values now as when when we here before
//                for (int i = 0; i < action.get(currentAction).length; i++) { //for every action we recorded we took while having these accelerations 
//                    if (action.get(currentAction)[i] > defaultReward ){ //if there was a "good" move
//                        //do best move
//                    }
//                }
//            }else{ //if we've been on this square, but have never had these current accelerations 
//                //explore (move randomly) and return the reward for doing so.
//                state.add(currentAction); //add our CURRENT accelerations to our list of accelerations under our list holding the current state.
//                //"move" goes here.
//                state.get(currentAction) == Qcalculation)=(); //save the reward for our move to that particular state-action.
//            }
//        }
//        else //We've NEVER been to this square before
//        {
//            stateAction.add(state); //add it to our list of square's we've visited
//            stateAction.get(state).add(currentAction); //and add this particular set of CURRENT accelerations to that square's list
//            //"move" randomly to explore.
//            stateAction.get(state).get(currentAction) == Qcalculation(); //save the reward for our move to that particular state-action.
//        }
//        //repeat until goal?
//        //end of code logic framework
//
//        
//        
//        //example "translated" code for "learning" Q
//        if (OldQ ==   {null,null}   || OldQ == {null,null}){
//            newQVal = reward; //this code is to the effect of "if we've never seen this before, go with the default reward"
//        }else{
//        	newQ = OldQ + alpha*(Q - OldQ);
//        	Q = reward(newAction) + gamma * Q[(newAction, newState)]; //r{{t+1}} == ??
//        }
//
//        return accl;
//        
//    }

	public int Qcalculation() { // least sure about this stuff
		int reward = -1; // default cost of a move
		// if( that would would cause a crash){
		reward = -1000;
		// }
		// if(car would hit finishline){
		reward = 10000;
		// }
		// //Q(s{t},a{t}) leftarrow Q(s{t},a{t})+ alpha [r{{t+1}}+ gamma
		// Q(s{{t+1}},a{{t+1}})-Q(s{t},a{t})]
		return reward;
	}
	
	//Find move and update q-value for appropriate state-action pair
	public int[] findNextMove(){
		this.alpha = 1/this.steps;  //Decay the learning rate
		
		//Check if action changed velocity
		if(nextCell[0] == this.car.getXPos() && nextCell[1] == this.car.getYPos()){
			this.car.updateFacing(this.car.getXVel(), this.car.getYVel());//Update facing
			this.s.velTable.get(this.key)[a] = tempQ;  //Store the calculated q-value
			this.s = board[this.car.getXPos()][this.car.getYPos()];  //Update state to current location
			this.a = chooseAction(s);
		}else{
			if(this.steps-1 != 0){
				this.alpha = 1/(this.steps-1);  //Decay the learning rate
			}
		}
		
		int[] accel = findAccel(a); //Take action a
		this.s1 = this.board[this.car.getXPos() + accel[0]][this.car.getYPos() + accel[1]]; //Set up next state
		this.nextCell[0] = this.car.getXPos() + accel[0];
		this.nextCell[1] = this.car.getYPos() + accel[1];
		int reward = s1.reward; //Get reward from the next state
		
		this.a1 = chooseAction(s1); //Determine which action to take
		this.key = this.car.getXVel() + "/" + this.car.getYVel();
		
		//Update equation for new Q(s,a) value
		this.tempQ = this.s.velTable.get(this.key)[a];
		this.s.velTable.get(this.key)[a] = s.velTable.get(this.key)[a] + this.alpha*(reward + (this.gamma*s1.highestQ(this.car.getXVel(), this.car.getYVel())[1]) - s.velTable.get(this.key)[a]);
		
		s = s1; //Set s to next state
		a = a1; //Set a to next action
		
		this.steps++;
		return accel;
	}
	
	//Determine which action to take, Epsilon greedy exploration
	public int chooseAction(GridSquare s){
		int a;
		double random = Math.random();
		
		//Epsilon Greedy
		if(random <= this.prob){
			a = (int) Math.random()*4; //Pick a random action
		}else{
			double[] result = s.highestQ(this.car.getXVel(), this.car.getYVel()); //Pick the best action by finding highest q-value
			a = (int) result[0];
		}
		return a;
	}
	
	public int[] findAccel(int index){
		int[] accel;
		
		//check facing  //accel
		if(this.car.getFacing() == 0){  //Facing left
			if(index == 0){ //Accel left
				accel = new int[]{0,-1};
			}else if(index == 1){ //Accel forward
				accel = new int[]{-1,0};
			}else if(index == 2){ //Accel right
				accel = new int[]{0,1};
			}else{ //Accel backward
				accel = new int[]{1,0};
			}
		}else if(this.car.getFacing() == 1){
			if(index == 0){ //Accel left
				accel = new int[]{-1,0};
			}else if(index == 1){ //Accel forward
				accel = new int[]{0,1};
			}else if(index == 2){ //Accel right
				accel = new int[]{1,0};
			}else{ //Accel backward
				accel = new int[]{0,-1};
			}
		}else if(this.car.getFacing() == 2){
			if(index == 0){ //Accel left
				accel = new int[]{0,1};
			}else if(index == 1){ //Accel forward
				accel = new int[]{1,0};
			}else if(index == 2){ //Accel right
				accel = new int[]{0,-1};
			}else{ //Accel backward
				accel = new int[]{-1,0};
			}
		}else{
			if(index == 0){ //Accel left
				accel = new int[]{1,0};
			}else if(index == 1){ //Accel forward
				accel = new int[]{0,-1};
			}else if(index == 2){ //Accel right
				accel = new int[]{-1,0};
			}else{ //Accel backward
				accel = new int[]{0,1};
			}
		}
		
		this.car.setAccel(accel);
		return accel;
	}

	public String toString() {
		return "SARSA " + super.toString();
	}
	
	private class GridSquare{
		private int reward;
		private Hashtable<String, double[]> velTable;
		
		public GridSquare(char trackSymbol){
			velTable = new Hashtable<String, double[]>();
			//Create a hash table for all state action pairs, state is grid location and velocity value, action is the acceleration
			//The velocity value is the key to the hash table, the value is an array holding q-values for each action, accel left/right/up/down
			for(int i = -5; i < 6; i++){
				for(int j = -5; j < 6; j++){
					String key = i + "/" + j;
					velTable.put(key, new double[]{0.0,0.0,0.0,0.0});
				}
			}
			
			//Set up the reward value for the grid square
			if(trackSymbol == 'S'){
				this.reward = 0;
			}else if(trackSymbol == 'F'){
				this.reward = 1000;
			}else if(trackSymbol == '#'){
				this.reward = -1000;
			}else{
				this.reward = -1;
			}
		}
		
		//Return the highest q value for the given state
		public double[] highestQ(int xvel, int yvel){
			int index = 0;
			double highestQ = 0.0;
			boolean equalQ = true;
			String key = xvel + "/" + yvel;
			double[] actionList = velTable.get(key);
			
			for(int i = 0; i < actionList.length; i++){
				if(actionList[i] > highestQ){
					highestQ = actionList[i];
					index = i;
					equalQ = false; //Q-values are not all the same
				}
			}
			
			if(equalQ){
				index = (int) Math.random()*4; //Pick an action if all q-values are the same
			}
			
			double[] values = new double[]{index, highestQ};
			return values;
		}
		
	}
	
}
