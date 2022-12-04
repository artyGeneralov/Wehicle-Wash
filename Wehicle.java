
public abstract class Wehicle extends Thread {

	private String plate_number;
	private double washing_time;

	public Wehicle(String plate_number, double washing_time) {
		this.plate_number = plate_number;
		this.washing_time = washing_time;
	}

	
	@Override
	public void run(){
		// runnable
		try {
			System.out.printf("I'm %s, sleeping: %f\n",plate_number, washing_time);
			Thread.sleep((long) washing_time);
		} catch (InterruptedException e) {
			System.err.println("Interrupted in Wehicle Class" + e);
		}
	}
	
	public String toString() {
		String s = String.format("Wehicle, no' %s, washing time %f", this.plate_number, this.washing_time);
		return s;
	}
}
