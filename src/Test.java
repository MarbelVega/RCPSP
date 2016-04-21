

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Test {
	
	public static void main (String[] args) throws FileNotFoundException{
///Georgs Test
		//////////////////////////////////////////////////////////////////////////////
		//1) READ INSTANCE (http://www.om-db.wi.tum.de/psplib/)

 
		
		Job[] jobs     = Job.read(new File("instances/j1201_5.sm"));//best makespan=112
		Resource[] res = Resource.read(new File("instances/j1201_5.sm"));
//		Job[] jobs     = Job.read(new File("j301_1.sm"));//optimum makespan=43
//		Resource[] res = Resource.read(new File("j301_1.sm"));
		for(int i = 0; i < jobs.length; i++){
			jobs[i].calculatePredecessors(jobs);
		}
		
		
		
		
		//////////////////////////////////////////////////////////////////////////////
		//2) INITIALIZE BEST INDIVIDUAL S
		
		Individual s = new Individual();//generate an individual
		s.initializeJobList(jobs);		//initialize genotype of the individual
		s.decodeJobList(jobs, res);		//decoding of the individual
		System.out.println("Fitness (=makespan): " + s.getFitness());
		

		
		
		//////////////////////////////////////////////////////////////////////////////
		//3) GENERATE NEW INDIVIDUALS BY MUTATION

		for(int g=0;g<10000000;g++){
			Individual c = new Individual();//generate an individual
			c.reproduce(s);					//calculate a clone of s
			c.mutate(jobs);					//mutate the individual c
			c.decodeJobList(jobs, res);		//decoding of the individual c
			
			if(c.getFitness() <= s.getFitness()){
				//REPLACE BEST INDIVIDUAL
				System.out.println(g + " new fitness (=makespan): " + c.getFitness());
				s = new Individual();			//generate a new best individual
				s.reproduce(c);					//calculate a clone of c
				s.decodeJobList(jobs, res);		//decoding of the individual s
			}
		}
		
		
		
	}
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//next methods are only for checking the data, which has been read from the input files
	
	private static void auslesen(Job[] jobs) {
		for (int i = 0; i < jobs.length; i++){
			System.out.print("Nummer: " + jobs[i].nummer()+"     |    ");
			System.out.print("Nachfolger: ");
			ArrayList<Integer> nachfolger = jobs[i].nachfolger();
			for(int j = 0; j < nachfolger.size(); j++){
				System.out.print(" " + nachfolger.get(j) +" ");
				
			}
			System.out.print(" Vorgaenger: ");
			ArrayList<Integer> vorgaenger = jobs[i].vorgaenger();
			for(int j = 0; j < vorgaenger.size(); j++){
				System.out.print(" " + vorgaenger.get(j) +" ");
				
			}
			System.out.print("     |    ");
			System.out.print("Dauer: " + jobs[i].dauer() + "     |    ");
			System.out.println("R1: " + jobs[i].verwendeteResource(0) +  "  R2: " + jobs[i].verwendeteResource(1) + 
					"  R3: " + jobs[i].verwendeteResource(2) + "  R4: " + jobs[i].verwendeteResource(3));
		}
	}
	
	private static void auslesen(Resource[] resource) {
		for (int i = 0; i < resource.length; i++){
			System.out.print("Resource: " + resource[i].nummer()+"     |    ");
			System.out.println("Verfügbarkeit: " + resource[i].maxVerfuegbarkeit());
		}
	}
}
