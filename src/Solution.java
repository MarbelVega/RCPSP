
public class Solution {

	private int criticalPath;
	private int fitness;
	private int cycles;
	private int populationSize;
	private String fittestIndividualUID;
	private boolean foundBestSolution = false;

	public String getFittestIndividualUID() {
		return fittestIndividualUID;
	}

	public void setFittestIndividualUID(String fittestIndividualUID) {
		this.fittestIndividualUID = fittestIndividualUID;
	}

	public boolean hasFoundBestSolution() {
		return foundBestSolution;
	}

	public void setFoundBestSolution(boolean foundBestSolution) {
		this.foundBestSolution = foundBestSolution;
	}

	public Solution() {
	}

	public void setCriticalPathFitness(int criticalPath) {
		this.criticalPath = criticalPath;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}

	public int getPopulation() {
		return populationSize;
	}

	public void setPopulation(int population) {
		this.populationSize = population;
	}

	/**
	 * Calculate deviation. fitness and criticalPath have to be set before!
	 * @return
	 */
	public double getDeviation() {
		if (criticalPath == 0){
			return 0.;
		}
		return (double)(fitness - criticalPath) / criticalPath;
	}
	
	public int getFitness(){
		return fitness;
	}
	
	public int getCriticalPath(){
		return criticalPath;
	}
}
