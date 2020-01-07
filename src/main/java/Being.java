import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Being {
    private int lp;
    private int gen;
    private List<Integer> moves;
    private int score;
    private boolean moved;
    private Robot robot;
    private int currentMove;
    private int fitness;
    private float pc;
    private int amount;

    public Being(){
        try {
            robot = new Robot();
        }catch (AWTException e){
            e.printStackTrace();
        }
        moved = false;
        lp = 0;
        gen = 0;
        score = 0;
        moves = new ArrayList<>();
        fitness = 0;
        amount = 0;
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
    public void setMove(int a,int b){
        moves.set(a,b);
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
    public int getUpMoves(){
        int a = 0;
        for (int i = 0; i < moves.size(); i++) {
            if(moves.get(i)==0){
                a++;
            }
        }
        return a;
    }
    public int getRightMoves(){
        int a = 0;
        for (int i = 0; i < moves.size(); i++) {
            if(moves.get(i)==1){
                a++;
            }
        }
        return a;
    }
    public int getDownMoves(){
        int a = 0;
        for (int i = 0; i < moves.size(); i++) {
            if(moves.get(i)==2){
                a++;
            }
        }
        return a;
    }
    public int getLeftMoves(){
        int a = 0;
        for (int i = 0; i < moves.size(); i++) {
            if(moves.get(i)==3){
                a++;
            }
        }
        return a;
    }
    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
