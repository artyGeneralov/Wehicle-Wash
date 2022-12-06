import java.util.ArrayList;
import java.util.Random;

public class WehicleWasher {

	// TEST: delete later.
	final static double lambda1 = 0.15;
	final static double lambda2 = 0.3;
	final static int MAX_CARS = 10;
	final static int MAX_WASHERS = 4;

	static int free_washers = MAX_WASHERS;
	static Queue<Wehicle> waiting_cars = new Queue<Wehicle>(Wehicle.class, MAX_CARS);
	static Wehicle[] in_process = new Wehicle[MAX_WASHERS];


	public static void main(String[] args) {
		int wehicles_processed = 0;
		final long start_time = System.nanoTime();
		final Object lock = new Object();
		WehicleLogger main_logger = new WehicleLogger(true);
		ArrayList<Car> washed_cars = new ArrayList<Car>();
		ArrayList<SUV> washed_suvs = new ArrayList<SUV>();
		ArrayList<Truck> washed_trucks = new ArrayList<Truck>();
		ArrayList<MiniBus> washed_minibus = new ArrayList<MiniBus>();

		final String[] car_nos = { "1", "2", "3", "4", "5", "6"};
		Thread thread1 = incomingCars(car_nos);

		main_logger.writeLogToFile(main_logger.timeStamp(System.nanoTime()));
		while (true) {
			WehicleWasher ws = new WehicleWasher();
			try {
				for (int i = 0; i < in_process.length; i++)
					if (in_process[i] != null)
						if (!in_process[i].isAlive()) {
							Wehicle removed = ws.removeWehicleFrom_InProcess(i);
							switch (removed.getClass().getSimpleName()) {
							case "Car":
								washed_cars.add((Car) removed);
								break;
							case "SUV":
								washed_suvs.add((SUV) removed);
								break;
							case "Truck":
								washed_trucks.add((Truck) removed);
								break;
							default:
								washed_minibus.add((MiniBus) removed);
								break;
							}
							main_logger.printWehicleDataTime(removed, start_time);
							synchronized (lock) {
								wehicles_processed++;
							}
						}
				Wehicle toAdd = waiting_cars.dequeue();
				ws.addWehicleTo_InProcess(toAdd);
			} catch (EmptyListException e) {
				if (wehicles_processed == car_nos.length) {
					main_logger.printAvgTime(washed_cars);
					main_logger.printAvgTime(washed_suvs);
					main_logger.printAvgTime(washed_trucks);
					main_logger.printAvgTime(washed_minibus);
					break;
				}
			}
		}

	}

	private synchronized void addWehicleTo_InProcess(Wehicle w) {
		while (hasEmpty(in_process) == -1) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		in_process[hasEmpty(in_process)] = w;
		WehicleLogger lg = new WehicleLogger();
		lg.log(w.toString() + " has been added to washing station");
		w.start();
	}

	private synchronized Wehicle removeWehicleFrom_InProcess(int pos) {
		Wehicle w = in_process[pos];
		in_process[pos] = null;
		try {
			notifyAll();
		} catch (Exception e) {
		}
		WehicleLogger lg = new WehicleLogger();
		lg.log(w.toString() + " has left its station");
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

	private static Thread incomingCars(String[] cars) {
		Thread t1 = new Thread() {
			public void run() {
				for (String elem : cars) {
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
					WehicleLogger lg = new WehicleLogger();
					lg.log(nextWehicle.toString() + " has been added to waiting queue");
					try {
						Thread.sleep((long) calculate_time(lambda1));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t1.start();
		return t1;

	}
}
