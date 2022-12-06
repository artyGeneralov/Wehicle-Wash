
public class EmptyListException extends Exception {
	
	public EmptyListException() {
		this("Error");
	}
	
	public EmptyListException(String msg) {
		//System.err.println("Empty List Error, " + msg);
	}
	
}
