import java.util.HashMap;

public class Population {

	private HashMap<String,Individual> pop = new HashMap<String,Individual>();
	private int cycles = 0;
	
	public int getCycles() {
		return cycles;
	}
	
	public int getPopulationSize() {
		return pop.size();
	}
	
	public void addIndividual(Individual s) {
		pop.put(s.getUniqueOrderId(), s);
		cycles++;
	}
	
}

