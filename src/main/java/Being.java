import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Being {
    private List<Integer> moves;
    private int score;
    private boolean moved;
    private Robot robot;
    private int currentMove;
    private int fitness;
    private float pc;

    public Being(){
        try {
            robot = new Robot();
        }catch (AWTException e){
            e.printStackTrace();
        }
        moved = false;
        score = 0;
        moves = new ArrayList<>();
        fitness = 0;
    }

    public void generateMove() {
        int move = ThreadLocalRandom.current().nextInt(0,4);
        if(move==0){
            robot.keyPress(KeyEvent.VK_UP);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_UP);
        }else if(move==1){
            robot.keyPress(KeyEvent.VK_RIGHT);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        }else if(move==2){
            robot.keyPress(KeyEvent.VK_DOWN);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_DOWN);
        }else if(move==3){
            robot.keyPress(KeyEvent.VK_LEFT);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_LEFT);
        }
        if(moved){
            moves.add(move);
        }
    }
    public void playMove(int i){
        currentMove = moves.get(i);
        if(currentMove==0){
            robot.keyPress(KeyEvent.VK_UP);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_UP);
        }else if(currentMove==1){
            robot.keyPress(KeyEvent.VK_RIGHT);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        }else if(currentMove==2){
            robot.keyPress(KeyEvent.VK_DOWN);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_DOWN);
        }else if(currentMove==3){
            robot.keyPress(KeyEvent.VK_LEFT);
            //robot.delay(10);
            robot.keyRelease(KeyEvent.VK_LEFT);
        }
    }
    public void setMoved(boolean moved){
        this.moved = moved;
    }

    public List<Integer> getMoves() {
        return moves;
    }

    public void setMoves(List<Integer> moves) {
        this.moves = moves;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public float getPc() {
        return pc;
    }

    public void setPc(float pc) {
        this.pc = pc;
    }
}
