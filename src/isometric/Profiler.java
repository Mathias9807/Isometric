package isometric;

/**
 * A timer used to profile performance. 
 * 
 * @author k2
 *
 */

public class Profiler {
	
	/**
	 * The system time when startTimer() was last called in milliseconds. 
	 */
	private long timeSinceDawnOfTime = 0;
	
	/**
	 * Start the base timer. Will override previous calls to this method. 
	 */
	public void startTimer() {
		timeSinceDawnOfTime = System.nanoTime() / 1000000;
	}
	
	/**
	 * Returns the time passed since last call of startTimer() in milliseconds as a long. 
	 * 
	 * @return Time recorded
	 */
	
	public long timePassed() {
		return (System.nanoTime() / 1000000) - timeSinceDawnOfTime;
	}
	
	/**
	 * Returns the time passed since startTimer() was called as a String. 
	 * 
	 * @return String of time recorded. 
	 */
	
	public String timePassedString() {
		return timePassed() + "ms";
	}
	
	/**
	 * Prints the current time passed to the standard output stream. 
	 * 
	 * Calling this method is equal to calling print(""). 
	 * 
	 * @param prefix
	 */
	
	public void print() {
		print("");
	}
	
	/**
	 * Prints the current time passed to the standard output stream. 
	 * 
	 * Begins output string with prefix. 
	 * 
	 * @param prefix
	 */
	
	public void print(String prefix) {
		System.out.println(prefix + timePassed() + "ms");
	}
	
}
