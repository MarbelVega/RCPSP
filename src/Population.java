import java.util.ArrayList;
import java.util.HashMap;

public class Population {
	ArrayList<Individual> individuals = new ArrayList<Individual>();
	
	//private HashMap<String,Individual> pop = new HashMap<String,Individual>();
	private int cycles = 0;
	
	public int getCycles() {
		return cycles;
	}
	
	public int getPopulationSize() {
		return individuals.size();
	}
	
	public void addIndividual(Individual s) {
		individuals.put(s.getUniqueOrderId(), s);
		cycles++;
	}

	public Individual getFittest() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

