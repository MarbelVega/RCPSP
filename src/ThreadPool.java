import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class ThreadPool {
	
	private static int startFileNumber = 1;
	private static int endFileNumber = 10;
	private static int Threadpoolsize = 5;
 
    public static void main(String[] args) {
    	
		int numberOfSolutions = 0;
		
		File folder = new File("instances");
		File[] listOfFiles = folder.listFiles();
	    
        ExecutorService executor = Executors.newFixedThreadPool(Threadpoolsize);
        
	    for (File file : listOfFiles) {
			numberOfSolutions++;
//			Calculate solution for problem instance
			if (numberOfSolutions >= startFileNumber && numberOfSolutions <= endFileNumber) {
				System.out.println("Starting file "+file.getPath());
				Runnable worker = new WorkerThread(file.getPath(), numberOfSolutions);
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
    
}
 