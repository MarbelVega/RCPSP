import java.util.ArrayList;
import java.util.Collections;

public class Population {
	ArrayList<Individual> individuals = new ArrayList<Individual>();
	
	//private HashMap<String,Individual> pop = new HashMap<String,Individual>();
	private int cycles = 0;
	private int PopulationCapPerFitnessLevel = Integer.MAX_VALUE;
	
	public int getCycles() {
		return cycles;
	}
	
	public int getPopulationSize() {
		return individuals.size();
	}
	
	public boolean addIndividual(Individual s) {
		boolean isNew = true;
		
		for (Individual i : individuals) {
			String tmpUID = i.getUniqueOrderId();
			if (tmpUID.compareTo(s.getUniqueOrderId()) == 0) {
				isNew = false;
			}
		}
		if (isNew == true) {
			individuals.add(s);
		}
		cycles++;
		
		if (PopulationCapPerFitnessLevel != Integer.MAX_VALUE) {
			reducePopulation();
		}
		
		return isNew;
	}

	public Individual getFittest() {
		
		int minFitness = Integer.MAX_VALUE;
		Individual bestIndiviual = null;
		
		for (Individual i : individuals) {
			int fitness = i.getFitness();
			if (fitness < minFitness) {
				bestIndiviual = i;
				minFitness = fitness;
			}
		}
		
		return bestIndiviual;
	}
		
	public void sort() {		
		Collections.sort(individuals);
	}
	
	public int getPopulationCapPerFitnessLevel() {
		return PopulationCapPerFitnessLevel;
	}

	public void setPopulationCapPerFitnessLevel(int populationCapPerFitnessLevel) {
		PopulationCapPerFitnessLevel = populationCapPerFitnessLevel;
	}

	private void reducePopulation()
	{
		sort();
		int oldfitness = 0;
		int IndividualsWithSameFitness = 0;
		
		for (int i = 0; i < individuals.size(); i++) {
			int individualfitness = individuals.get(i).getFitness();
			if (oldfitness == individualfitness) {
				IndividualsWithSameFitness++;
			}
			else {
				oldfitness = individualfitness;				
				
				if (IndividualsWithSameFitness > PopulationCapPerFitnessLevel) {
					removeIndividualAtRelativePosition(i, IndividualsWithSameFitness);

					i--;
				}	
				
				IndividualsWithSameFitness = 1;
			}			
		}	
		
		if (IndividualsWithSameFitness > PopulationCapPerFitnessLevel)
		{
			removeIndividualAtRelativePosition(individuals.size(),IndividualsWithSameFitness);
		}
		
	}
	
	private void removeIndividualAtRelativePosition(int endOfRelativeBoundry, int numberOfElements) {
		double random = Math.random();
		int removeIndex = (int)(random * numberOfElements);

		removeIndex = removeIndex + endOfRelativeBoundry - numberOfElements;
		individuals.remove(removeIndex);		
	}
	
	
	public ArrayList<Individual> getIndividuals() {
		ArrayList<Individual> copyArrayList = new ArrayList<Individual>();
		
		for (Individual i : individuals) {
			copyArrayList.add(i);
		}
		
		return copyArrayList;
	}
	
}

