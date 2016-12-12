package reinforcementlearning;

import java.io.*;
import java.util.*;

/**
 *
 * @author Lizzie Herman
 */
public class RaceTrack {
    private char track[][];
    private ArrayList<int[]> start = new ArrayList();
    private ArrayList<int[]> finish = new ArrayList();
    private char name;
    
    public RaceTrack(String filename){
        try {
            name = filename.charAt(0);
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = "";
            line = br.readLine();
            String[] dimensions = line.split(",");
            int length = Integer.parseInt(dimensions[0]);
            int width = Integer.parseInt(dimensions[1]);
            track = new char[length][width];
            for(int i = length-1; i >= 0; i--){
                line = br.readLine();
                track[i] = line.toCharArray();
            }
            for(int i = 0; i < track.length; i++){
                for(int j = 0; j < track[i].length; j++){
                    if(track[i][j] == 'S'){
                        int[] pos = {j,i};
                        start.add(pos);
                    }
                    if(track[i][j] == 'F'){
                        int[] pos = {j,i};
                        finish.add(pos);
                    }
                }
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public char[][] getCopyOfTrack(){
        char[][] copy = new char[track.length][track[0].length];
        for(int i = 0; i < track.length; i++){
            copy[i] = Arrays.copyOf(track[i], track[i].length);
        }
        return copy;
    }
    
    public boolean cellSafe(int x, int y){
        try {
            char cell = track[y][x];
            if(cell == '#'){
                return false;
            }
            return true;
        } catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public boolean notCrash(int x1, int y1, int x2, int y2){
        char cell;
        int changex = Math.abs(x1-x2);
        int changey = Math.abs(y1-y2);
        if(! cellSafe(x1,y1)){
            return false;
        }
        try {
            if(y1 == y2 && x1 == x2){
                return cellSafe(x1,y1);
            } else if(y1 == y2){
                if(x1 < x2){
                    for(int x = x1; x <= x2; x++){
                        cell = track[y1][x];
                        if(cell == '#') return false;
                    }
                } else {
                    for(int x = x2; x <= x1; x++){
                        cell = track[y1][x];
                        if(cell == '#') return false;
                    }
                }
            } else if(x1 == x2){
                if(y1 < y2){
                    for(int y = y1; y <= y2; y++){
                        cell = track[y][x1];
                        if(cell == '#') return false;
                    }
                } else {
                    for(int y = y2; y <= y1; y++){
                        cell = track[y][x1];
                        if(cell == '#') return false;
                    }
                }
            } else if(changex == 1 && changey == 1){
                int x, y;
                if(x1<x2) x = x1;
                else x = x2;
                if(y1<y2) y = y1;
                else y = y2;
                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < 2; j++){
                        if(! cellSafe(x+i,y+j)) return false;
                    }
                }
            } else if(changey > changex){
                double slope = changex / changey;
                int x,y;
                double xd;
                if(x1<x2){
                    x = x1;
                    xd = x1;
                }
                else{
                    x = x2;
                    xd = x2;
                }
                if(y1<y2) y = y1;
                else y = y2;
                for(int i = 0; i <= changey; i++){
                    xd += slope;
                    x = (int) xd;
                    if(i != 0 && (x-1) == (int)(xd-slope) && !((x-1) < x1 && (x-1) < x2)){
                        cell = track[y+i][x-1];
                        if(cell == '#') return false;
                    }
                    cell = track[y+i][x];
                    if(cell == '#') return false;
                    if(i != changey && (x+1) == (int)(xd+slope) && !((x+1) > x1 && (x+1) > x2)){
                        cell = track[y+i][x+1];
                        if(cell == '#') return false;
                    }
                }
            } else {
                double slope = changey / changex;
                int x,y;
                double yd;
                if(x1<x2) x = x1;
                else x = x2;
                if(y1<y2){
                    y = y1;
                    yd = y1;
                }
                else{
                    y = y2;
                    yd = y2;
                }
                for(int i = 0; i <= changex; i++){
                    yd += slope;
                    y = (int) yd;
                    if(i != 0 && (y-1) == (int)(yd-slope) && !((y-1) < y1 && (y-1) < y2)){
                        cell = track[y-1][x+i];
                        if(cell == '#') return false;
                    }
                    cell = track[y][x+i];
                    if(cell == '#') return false;
                    if(i != changex && (y+1) == (int)(yd+slope) && !((y+1) > y1 && (y+1) > y2)){
                        cell = track[y+1][x+i];
                        if(cell == '#') return false;
                    }
                }
            }
            return true;
        } catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public char getCell(int x, int y){
        try {
            char cell = track[y][x];
            switch(cell){
                case 'S':
                    //System.out.println("At the starting line");
                    break;
                case 'F':
                    System.out.println("At finish line");
                    break;
                case '.':
                    //System.out.println("Still on the track");
                    break;
                case '#':
                    System.out.println("Hit a wall");
                    break;
                default:
                    System.out.println("Unknown cell");
                    break;
            }
            return cell;
        } catch(IndexOutOfBoundsException e){
            System.out.println("Went outside the grid");
            return ' ';
        }
    }
    
    public int[] getSize(){
        int[] size = {track.length, track[0].length};
        return size;
    }
    
    public ArrayList<int[]> getStart(){
        return start;
    }
    
    public ArrayList<int[]> getFinish(){
        return finish;
    }
    
    public char getSymbol(int x, int y){
    	return track[x][y];
    }
    
    public char getName(){
    	return name;
    }
    
    
    
    public int[] getNextStepBackFromFinish(int steps){
        Random random = new Random();
        int num = random.nextInt(finish.size());
        //int num = 4;
        int[] pos = finish.get(num);
        switch(name){
            case 'L':
                return stepBackL(Arrays.copyOf(pos, 2),num,steps);
            case 'O':
                return stepBackO(Arrays.copyOf(pos, 2),num,steps);
            case 'R':
                return stepBackR(Arrays.copyOf(pos, 2),num,steps);
        }
        return pos;
    }
    
    private int[] stepBackL(int[] pos, int num, int steps){
        int x = pos[0];int y = pos[1];
        if( steps <= 5 + num){
            pos[0] = x;
            pos[1] = y - steps;
            return pos;
        }
        pos[1] = y - (5 + num);
        steps -= (5 + num);
        pos[0] = x - steps;
        try {
            if(! cellSafe(pos[0], pos[1])) pos = start.get(num);
        } catch(IndexOutOfBoundsException e){
            pos = start.get(num);
        }
            
        return pos;
    }
    
    private int[] stepBackO(int[] pos, int num, int steps){
        int x = pos[0]; int y = pos[1];
        // first down
        if(steps <= 7){ pos[1] = y - steps; return pos;}
        y -= 7; steps -= 7;
        // diagonal \>
        if(num < 2){
            if(steps <= 4){ pos[0] = x + steps; pos[1] = y - steps; return pos;}
            x += 4; y -= 4; steps -= 4;
        } else {
            if(steps <= 3){ pos[0] = x + steps; pos[1] = y - steps; return pos;}
            x += 3; y -= 3; steps -= 3;
        }
        // first across >
        switch(num){
            case 0:
                if(steps <= 14){ pos[0] = x + steps; pos[1] = y; return pos;}
                x += 14; steps -= 14;
                break;
            case 3:
                if(steps <= 10){ pos[0] = x + steps; pos[1] = y; return pos;}
                x += 10;steps -= 10;
                break;
            default:
                if(steps <= 12){ pos[0] = x + steps; pos[1] = y; return pos;}
                x += 12; steps -= 12;
                break;
        }
        // second diagnol />
        if(num < 2){
            if(steps <= 4){ pos[0] = x + steps; pos[1] = y + steps; return pos;}
            x += 4; y += 4; steps -= 4;
        } else {
            if(steps <= 3){ pos[0] = x + steps; pos[1] = y + steps; return pos;}
            x += 3; y += 3; steps -= 3;
        }
        // up
        if(steps <= 14){ pos[0] = x; pos[1] = y + steps; return pos; }
        y += 14; steps -= 14;
        // third diagnol <\
        if(num < 2){
            if(steps <= 4){ pos[0] = x - steps; pos[1] = y + steps; return pos;}
            x -= 4; y += 4; steps -= 4;
        } else {
            if(steps <= 3){ pos[0] = x - steps; pos[1] = y + steps; return pos;}
            x -= 3; y += 3; steps -= 3;
        }
        // second across <
        switch(num){
            case 0:
                if(steps <= 14){ pos[0] = x - steps; pos[1] = y; return pos;}
                x -= 14; steps -= 14;
                break;
            case 3:
                if(steps <= 10){ pos[0] = x - steps; pos[1] = y; return pos;}
                x -= 10; steps -= 10;
                break;
            default:
                if(steps <= 12){ pos[0] = x - steps; pos[1] = y; return pos;}
                x -= 12; steps -= 12;
                break;
        }
        // fourth diagnol </
        if(num < 2){
            if(steps <= 4){ pos[0] = x - steps; pos[1] = y - steps; return pos;}
            x -= 4; y -= 4; steps -= 4;
        } else {
            if(steps <= 3){ pos[0] = x - steps; pos[1] = y - steps; return pos;}
            x -= 3; y -= 3; steps -= 3;
        }
        // last down 
        if(steps <= 5){ pos[0] = x; pos[1] = y - steps; return pos;}
        y -= 5; steps -= 5;
        pos[0] = x; pos[1] = y;
        
        return pos;
    }
    
    private int[] stepBackR(int[] pos, int num, int steps){
        int x = pos[0];int y = pos[1];
        while(steps > 0){
            if(track[y][x] == 'S') break;
            //1-2
            if(steps <= 2){ y += steps; break;}
            y += 2; steps -= 2;x -= 1;
            //3-9
            if(steps <= 7){ y += steps; break;}
            y += 7; steps -= 7;
            //10-11
            if(steps <= 2){ y += steps; x -= (2*steps); break;}
            y += 3; x -= 7; steps -= 3;
            //12
            if(steps == 0) break;
            x -= 1;
            //13-15
            if(steps <= 3){ y += steps; x -= steps; break;}
            y += 3; x -= 7; steps -= 3;
            //16-21
            if(steps > 3) x -= 1;
            if(steps <= 6){ y += steps; x += (2*steps); break;}
            y += 6; x += 12; steps -= 6;
            //22-23
            if(steps <= 2){ y += steps; break;}
            y += 2; steps -= 2;
            
            if(num != 0 && steps > 0){
                y += 1; x -= 1; steps -= 1;
                if(num > 2 && steps > 0){
                    y += 1; x -= 1; steps -= 1;
                }
            }
            
            if(steps <= 11){ x -= steps; break;}
            x -= 11; steps -= 11;
            
            if(num != 0 && steps > 0){
                if(num > 2 && steps > 0){ y -= 1; x -= 1; steps -= 1;}
                if(num > 1){
                    if(steps <= 2){ x -= steps; break;}
                    x -= 2; steps -= 2;
                }
                if(num == 4){
                    if(steps <= 2){ x -= steps; break;}
                    x -= 2; steps -= 2;
                }
                y -= 1; x -= 1; steps -= 1;
            }
            
            if(steps <= 2){ y -= steps; x -= steps; break;}
            y -= 2; x -= 2; steps -= 2;
            
            if(steps <= 6){ y -= steps; break;}
            y -= 6; x -= 1; steps -= 6;
            
            if(steps <= 4){ y -= steps; break;}
            y -= 4; x += 1; steps -= 4;
            
            if(steps <= 5){ y -= steps; break;}
            y -= 5; x -= 1; steps -= 5;
            
            if(steps <= 5){ y -= steps; break;}
            y -= 6; steps -= 6;
        }
        pos[0] = x;
        pos[1] = y;
        return pos;
    }
}
