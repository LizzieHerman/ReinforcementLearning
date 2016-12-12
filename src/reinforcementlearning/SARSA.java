package reinforcementlearning;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * 
 * @author Lizzie Herman, Ryan Freivalds, Greg Neznanski
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
		this.nextCell[0] = this.car.getXPos() + accel[0];  //Keep track of where the car should end up if acceleration is applied
		this.nextCell[1] = this.car.getYPos() + accel[1];
		int reward = s1.reward; //Get reward from the next state
		
		this.a1 = chooseAction(s1); //Determine which action to take
		this.key = this.car.getXVel() + "/" + this.car.getYVel();  //Get key for the hashtable
		
		//Update equation for new Q(s,a) value
		this.tempQ = this.s.velTable.get(this.key)[a];
		this.s.velTable.get(this.key)[a] = s.velTable.get(this.key)[a] + this.alpha*(reward + (this.gamma*s1.highestQ(this.car.getXVel(), this.car.getYVel())[1]) - s.velTable.get(this.key)[a]);  //Q(s,a) = Q(s,a) + alpha*(reward(s1) + gamma*Q(s1,a1) - Q(s,a))
		
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
		}else if(this.car.getFacing() == 1){  //Facing up
			if(index == 0){ //Accel left
				accel = new int[]{-1,0};
			}else if(index == 1){ //Accel forward
				accel = new int[]{0,1};
			}else if(index == 2){ //Accel right
				accel = new int[]{1,0};
			}else{ //Accel backward
				accel = new int[]{0,-1};
			}
		}else if(this.car.getFacing() == 2){  //Facing right
			if(index == 0){ //Accel left
				accel = new int[]{0,1};
			}else if(index == 1){ //Accel forward
				accel = new int[]{1,0};
			}else if(index == 2){ //Accel right
				accel = new int[]{0,-1};
			}else{ //Accel backward
				accel = new int[]{-1,0};
			}
		}else{  //Facing down
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
	
	//Inner class to hold information about each square in the race track grid
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
