
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
