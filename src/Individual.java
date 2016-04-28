import java.util.ArrayList;

public class Individual implements Comparable<Individual>{

	int[] jobListe;//genotype
	int[] schedule;//phänotype
	private String UniqueOrderId = null;
	private boolean wasDecoded = false;
	
	
	//example of the data structure:
	//jobListe: 1 3 6 2 4 5 7  ; that means, jobs should schedule by the order 1,3,...,7; the Jobs 1 and 7 are the dummy jobs
	//schedule: 0 0 0 1 2 4 6  ; that means, that job with number 6 starts at time 0; that job with number 4 starts at time 2; that dummy job with number 7 starts at time 6 which also means, that the project is finished 
			
	
	
	public void reproduce(Individual elter){
		//generate a clone of elter, that means the jobListe of elter is copied
		jobListe = new int[elter.jobListe.length];
		for(int i=0; i<jobListe.length; i++){
			jobListe[i] = elter.jobListe[i];
		}		
	}
	
	public void mutate(Job[] jobs){
		//make one feasible swap move on the jobListe
		
		boolean swapFeasible = false;
		int pos1 = 1;
		int pos2 = 2;
		int nr1;
		int nr2;
		
		while(!swapFeasible){
			pos1 = 1+(int)((jobListe.length-2)*Math.random());
			pos2 = pos1+1;
			nr1  = jobListe[pos1];
			nr2  = jobListe[pos2];
			
			Job j    = Job.getJob(jobs, nr1);
			ArrayList<Integer> nachfolger = j.nachfolger;
			
			if(!nachfolger.contains(nr2))swapFeasible=true;
		}
		
		int tmp = jobListe[pos1];
		jobListe[pos1] = jobListe[pos2];
		jobListe[pos2] = tmp;
	}
	
	public void initializeJobList(Job[] jobs){
		
		ArrayList<Job> eligibleJobs = new ArrayList<Job>();
		jobListe = new int[jobs.length];
		
		// 1. Job to jobListe
		int count = 0; 
		jobListe[count] = jobs[0].nummer();
		count++;
		ArrayList<Integer> nachfolgerAkt = jobs[0].nachfolger();
		for(int i = 0; i < nachfolgerAkt.size(); i++){
			eligibleJobs.add(Job.getJob(jobs, nachfolgerAkt.get(i)));
		}
		
		while(count != jobs.length)
		{
			Job min = eligibleJobs.get(0); 
			int minDauer = eligibleJobs.get(0).dauer();
			for(int i = 0; i < eligibleJobs.size(); i++){
				if(eligibleJobs.get(i).dauer < minDauer){
					minDauer = eligibleJobs.get(i).dauer;
					min = eligibleJobs.get(i);
				}
			}

			jobListe[count] = min.nummer;
			count++;
			eligibleJobs.remove(min);
			
			nachfolgerAkt = min.nachfolger();
			for(int i = 0; i < nachfolgerAkt.size(); i++){
				Job aktuellerNachfolgerJob = Job.getJob(jobs, nachfolgerAkt.get(i));
				ArrayList<Integer> vorgaengerAkt = aktuellerNachfolgerJob.vorgaenger;
				boolean alleVorgaenger = true;
				for(int j = 0; j < vorgaengerAkt.size();j++){
					boolean found = false;
					for(int k = 0; k < jobListe.length; k++){
						if(jobListe[k] == vorgaengerAkt.get(j)){
							found = true;
						}
					}
					if(!found){
						alleVorgaenger = false;
						break;
					}
						
				}
				if(alleVorgaenger)
				{
					eligibleJobs.add(Job.getJob(jobs, nachfolgerAkt.get(i)));
				}
			}
		}
	}

	public void decodeJobList(Job[] jobs, Resource[] res){
		//calculate the starting times of the jobs in the order of jobListe
		
		
		schedule = new int[jobListe.length];
		
		//calculate the maximum possible makespan "maxDauer" of the project
		int maxDauer = 0;
		for(int i = 0; i < jobs.length; i++){
			maxDauer += jobs[i].dauer;
		}
		
		//initialize a resource table, which is used to check resource restrictions
		//each row is a resource
		//each column is a period
		//so the following resource table contains for each resource and each period the amount of available capacities
		int[][] resourcenTableau = new int[res.length][maxDauer];
		
		for(int i = 0; i < resourcenTableau.length; i++){
			for(int j = 0; j < resourcenTableau[i].length; j++){
				resourcenTableau[i][j] = res[i].maxVerfuegbarkeit();
			}
		}
		
		for(int i = 0; i < jobListe.length; i++){
			int nr = jobListe[i];
						
			Job j = Job.getJob(jobs, nr);
			int p1 = earliestPossibleStarttime(j, jobs);
			int p2 = starttime(j, p1, resourcenTableau);
			actualizeResources(j, resourcenTableau, p2);
			
			schedule[i] = p2;
		}
		
		wasDecoded = true;
		
	}
	
	public int getFitness(){
		//the start time of last job (= dummy job) is the makespan of the project and thus the fitness of the individual
		return schedule[schedule.length-1];
	}
	
	public String getUniqueOrderId() {
		//return the joborder as a String to create a unique  ID
		
		if (wasDecoded == true) {
			if (UniqueOrderId == null) {
				String tmpString = "";
				for(int k = 0; k < schedule.length; k++){
					for(int i = 0; i < schedule.length; i++){
						for(int j = 0; j < jobListe.length; j++){
							if (k == schedule[i] && j == jobListe[i]) {
								tmpString += jobListe[i];
							}
						}
					}			
				}
				UniqueOrderId = tmpString;
			}
		}
		else
		{
			System.out.println( "Error: Inidivual was not decoded" );
		}
		return UniqueOrderId;
	}
	
	public int earliestPossibleStarttime(Job j, Job[] jobs){
		// calculate the time, when all successors of j have been finished
		
		ArrayList<Integer> vorgaenger = j.vorgaenger;
		
		int earliestStart = 0;
		
		for(int i = 0; i < vorgaenger.size(); i++){
			Job vorg = Job.getJob(jobs, vorgaenger.get(i));
			
			for(int k = 0; k < jobListe.length; k++){
				if(jobListe[k] == vorg.nummer){
					if((schedule[k] + vorg.dauer()) > earliestStart)
						earliestStart = schedule[k] + vorg.dauer();
				}
			}
		}
		return earliestStart;
	}
	
	public int starttime(Job j, int p1, int[][] resTab){
		// Prüfen, ob ab diesem Zeitpunkt genügend resourcen für die Dauer des Jobs vorhanden sind
		int[] verwendeteResourcen = j.verwendeteResourcen;
		boolean genug = true;
		int count = 0; 
		do{
			genug = true;
			if(count != 0)
			{
				p1++;
			}
			for(int k = 0; k < resTab.length; k++){
				for(int i = p1; i < (p1 + j.dauer); i++){
					if(resTab[k][i] < verwendeteResourcen[k]){
						genug = false;
					}
				}
			}
			count++;
		}while(!genug);
		return p1;
	}
	
	public void actualizeResources(Job j, int[][] resTab, int start){
		
		int[] verwendeteResourcen = j.verwendeteResourcen;
		for(int k = 0; k < resTab.length; k++){
			for(int i = start; i < (start + j.dauer); i++){
				resTab[k][i] -= verwendeteResourcen[k];
			}
		}
	}

	@Override
	public int compareTo(Individual otherIndividual) {
		
		if (this.getFitness() < otherIndividual.getFitness()) {
	        return -1;
	    }
	    else if(this.getFitness() > otherIndividual.getFitness()){
	        return 1;
	    }

	    return 0;
		
	}
		
}
