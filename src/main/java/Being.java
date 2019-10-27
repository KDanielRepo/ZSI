import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Being {
    List<Integer> moves;
    int score;
    boolean moved;
    Robot robot;
    int currentMove;
    public Being()throws AWTException{
        robot = new Robot();
        moved = false;
        score = 0;
        moves = new ArrayList<>();
    }

    public void generateMove() throws AWTException {
        int move = ThreadLocalRandom.current().nextInt(0,4);
        if(move==0){
            robot.keyPress(KeyEvent.VK_UP);
            robot.delay(20);
            robot.keyRelease(KeyEvent.VK_UP);
        }else if(move==1){
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.delay(20);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        }else if(move==2){
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.delay(20);
            robot.keyRelease(KeyEvent.VK_DOWN);
        }else if(move==3){
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.delay(20);
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
            robot.delay(20);
            robot.keyRelease(KeyEvent.VK_UP);
        }else if(currentMove==1){
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.delay(20);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        }else if(currentMove==2){
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.delay(20);
            robot.keyRelease(KeyEvent.VK_DOWN);
        }else if(currentMove==3){
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.delay(20);
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
}
