package reinforcementlearning;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Lizzie Herman Connor O'Leary
 */
public class ValueIteration extends Algorithm {
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

	public int[] findNextMove(RaceCar c) {
		int[] accl = { 0, 0 };
		int[] currState = { 0, 0, 0, 0, 0 };

		// match up the current state of the car with the state in the values
		// arraylist
		for (int[] state : values) {
			if (matches(c, state)) {
				currState = state;
				break;
			}
		}

		RaceCar car = new RaceCar(c.getXPos(), c.getYPos());
		int[] maxState = { 0, 0, 0, 0, 0 };

		// Find each possible next state based on possible accelerations and
		// select best acceleration
		for (int accX = -1; accX < 2; accX++) {
			for (int accY = -1; accY < 2; accY++) {

				car.setVel(c.getXVel() + accX, c.getYVel() + accY);
				car.setPos(c.getXVel() + accX + c.getXPos(), c.getYVel() + accY
						+ c.getYPos());

				// match up the next state of the car with the state in the
				// values arraylist
				for (int[] nextState : values) {
					if (matches(car, nextState)) {
						if (nextState[4] > maxState[4]
								&& track.notCrash(car.getXPos(), car.getYPos(),
										c.getXPos(), c.getYPos())) { // best
																		// next
																		// value
							car.setVel(c.getXVel() + accX, c.getYVel() + accY);
							car.setPos(c.getXVel() + accX + c.getXPos(),
									c.getYVel() + accY + c.getYPos());
							maxState = nextState;
							accl[0] = accX;
							accl[1] = accY;
						}
						// if the next state is the same value then still might
						// go to it
						if (nextState[4] == maxState[4]
								&& random.nextDouble() > .7) {
							car.setVel(c.getXVel() + accX, c.getYVel() + accY);
							car.setPos(c.getXVel() + accX + c.getXPos(),
									c.getYVel() + accY + c.getYPos());
							maxState = nextState;
							accl[0] = accX;
							accl[1] = accY;
						}
						break;
					}
				}
			}
		}

		// Calculate and set updated state value
		double gamma = .005;
		int[] newState = { currState[0], currState[1], currState[2],
				currState[3], (int) (currState[4] * gamma * maxState[4]) + 1 };
		if (values.indexOf(currState) != -1)
			values.set(values.indexOf(currState), newState);

		// allow the gui to move slower
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return accl;
	}

	// check to see if the state of the car matches with the state array
	public boolean matches(RaceCar car, int[] state) {
		if (state[0] == car.getXPos() && state[1] == car.getYPos()
				&& state[2] == car.getXVel() && state[3] == car.getYVel())
			return true;
		return false;
	}

	public String toString() {
		return "Value Iteration " + super.toString();
	}

	// initialize each possible state to a random value, except the final and
	// initial states
	public ArrayList<int[]> initializeStateValues() {
		ArrayList<int[]> values = new ArrayList<int[]>();
		char[][] track = t.getCopyOfTrack();
		int length = track.length;
		int width = track[0].length;
		for (int x = 0; x < length * width; x++) {
			if (track[x % length][x / length] == 'F') {

				// for each possible velocity
				for (int velX = 0; velX < 121; velX++) {
					int[] temp = { x / length, x % length, velX % 11 - 5,
							velX / 11 - 5, 1000 };
					values.add(temp);
				}
			}

			else if (track[x % length][x / length] == 'S') {
				for (int velX = 0; velX < 121; velX++) {
					int[] temp = { x / length, x % length, velX % 11 - 5,
							velX / 11 - 5, 1 };
					values.add(temp);
				}
			}
			else if (track[x % length][x / length] == '.') {
				for (int velX = 0; velX < 121; velX++) {
					int[] temp = { x / length, x % length, velX % 11 - 5,
							velX / 11 - 5, random.nextInt(100) };
					values.add(temp);
				}
			}
		}
		return values;
	}

}
