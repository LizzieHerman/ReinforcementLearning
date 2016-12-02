package reinforcementlearning;

import java.util.Random;
/**
 *
 * @author Lizzie Herman
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
        
        return accl;
    }


}
