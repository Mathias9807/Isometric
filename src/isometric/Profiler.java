package isometric;

public class Profiler {
	
	private long timeSinceDawnOfTime = 0;
	
	public void startTimer() {
		timeSinceDawnOfTime = System.nanoTime() / 1000000;
	}
	
	public long timePassed() {
		return (System.nanoTime() / 1000000) - timeSinceDawnOfTime;
	}
	
	public String timePassedString() {
		return timePassed() + "ms";
	}
	
	public void print() {
		print("");
	}
	
	public void print(String prefix) {
		System.out.println(prefix + timePassed() + "ms");
	}
	
}
