import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WehicleLogger {

	private final static String DEAFULT_FILE_NAME = "log.txt";
	private boolean writable = false;
	private File dest;
	private String current_file_name;

	public WehicleLogger() {

	}

	public WehicleLogger(boolean createOutputFile) {
		this(true, DEAFULT_FILE_NAME);
	}

	public WehicleLogger(boolean createOutputFile, String fileName) {
		dest = new File(fileName);
		writable = true;
		current_file_name = fileName;
	}

	public <T> void printAvgTime(ArrayList<T> arr) {
		double sum = 0;
		int c = 0;
		for (Object e : arr) {
			sum += ((Wehicle) e).getWashingTime();
			c++;
		}
		sum /= c;
		if (arr.size() != 0)
			log(Double.toString(sum) + " " + arr.get(0).getClass().getSimpleName());
	}
	
	
	// NANO-TIME!
	public String timeStamp(long time) {
		int hours = (int)((time / 1000000 / 1000 / 60 / 60) % 24);
		int minutes = (int)((time / 1000000 / 1000 / 60) % 60);
		int seconds = (int)((time / 1000000 / 1000) % 60);
		int millis = (int)((time / 1000000) % 1000) ;
		int nanos = (int)(time % 1000000);
		
		String t = String.format("%02d:%02d:%02d:%02d:%d",hours, minutes, seconds, millis, nanos);
		return t;
	}

	public synchronized void printWehicleDataTime(Wehicle w, long time) {
		String s = String.format("%s %s", timeStamp(time_elapsed(time)), formatWehicle(w));
		log(s);
		if (writable)
			writeLogToFile(s);
	}

	public synchronized void writeLogToFile(String s) {
		if (writable == true)
			try {
				FileWriter out = new FileWriter(dest, true);
				out.write(s + "\n");
				out.close();
			} catch (IOException e) {
				System.out.println("Could not write to file " + current_file_name + "\n");
			}
	}

	public long time_elapsed(long time) {
		return System.nanoTime() - time;
	}

	public String formatWehicle(Wehicle w) {
		return String.format("Wehicle no': %s, of type: %s", w.getPlateNumber(), w.getClass().getSimpleName());
	}

	public synchronized void log(String s) {
		System.out.println(s);
	}
}
