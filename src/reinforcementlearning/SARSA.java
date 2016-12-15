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
	private RaceTrack track;
	private double prob, alpha, gamma;
	private double[] tempQ;
	private int steps, a, a1;
	private int[] nextCell;
	private String key;

	public SARSA(RaceTrack t, RaceCar c, TrackGUI g, double p) {
		super(t, c, g);
		prob = p;
		this.steps = 1;
		this.car = c;
		this.track = t;
		this.board = new GridSquare[t.getSize()[0]][t.getSize()[1]];
		this.nextCell = new int[]{this.car.getXPos(), this.car.getYPos()};
		this.tempQ = new double[4];
		
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
		//this.alpha = 1/this.steps;  //Decay the learning rate
		this.car.setVel(super.car.getXVel(), super.car.getYVel());
		this.car.setPos(super.car.getXPos(), super.car.getYPos());
		//System.out.println("\n SARSA position: (" + this.car.getXPos() + "," + this.car.getYPos() + ")");
		//System.out.println("Reward for start state: " + s.reward);
		
		//Check if action changed velocity
		if(nextCell[0] == this.car.getXPos() && nextCell[1] == this.car.getYPos()){  //Car moved where it was supposed to go
			this.car.updateFacing(this.car.getXVel(), this.car.getYVel());//Update facing
		}else if(this.car.getReset()){  //Car crashed, reset the facing
			this.car.startFacing(this.track.getName());
			this.car.setReset(false);
			this.s = board[this.car.getXPos()][this.car.getYPos()];  //Update state to current location
			this.a = chooseAction(s);  //Choose an action for the new state
		}else{  //Acceleration did not happen from previous loop
			if(this.steps-1 != 0){
				this.alpha = 1/(this.steps-1);  //Restore the learning rate to previous value
			}
			this.s.velTable.put(this.key, this.tempQ);  //Reset the calculated q-value since it is not accurate for the resulting position
			this.s = board[this.car.getXPos()][this.car.getYPos()];  //Update state to current location
			this.a = chooseAction(s);  //Choose an action for the new state
			//System.out.println("Choosing action: " + this.a);
		}
		
		int[] accel = findAccel(a); //Take action a
		
		//Check to make sure car ends up in bounds of the grid array
		int[] limit = this.track.checkBounds(accel, this.car.getXPos(), this.car.getYPos(), this.car.getXVel(), this.car.getYVel());
		if(limit[0] == car.getXPos()){
			if(limit[1] == car.getYPos()){
				int action = this.a;
				if(this.car.getXPos() + accel[0] > this.track.getSize()[0] || this.car.getXPos() + accel[0] < 0){
					while(this.a == action){
						this.a = chooseAction(s);
					}
					this.s1 = this.board[this.car.getXPos() + accel[0]][this.car.getYPos() + accel[1]]; //Set up next state
				}else if(this.car.getYPos() + accel[1] > this.track.getSize()[1] || this.car.getYPos() + accel[1] < 0){
					while(this.a == action){
						this.a = chooseAction(s);
					}
					this.s1 = this.board[this.car.getXPos() + accel[0]][this.car.getYPos() + accel[1]]; //Set up next state
				}else{
					this.s1 = this.board[this.car.getXPos() + accel[0]][this.car.getYPos() + accel[1]]; //Set up next state
				}
				this.nextCell[0] = this.car.getXPos() + this.car.getXVel() + accel[0];  //Keep track of where the car should end up if acceleration is applied
				this.nextCell[1] = this.car.getYPos() + this.car.getYVel() + accel[1];
			}
		}else{
			this.s1 = this.board[limit[0]][limit[1]]; //Set up next state, car at limit of grid
			this.car.setPos(limit[0] - accel[0], limit[1] - accel[1]);
			super.car.setPos(limit[0] - accel[0], limit[1] - accel[1]);
			this.nextCell[0] = limit[0];  //Keep track of where the car should end up if acceleration is applied
			this.nextCell[1] = limit[1];
		}
		
//		this.nextCell[0] = this.car.getXPos() + this.car.getXVel() + accel[0];  //Keep track of where the car should end up if acceleration is applied
//		this.nextCell[1] = this.car.getYPos() + this.car.getYVel() + accel[1];
//		this.nextCell[0] = limit[0];  //Keep track of where the car should end up if acceleration is applied
//		this.nextCell[1] = limit[1];
		
		//System.out.println("Recommended acceleration: (" + accel[0] + "," + accel[1] + ")");
		//System.out.println("Next cell should be (" + this.nextCell[0] + "," + this.nextCell[1] + ")");
		double reward = s1.reward; //Get reward from the next state
		System.out.println("Reward for next state: " + reward);
		
		this.a1 = chooseAction(s1); //Determine which action to take
		//System.out.println("Choosing action at next state: " + this.a1);
		this.key = this.car.getXVel() + "/" + this.car.getYVel();  //Get key for the hashtable
		
		//Update equation for new Q(s,a) value, store in the action array
		this.tempQ = this.s.velTable.get(this.key);  //Store the previous action q-value list, in case acceleration does not happen
		
		//Q(s,a) = Q(s,a) + alpha*(reward(s1) + gamma*Q(s1,a1) - Q(s,a))
		//double newQ = s.velTable.get(this.key)[a] + this.alpha*(reward + (this.gamma*s1.highestQ(this.car.getXVel(), this.car.getYVel())[1]) - s.velTable.get(this.key)[a]);
		int newXVel = this.car.getXVel() + accel[0];
		int newYVel = this.car.getXVel() + accel[0];
		
		String nextKey = newXVel + "/" + newYVel;
		
		//System.out.println("TEST: " + s.velTable.get(this.key)[a] + " + " + (this.gamma*(reward + (this.gamma*s1.velTable.get(nextKey)[a1]) - s.velTable.get(this.key)[a])));
		//double newQ = s.velTable.get(this.key)[a] + this.alpha*(reward + (this.gamma*s1.velTable.get(nextKey)[a1]) - s.velTable.get(this.key)[a]);
		//System.out.println("EQUATION: " + s.velTable.get(this.key)[a] + this.alpha*(reward + (this.gamma*s1.velTable.get(nextKey)[a1]) - s.velTable.get(this.key)[a]));
		//System.out.println("newQ=" + newQ);
		
		double part1 = s.velTable.get(this.key)[a];
		double part2 = (this.gamma*(reward + (this.gamma*s1.velTable.get(nextKey)[a1]) - s.velTable.get(this.key)[a]));
		part1 = part1 + part2;
		//System.out.println(part1);
		double[] updateActionQ = this.tempQ;
		updateActionQ[a] = part1;
		this.s.velTable.put(this.key, updateActionQ);  //Store the new q-value in the action list
		//System.out.println("Velocity: (" + this.car.getXVel() + "," + this.car.getYVel() + ")");
		System.out.println("Updated Q-value for action at initial state: " + part1);
		
		s = s1; //Set s to next state
		a = a1; //Set a to next action
		
		this.steps++;
		//System.out.println("step count: " + this.steps);
		return accel;
	}
	
	//Determine which action to take, Epsilon greedy exploration
	public int chooseAction(GridSquare s){
		int a;
		double random = Math.random();
		
		//Epsilon Greedy
		if(random <= this.prob){
			a = (int) (Math.random()*4); //Pick a random action
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
				accel = new int[]{1,0};
			}else if(index == 1){ //Accel forward
				accel = new int[]{0,-1};
			}else if(index == 2){ //Accel right
				accel = new int[]{-1,0};
			}else{ //Accel backward
				accel = new int[]{0,1};
			}
		}else if(this.car.getFacing() == 1){  //Facing up
			if(index == 0){ //Accel left
				accel = new int[]{0,-1};
			}else if(index == 1){ //Accel forward
				accel = new int[]{-1,0};
			}else if(index == 2){ //Accel right
				accel = new int[]{0,1};
			}else{ //Accel backward
				accel = new int[]{1,0};
			}
		}else if(this.car.getFacing() == 2){  //Facing right
			if(index == 0){ //Accel left
				accel = new int[]{-1,0};
			}else if(index == 1){ //Accel forward
				accel = new int[]{0,1};
			}else if(index == 2){ //Accel right
				accel = new int[]{1,0};
			}else{ //Accel backward
				accel = new int[]{0,-1};
			}
		}else{  //Facing down
			if(index == 0){ //Accel left
				accel = new int[]{0,1};
			}else if(index == 1){ //Accel forward
				accel = new int[]{1,0};
			}else if(index == 2){ //Accel right
				accel = new int[]{0,-1};
			}else{ //Accel backward
				accel = new int[]{-1,0};
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
				this.reward = -10;
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
				index = (int) (Math.random()*4); //Pick an action if all q-values are the same
			}
			
			double[] values = new double[]{index, highestQ};
			return values;
		}
		
	}
}
