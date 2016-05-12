import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	private static int startFileNumber = 101;	//Start with this file-Nr
	private static int endFileNumber = 150;		//End with this file-Nr
	private static int Threadpoolsize = 5;		//Number of parallel tasks
    private static int populationCap = 5;		//keep n Solutions for each fitness-value
    private static int numberOfCycles = 50000;	//Target cycle-Number
    private static int numberOfMutations = 100; //Number of Child mutations
 
    public static void main(String[] args) {
    	
		int numberOfSolutions = 0;
		
		File folder = new File("instances");
		File[] listOfFiles = folder.listFiles();
	    
        ExecutorService executor = Executors.newFixedThreadPool(Threadpoolsize);
        
	    for (File file : listOfFiles) {
			numberOfSolutions++;
//			Calculate solution for problem instance
			if (numberOfSolutions >= startFileNumber && numberOfSolutions <= endFileNumber) {
				Runnable worker = new WorkerThread(file.getPath(), numberOfSolutions, populationCap, numberOfCycles, numberOfMutations);
				executor.execute(worker);	
			}
			
		}
	    
	    executor.shutdown();
	    
        while (!executor.isTerminated()) {
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        System.out.println("Finished all threads");
    	
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
