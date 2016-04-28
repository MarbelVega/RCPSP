import java.util.ArrayList;
import java.util.Collections;

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
		boolean isNew = true;
		
		for (Individual i : individuals) {
			if (i.getUniqueOrderId() == s.getUniqueOrderId()) {
				isNew = false;
			}
		}
		if (isNew == true) {
			individuals.add(s);
		}
		cycles++;
	}

	public Individual getFittest() {
		
		int maxFitness = 0;
		Individual bestIndiviual = null;
		
		for (Individual i : individuals) {
			int fitness = i.getFitness();
			if (fitness > maxFitness) {
				bestIndiviual = i;
				maxFitness = fitness;
			}
		}
		
		return bestIndiviual;
	}
		
	public void sort() {		
		Collections.sort(individuals);
	}

	public ArrayList<Individual> getIndividuals() {
		ArrayList<Individual> copyArrayList = new ArrayList<Individual>();
		
		for (Individual i : individuals) {
			copyArrayList.add(i);
		}
		
		return copyArrayList;
	}
	
}

