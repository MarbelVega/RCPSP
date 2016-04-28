import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

public class Evolution {

	public ArrayList<Individual> crossover(Individual father, Individual mother) throws Exception {
		Individual son = new Individual();
		Individual daughter = new Individual();
		ArrayList<Individual> children = new ArrayList<Individual>();
		
		int length1 = father.jobListe.length;
		int length2 = mother.jobListe.length;

		if(length1 != length2){
			throw new InvalidAttributesException("Something wrong with Individuals. Number of job items doesn't match.");
		}
		
		for(int i =0; i < length1; i++){
			if (i <= length1/2){
				son.jobListe[i] = father.jobListe[i];
				daughter.jobListe[i] = mother.jobListe[i];
			} else{					
				son.jobListe[i] = mother.jobListe[i];
				daughter.jobListe[i] = father.jobListe[i];
			}
		}
		normalizeChild(son, mother, (length1/2)+1, length1, 0, (length1/2));
		normalizeChild(daughter, father, 0, length1/2, (length1/2)+1, length1);
		
		children.add(son);
		children.add(daughter);
		return children;
	}
	
	public void normalizeChild(Individual child, Individual parent, int normalizeFrom, int normalizeTo, int validFrom, int validTo){
		ArrayList<Integer> validPartOfChildJobListe = new ArrayList<Integer>();
		for(int j = validFrom; j <= validTo; j++){
			validPartOfChildJobListe.add(child.jobListe[j]);
		}

		for(int i = normalizeFrom; i <= normalizeTo; i++){
			//check in part that needs to be normalized if valid part of job list already has that value
			int possiblyInvalidEntry = child.jobListe[i];
			if (validPartOfChildJobListe.contains(possiblyInvalidEntry)){ 
				//replace it with first valid entry of parent from the beginning of parents
				for (Integer possibleValidEntryFromParents : parent.jobListe){
					if (!validPartOfChildJobListe.contains(possibleValidEntryFromParents)){
						child.jobListe[i] = possibleValidEntryFromParents;
						break;
					}
				}
			}
		}
	}


	public ArrayList<Individual> getRankedIndividuals(Population population) {
		
		ArrayList<Individual> rankedIndividuals = new ArrayList<Individual>();
		
		population.sort();
		
		ArrayList<Individual> SortedPopulation = population.getIndividuals();
		
		//Parent 1
		rankedIndividuals.add(getRankedParent(SortedPopulation));
		//Parent 2
		// TODO: Iterate until Parent 1 != Parent 2
		rankedIndividuals.add(getRankedParent(SortedPopulation));	
		
		return rankedIndividuals;
	}
	
	private Individual getRankedParent(ArrayList<Individual> pop) {
		int popSize = pop.size();
		long sumRank = (popSize * (popSize+1)) / 2;
		int Rank = popSize;
		double propability = 0.0;
		double random = Math.random();
		
		for (int i = 0; i < popSize; i++) {
			propability = propability + (double)Rank / sumRank;
			Rank--;
			
			if (random <= propability) {
				return pop.get(i);
			}
		}
		//never reached
		return null;
	}
	
	public ArrayList<Individual> mutate(Job[] jobs, ArrayList<Individual> individuals){
		for (Individual individual : individuals) {
			individual.mutate(jobs);
		}
		return individuals;
	}

}
