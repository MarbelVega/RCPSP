
public class Solution {

	private int criticalPath;
	private int fitness;

	public Solution() {
	}

	public void setCriticalPathFitness(int criticalPath) {
		this.criticalPath = criticalPath;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public double getDeviation() {
		return (fitness - criticalPath) / criticalPath;
	}
	
	public int getFitness(){
		return fitness;
	}
	
	public int getCriticalPath(){
		return criticalPath;
	}
}
