package reinforcementlearning;

import java.util.*;

/**
 *
 * @author Lizzie Herman
 */
public class ReinforcementLearning {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //loop through different tracks
        char[] letters = {'L', 'O', 'R'};
        //char letter = 'R';
        int i = 1;
        boolean notStart = true;
        for(char letter : letters){
            while(i <= 10){
                String filename;
                switch(letter){
                    case 'L':
                        filename = "L-track.txt";
                        System.out.println(filename);
                        break;
                    case 'O':
                        filename = "O-track.txt";
                        System.out.println(filename);
                        break;
                    case 'R':
                        filename = "R-track.txt";
                        System.out.println(filename);
                        break;
                    default:
                        filename = "";
                }
                RaceTrack track = new RaceTrack(filename);
                // create gui to show what is happening
                TrackGUI gui = new TrackGUI(track.getCopyOfTrack(), letter);
                gui.pack();
                gui.setVisible(true);
                boolean safe = true;
                int x = 0,y = 0;
                Random random = new Random();
                int pos[];
                // choose which starting position you want the car to have
                switch("start"){
                    case "start":
                        ArrayList<int[]> start = track.getStart();
                        int j = random.nextInt(start.size());
                        pos = start.get(j);
                        x = pos[0]; y = pos[1];
                        break;
                    case "final":                       
                        pos = track.getNextStepBackFromFinish(i);
                        x = pos[0]; y = pos[1];
                        //System.out.println(i);
                        notStart = (track.getCell(x, y) != 'S');
                        i++;
                        break;
                    default:
                        int[] size = track.getSize();
                        do{
                            x = random.nextInt(size[1]);
                            y = size[0] - (1 + random.nextInt(size[0]));
                            safe = track.cellSafe(x, y);
                        } while(! safe);
                        break;
                }
                gui.updateTrack(x, y, 0, 0, 0, 0);

                RaceCar car = new RaceCar(x,y);
                Algorithm algorithm;
                // choose which algorithm to run
                switch('v'){
                    case 'v':
                        algorithm = new ValueIteration(track,car,gui);
                        break;
                    case 's':
                        algorithm = new SARSA(track,car,gui,0.2);
                        break;
                    default:
                        algorithm = new Algorithm(track,car,gui);
                        break;
                }
                // choose which crash action happens
                // true sends car to original position
                // false sends car to the closest track position from crash site
                boolean crash = true;

                // run algorithm
                algorithm.runCar(crash);

                gui.updateTrack(car.getXPos(), car.getYPos(), car.getXVel(), car.getYVel(), algorithm.getTime(), algorithm.getCost());
                System.out.println(algorithm.toString());
                gui.dispose();
                i++;
            }
            i = 1;
        }
    }
}
