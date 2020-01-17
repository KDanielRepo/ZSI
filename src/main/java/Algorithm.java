import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Algorithm {
    private List<Being> genePool = new ArrayList<>();
    private List<Being> pcPool = new ArrayList<>();

    public Being getBest() {
        return best;
    }

    private Being best = new Being();

    public Algorithm() {
    }

    /*public Being selectParent(List<Being> beings,int fit){
        Being parent = null;
        for (int i = 0; i < getGenePool().size(); i++) {
            if(fit==getGenePool().get(i).getScore()){
                parent = beings.get(i);
            }
        }
        return parent;
    }*/
    public void createOffspring() {
        //ustawianie PC kazdego osobnika
        for (int i = 0; i < getGenePool().size(); i++) {
            getGenePool().get(i).setPc(ThreadLocalRandom.current().nextFloat());
        }
        //wybieranie osobnikow do puli c
        for (int i = 0; i < getGenePool().size(); i++) {
            if(getGenePool().get(i).getPc()>0.6f&&getPcPool().size()<150){
                getPcPool().add(getGenePool().get(i));
                getGenePool().remove(i);
            }
        }
        //uzupelnienie puli do liczby parzystej
        while(getPcPool().size()<150 || getPcPool().size()%2!=0){
            int random = ThreadLocalRandom.current().nextInt(0,getGenePool().size());
            getPcPool().add(getGenePool().get(random));
            getGenePool().remove(random);
        }
        //sprawdzenie czy najlepszy osobnik jest w puli
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
        //krzyzowanie
        int[] a = new int[150];
        for (int i = 0; i < getPcPool().size()/2; i++) {
            int random = ThreadLocalRandom.current().nextInt(0,150);
            if(a[random]==0){
                a[random]=1;
            }else{
                while (a[random]!=0){
                    random = ThreadLocalRandom.current().nextInt(0,150);
                }
            }
            Being being1 = getPcPool().get(random);
            int random2 = ThreadLocalRandom.current().nextInt(0,150);
            if(a[random2]==0){
                a[random2]=1;
            }else{
                while (a[random2]!=0){
                    random2 = ThreadLocalRandom.current().nextInt(0,150);
                }
            }
            Being being2 = getPcPool().get(random2);
            Being child1 = new Being();
            Being child2 = new Being();
            int cut = ThreadLocalRandom.current().nextInt(0,(being1.getMoves().size()/2));
            int cut2 = ThreadLocalRandom.current().nextInt(0,being2.getMoves().size()/2);
            for (int k = 0; k < cut; k++) {
                child1.getMoves().add(being1.getMoves().get(k));
            }
            for(int k = 0; k<cut2;k++){
                child2.getMoves().add(being2.getMoves().get(k));
            }
            for (int k = cut; k < being1.getMoves().size(); k++) {
                child2.getMoves().add(being1.getMoves().get(k));
            }
            for (int k = cut2; k < being2.getMoves().size(); k++) {
                child1.getMoves().add(being2.getMoves().get(k));
            }
            getGenePool().add(child1);
            getGenePool().add(child2);
        }
    }
    public void resetPcPool(){
        pcPool = new ArrayList<>();
    }
    public void mutate(){
        for (int i = 0; i < getGenePool().size(); i++) {
            for (int j = 0; j < getGenePool().get(i).getMoves().size(); j++) {
                float temp = (float) getGenePool().get(i).getMoves().size();
                float probability = 1/temp;
                float random = ThreadLocalRandom.current().nextFloat()/100;
                if(random<probability){
                    int rrandom = ThreadLocalRandom.current().nextInt(0,3);
                    while (rrandom==getGenePool().get(i).getMoves().get(j)){
                        rrandom = ThreadLocalRandom.current().nextInt(0,3);
                    }
                    getGenePool().get(i).setMove(j,rrandom);
                }
            }
        }
    }
    public void getAverageFitness(){
        int sum=0;
        for (int i = 0; i < getGenePool().size(); i++) {
            sum+=getGenePool().get(i).getScore();
        }
        System.out.println(sum/getGenePool().size());
    }
    public int calculateGlobalFitness(){
        int sum = 0;
        for (int i = 0; i < getGenePool().size(); i++) {
            sum+=getGenePool().get(i).getScore();
        }
        return sum;
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
                if(best.getScore()<fit){
                    System.out.println("tempbest: "+best.getScore()+" , gentemp: "+fit);
                    best = getGenePool().get(i);
                }
            }
        }
        System.out.println("best of this gen: "+fit);
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
