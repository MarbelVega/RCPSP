import java.util.ArrayList;

public class Evolution {

	public ArrayList<Individual> mutate(ArrayList<Individual> crossoveredChildren) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Individual> crossover(Individual individual, Individual individual2) {
		Individual son = new Individual();
		Individual daughter = new Individual();
		ArrayList<Individual> children = new ArrayList<Individual>();
		
		int length1 = individual.jobListe.length;
		int length2 = individual2.jobListe.length;

		if(length1 != length2){
			//TODO Exception or sth.
			System.out.println("Something wrong with Individuals. Number of job items doesn't match.");
		}
		
		//TODO dummy
		son.jobListe = individual.jobListe;
		daughter.jobListe = individual2.jobListe;
		
		children.add(son);
		children.add(daughter);
		return children;
	}

	public ArrayList<Individual> getRankedIndividuals(Population population) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Individual> mutate(Job[] jobs, ArrayList<Individual> individuals){
		for (Individual individual : individuals) {
			individual.mutate(jobs);
		}
		return individuals;
	}

}
