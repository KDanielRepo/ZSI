import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {
    private List<Being> genePool = new ArrayList<>();
    private List<Being> pcPool = new ArrayList<>();
    private Being best;

    public Algorithm() throws AWTException {

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
    public void createOffspring() {
        for (int i = 0; i < getGenePool().size(); i++) {
            getGenePool().get(i).setPc(ThreadLocalRandom.current().nextFloat());
        }
        for (int i = 0; i < getGenePool().size(); i++) {
            if(getGenePool().get(i).getPc()>0.6f&&getPcPool().size()<26){
                getPcPool().add(getGenePool().get(i));
                getGenePool().remove(i);
            }
        }
        int testt = 0;
        for (int i = 0; i < getPcPool().size(); i++) {
            if(getPcPool().get(i).getScore()!=best.getScore()){
                testt++;
            }
            if(testt==getPcPool().size()){
                getPcPool().remove(ThreadLocalRandom.current().nextInt(0,getPcPool().size()));
                getPcPool().add(best);
            }
        }
        if(getPcPool().size()<26 || getPcPool().size()%2!=0){
            int random = ThreadLocalRandom.current().nextInt(0,getGenePool().size());
            getPcPool().add(getGenePool().get(random));
        }
        int[] a = new int[26];
        for (int i = 0; i < getPcPool().size()/2; i++) {
            int random = ThreadLocalRandom.current().nextInt(0,26);
            if(a[random]==0){
                a[random]=1;
            }else{
                while (a[random]!=0){
                    random = ThreadLocalRandom.current().nextInt(0,26);
                }
            }
            Being being1 = getPcPool().get(random);
            int random2 = ThreadLocalRandom.current().nextInt(0,26);
            if(a[random2]==0){
                a[random2]=1;
            }else{
                while (a[random2]!=0){
                    random2 = ThreadLocalRandom.current().nextInt(0,26);
                }
            }
            Being being2 = getPcPool().get(random2);
            Being child1 = null;
            Being child2 = null;
            int cut = ThreadLocalRandom.current().nextInt(0,being1.getMoves().size()/2);
            for (int k = 0; k < cut; k++) {
                child1.getMoves().add(being1.getMoves().get(k));
            }
            for(int k = 0; k<cut;k++){
                child2.getMoves().add(being2.getMoves().get(k));
            }
            for (int k = cut; k < being2.getMoves().size(); k++) {
                child1.getMoves().add(being2.getMoves().get(k));
            }
            for (int k = cut; k < being1.getMoves().size(); k++) {
                child2.getMoves().add(being1.getMoves().get(k));
            }
            getGenePool().add(child1);
            getGenePool().add(child2);
        }
        System.out.println(getGenePool().size());
    }
    public int calculateGlobalFitness(){
        int sum = 0;
        for (int i = 0; i < getGenePool().size(); i++) {
            sum+=getGenePool().get(i).getScore();
        }
        return sum/getGenePool().size();
    }
    public void calculateRFitness(){
        for (int i = 0; i < getGenePool().size(); i++) {
            getGenePool().get(i).setFitness((getGenePool().get(i).getScore()/calculateGlobalFitness()));
        }
    }
    public List<Being> getGenePool() {
        return genePool;
    }
    public List<Being> getPcPool(){return pcPool;}
    public int getFittest(){
        int fit = 0;
        for (int i = 0; i < getGenePool().size(); i++) {
            if(fit<getGenePool().get(i).getScore()){
                fit = getGenePool().get(i).getScore();
                best = getGenePool().get(i);
            }
        }
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
        return fit;
    }
}
