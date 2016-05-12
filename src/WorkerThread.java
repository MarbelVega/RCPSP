import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorkerThread implements Runnable {
     
    private String filePath;
    private int fileNumber;
    private int populationCap;
    private int numberOfCycles;
    private int numberOfMutations;
     
    public WorkerThread(String filePath, int fileNumber, int populationCap, int numberOfCycles, int numberOfMutations){
        this.filePath=filePath;
        this.fileNumber=fileNumber;
        this.populationCap=populationCap;
        this.numberOfCycles=numberOfCycles;
        this.numberOfMutations=numberOfMutations;
    }
 
    @Override
    public void run() {
    	System.out.println(fileNumber+"-"+Thread.currentThread().getName()+" Start.");
        try {
        	Solution solution = processOneInstance(filePath);
			writeToCSV(solution);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(fileNumber+"-"+Thread.currentThread().getName()+" End.");
    }

    private synchronized void writeToCSV(Solution solution) throws IOException {

    	String output = "File_name;"+filePath+";FileNr;"+fileNumber+";Cycles;"+solution.getCycles()+";PopulationSize;"+solution.getPopulation()+";Fitness;" + solution.getFitness() + ";Critical_path;"+solution.getCriticalPath() + ";Deviation;"+solution.getDeviation() + ";UID;"+solution.getFittestIndividualUID();
		System.out.println(output+"\n");
    	
	    FileWriter fw = new FileWriter("ausgabe.csv",true);
	    BufferedWriter bw = new BufferedWriter(fw);	    
	   
		bw.write(output+"\n");
		bw.flush();
		bw.close();
	    
    }
    
    private Solution processOneInstance(String fileName) throws Exception {
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
		//System.out.println("Fitness start point (=makespan): " + firstMother.getFitness());

		Population population = new Population();
		population.setPopulationCapPerFitnessLevel(populationCap); // keep only n individuals per fitness-level
		population.addIndividual(firstMother);
		Individual firstFather = new Individual();// generate an individual
		firstFather.reproduce(firstMother);
		
		boolean isNewParent = false;
		while (isNewParent == false) {
			for (int i = 0; i < (numberOfMutations*100); i++) {
				firstFather.mutate(jobs);	
			}
			
			firstFather.decodeJobList(jobs, res);
			isNewParent = population.addIndividual(firstFather);
		}
		
		Evolution evolution = new Evolution();

		//////////////////////////////////////////////////////////////////////////////
		// 3) GENERATE NEW INDIVIDUALS BY MUTATION
		while (population.getCycles() < (numberOfCycles - 1) && solution.hasFoundBestSolution() == false) {
			ArrayList<Individual> pickedParents = evolution.getRankedIndividuals(population); //random pick of two but weighed with fitness //Marc
			ArrayList<Individual> crossoveredChildren = evolution.fixedCrossover(pickedParents.get(0), pickedParents.get(1)); //crossover the joblists of them //Patrick
			ArrayList<Individual> crossoveredAndMutatedChildren = evolution.mutate(jobs, crossoveredChildren,numberOfMutations); //take it from individual and put into evolution //Georg

			for (Individual child : crossoveredAndMutatedChildren) {
				child.decodeJobList(jobs, res);
				population.addIndividual(child);
			}
			
			if (population.getFittest().getFitness() == criticalPathIndividual.getFitness())
			{
				solution.setFoundBestSolution(true);
			}
			
			if(population.getCycles() % 10000 == 0 || solution.hasFoundBestSolution() == true){
				System.out.println(fileNumber+"-"+Thread.currentThread().getName()+" Cycles: " + population.getCycles() + " - Population: " + population.getPopulationSize() + " - Fittest Individual: " + population.getFittest().getFitness() + " of " + criticalPathIndividual.getFitness());
			}
		}
		Individual bestIndividual = population.getFittest();
		solution.setFittestIndividualUID(bestIndividual.getUniqueOrderId());
		solution.setFitness(bestIndividual.getFitness());
		solution.setCycles(population.getCycles());
		solution.setPopulation(population.getPopulationSize());
		return solution;
	}
    
}