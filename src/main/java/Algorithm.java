import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {
    List<Being> genePool = new ArrayList<>();

    public Algorithm() throws AWTException {

    }

    public Algorithm(Being being, Being being2) throws AWTException {

    }
    public Being selectParent(List<Being> beings,int fit){
        Being parent = null;
        for (int i = 0; i < getGenePool().size(); i++) {
            if(fit==getGenePool().get(i).getScore()){
                parent = beings.get(i);
            }
        }
        return parent;
    }
    public Being createOffspring(Being father,Being mother) throws AWTException {
        Being being = new Being();
        int cut = ThreadLocalRandom.current().nextInt(0,father.getMoves().size()/2);
        for (int i = 0; i < cut; i++) {
            being.getMoves().add(father.getMoves().get(i));
        }
        for (int i = cut; i < mother.getMoves().size(); i++) {
            being.getMoves().add(mother.getMoves().get(i));
        }
        return being;
    }

    public List<Being> getGenePool() {
        return genePool;
    }
    public int getFittest(){
        int fit = 0;
        for (int i = 0; i < getGenePool().size(); i++) {
            if(fit<getGenePool().get(i).getScore()){
                fit = getGenePool().get(i).getScore();
            }
        }
        //System.out.println("most fit to: "+fit);
        return fit;
    }
    public int getSecondFittest(){
        int fit = 0;
        int fittemp = 0;
        for (int i = 0; i < getGenePool().size()-1; i++) {
            fittemp = getGenePool().get(i).getScore();
            if((getFittest()!=fittemp)&&fit<getGenePool().get(i).getScore()){
                fit = getGenePool().get(i).getScore();
            }
        }
        System.out.println("second to : "+fit);
        return fit;
    }

    public void setGenePool(List<Being> genePool) {
        this.genePool = genePool;
    }
}
