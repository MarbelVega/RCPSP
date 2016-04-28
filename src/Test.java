
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws Exception {
		
		double averageDeviation = 0;
		int numberOfSolutions = 0;
		
		File folder = new File("instances");
		File[] listOfFiles = folder.listFiles();
		
	    FileWriter fw = new FileWriter("ausgabe.txt");
	    BufferedWriter bw = new BufferedWriter(fw);
	    
//		Iterate over all files in the folder
		for (File file : listOfFiles) {
//			Calculate solution for problem insatnce
			System.out.println("Starting file "+file.getPath());
			Solution solution = Test.processOneInstance(file.getPath());	
			String output = "File name: "+file.getPath()+" - Fitness: " +solution.getFitness() + " - Critical path: "+solution.getCriticalPath() + " - Deviation: "+solution.getDeviation();
			System.out.println(output);
		   
			bw.write(output);

			averageDeviation = averageDeviation + solution.getDeviation();
			numberOfSolutions++;
		}
		bw.close();
		
		System.out.println((double)averageDeviation / numberOfSolutions);
	}

	private static Solution processOneInstance(String fileName) throws Exception {
		//////////////////////////////////////////////////////////////////////////////
		// 1) READ INSTANCE (http://www.om-db.wi.tum.de/psplib/)
		Job[] jobs = Job.read(new File(fileName));// best makespan=112
		Resource[] res = Resource.read(new File(fileName));
		Solution solution = new Solution();
		Resource[] criticalPathRessouce = Resource.read(new File(fileName));	
		Resource.maximizeResources(criticalPathRessouce);		
		
		for (int i = 0; i < jobs.length; i++) {
			jobs[i].calculatePredecessors(jobs);
		}
		
		//Calculating critical Path
		Individual criticalPathIndividual = new Individual();
		criticalPathIndividual.initializeJobList(jobs);
		criticalPathIndividual.decodeJobList(jobs, criticalPathRessouce);
		solution.setCriticalPathFitness(criticalPathIndividual.getFitness());

		//////////////////////////////////////////////////////////////////////////////
		// 2) INITIALIZE BEST INDIVIDUAL S
		Individual firstMother = new Individual();// generate an individual
		firstMother.initializeJobList(jobs); // initialize genotype of the individual
		firstMother.decodeJobList(jobs, res); // decoding of the individual
		System.out.println("Fitness (=makespan): " + firstMother.getFitness());

		Population population = new Population();
		population.addIndividual(firstMother);
		Individual firstFather = new Individual();// generate an individual
		firstFather.reproduce(firstMother);
		
		boolean isNewParent = false;
		while (isNewParent == false) {
			for (int i = 0; i < 20; i++) {
				firstFather.mutate(jobs);	
			}
			
			firstFather.decodeJobList(jobs, res);
			isNewParent = population.addIndividual(firstFather);
		}
		
		Evolution evolution = new Evolution();

		//////////////////////////////////////////////////////////////////////////////
		// 3) GENERATE NEW INDIVIDUALS BY MUTATION
		while (population.getCycles() <= 50000) {
			ArrayList<Individual> pickedParents = evolution.getRankedIndividuals(population); //random pick of two but weighed with fitness //Marc
			ArrayList<Individual> crossoveredChildren = evolution.crossover(pickedParents.get(0), pickedParents.get(1)); //crossover the joblists of them //Patrick
			ArrayList<Individual> crossoveredAndMutatedChildren = evolution.mutate(jobs, crossoveredChildren); //take it from individual and put into evolution //Georg

			for (Individual child : crossoveredAndMutatedChildren) {
				child.decodeJobList(jobs, res);
				population.addIndividual(child);
			}
			if(population.getCycles() % 500 == 0){
				System.out.println("Cycles: " + population.getCycles() + " - Population: " + population.getPopulationSize() + " - Fittest Individual: " + population.getFittest().getFitness());
			}
		}
		Individual bestIndividual = population.getFittest();
		solution.setFitness(bestIndividual.getFitness());
		return solution;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// next methods are only for checking the data, which has been read from the
	/////////////////////////////////////////////////////////////////////////////////////////////// input
	/////////////////////////////////////////////////////////////////////////////////////////////// files

	private static void auslesen(Job[] jobs) {
		for (int i = 0; i < jobs.length; i++) {
			System.out.print("Nummer: " + jobs[i].nummer() + "     |    ");
			System.out.print("Nachfolger: ");
			ArrayList<Integer> nachfolger = jobs[i].nachfolger();
			for (int j = 0; j < nachfolger.size(); j++) {
				System.out.print(" " + nachfolger.get(j) + " ");

			}
			System.out.print(" Vorgaenger: ");
			ArrayList<Integer> vorgaenger = jobs[i].vorgaenger();
			for (int j = 0; j < vorgaenger.size(); j++) {
				System.out.print(" " + vorgaenger.get(j) + " ");

			}
			System.out.print("     |    ");
			System.out.print("Dauer: " + jobs[i].dauer() + "     |    ");
			System.out.println("R1: " + jobs[i].verwendeteResource(0) + "  R2: " + jobs[i].verwendeteResource(1)
					+ "  R3: " + jobs[i].verwendeteResource(2) + "  R4: " + jobs[i].verwendeteResource(3));
		}
	}

	private static void auslesen(Resource[] resource) {
		for (int i = 0; i < resource.length; i++) {
			System.out.print("Resource: " + resource[i].nummer() + "     |    ");
			System.out.println("Verfügbarkeit: " + resource[i].maxVerfuegbarkeit());
		}
	}
}
