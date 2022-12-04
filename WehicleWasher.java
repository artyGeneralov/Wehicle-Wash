import java.util.ArrayList;
import java.util.Random;

public class WehicleWasher {

	// TEST: delete later.
	final static double lambda1 = 1.5;
	final static double lambda2 = 3;
	final static int MAX_CARS = 10;
	final static int MAX_WASHERS = 4;

	// maybe needed???
	static int free_washers = MAX_WASHERS;
	static Queue<Wehicle> waiting_cars = new Queue<Wehicle>(Wehicle.class, MAX_CARS);
	static Wehicle[] in_process = new Wehicle[MAX_WASHERS];

	/*
	 * 
	 * number of washers - N number of cars - M (?) time1 time2
	 * 
	 * 
	 */

	public static void main(String[] args) {

		ArrayList<Car> washed_cars = new ArrayList<Car>();
		ArrayList<Car> washed_suvs = new ArrayList<Car>();
		ArrayList<Car> washed_trucks = new ArrayList<Car>();
		ArrayList<Car> washed_minibus = new ArrayList<Car>();

		String[] car_nos = { "1", "2", "3" };

		
		// TODO: move this VVV to seperate synchronized method.
		new Thread() {
			public void run() {
				for (String elem : car_nos) {

					Random rnd = new Random();
					Wehicle nextWehicle;
					int r = rnd.nextInt(4);
					switch (r) {
					case 0:
						// Car
						nextWehicle = new Car(elem, calculate_time(lambda2));
						break;
					case 1:
						// SUV
						nextWehicle = new SUV(elem, calculate_time(lambda2));
						break;
					case 2:
						// Truck
						nextWehicle = new Truck(elem, calculate_time(lambda2));
						break;
					default:
						// MiniBus
						nextWehicle = new MiniBus(elem, calculate_time(lambda2));
						break;
					}
					waiting_cars.enqueue(nextWehicle);
					System.out.println();
					try {
						Thread.sleep((long) calculate_time(lambda1));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		/*waiting_cars.enqueue(new Car("1-2-3", calculate_time(lambda2)));
		waiting_cars.enqueue(new SUV("4-5-6", calculate_time(lambda2)));
		waiting_cars.enqueue(new Truck("7-8-9", calculate_time(lambda2)));
		waiting_cars.enqueue(new Car("10-11-12", calculate_time(lambda2)));
		waiting_cars.enqueue(new Car("13-14-15", calculate_time(lambda2)));
		waiting_cars.enqueue(new SUV("16-17-18", calculate_time(lambda2)));
		waiting_cars.enqueue(new MiniBus("19-20-21", calculate_time(lambda2)));
		waiting_cars.enqueue(new Truck("22-23-24", calculate_time(lambda2)));
		waiting_cars.enqueue(new Car("25-26-27", calculate_time(lambda2)));*/

		while (true) {
			WehicleWasher ws = new WehicleWasher();
			try {
				ws.addWehicleTo_InProcess(waiting_cars.dequeue());
				for (int i = 0; i < in_process.length; i++)
					if (in_process[i] != null)
						if (!in_process[i].isAlive())
							ws.removeWehicleFrom_InProcess(i);
			} catch (Exception e) {
				break;
			}
		}
	}

	private synchronized void addWehicleTo_InProcess(Wehicle w) {
		// TODO: change, so that method knows that wehicle array is not empty, wait for new wehicle to come in
		try {
			if (hasEmpty(in_process) == -1)
				this.wait();
		} catch (InterruptedException e) {
		}
		in_process[hasEmpty(in_process)] = w;
		System.out.printf("%s Added\n\n", w.toString());
		w.start();
	}

	private synchronized Wehicle removeWehicleFrom_InProcess(int pos) {
		Wehicle w = in_process[pos];
		in_process[pos] = null;
		notifyAll();
		System.out.printf("%s Removed\n\n", w.toString());
		return w;
	}

	// returns -1 if exists an array is full
	// if array has empty space, return an empty space
	private static <T> int hasEmpty(T[] arr) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == null)
				return i;
		return -1;
	}

	// u = 0.2 lambda = 3
	// nextTime = 0.536
	// u = 0.2 lambda = 1.5
	// nextTime = 1.07
	public static double calculate_time(double lambda) {
		float f = (float) -(Math.log(Math.random()) / lambda);
		return (double) f;
	}
	public void method1() {
		new Thread() {
			//...
			// sleep
		}.start();
	}
}
