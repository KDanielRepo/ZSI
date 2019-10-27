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
    public Being selectParent(List<Being> beings){
        int selection = ThreadLocalRandom.current().nextInt(0,beings.size());
        Being parent = beings.get(selection);
        beings.remove(selection);
        return parent;
    }
    public Being createOffspring(Being father,Being mother) throws AWTException {
        Being being = new Being();
        int cut = ThreadLocalRandom.current().nextInt(0,father.moves.size()/2);
        for (int i = 0; i < cut; i++) {
            being.getMoves().add(father.getMoves().get(i));
        }
        for (int i = being.getMoves().size(); i < mother.getMoves().size(); i++) {
            being.getMoves().add(mother.getMoves().get(i));
        }
        return being;
    }

    public List<Being> getGenePool() {
        return genePool;
    }

    public void setGenePool(List<Being> genePool) {
        this.genePool = genePool;
    }
}
