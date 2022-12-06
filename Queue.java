import java.lang.reflect.Array;

public class Queue<T> {

	private int front; // postitions
	private int back;
	private int count; // how many?
	private int max_size;
	private T[] queue_arr;
	private Class<T> c;

	@SuppressWarnings("unchecked")
	public Queue(Class<T> c, int max_size) {
		queue_arr = (T[]) Array.newInstance(c, max_size);
		this.count = 0;
		this.front = this.back = 0;
		this.max_size = max_size;
		this.c = c;
	}

	public int getSize() {
		return this.count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public synchronized void enqueue(T item) {
		if(this.count == this.max_size)
		{
			// enlarge array
			// front and back one after the other, always!!
			int newMax = max_size * 2;
			@SuppressWarnings("unchecked")
			T[] temp_arr = (T[]) Array.newInstance(c, newMax);
			int i = front;
			int j = 0;
			do {
				temp_arr[j] = queue_arr[i];
				j++;
				i = (i+1) % (max_size); //old max_size
			}while(i != front);
			this.back = j;
			this.front = 0;
			this.max_size = newMax;
			this.queue_arr = temp_arr;
		}
		queue_arr[back] = item;
		back++;
		back %= max_size;
		count++;
	}

	public synchronized T dequeue() throws EmptyListException
	{
		if (isEmpty())
			throw new EmptyListException();
		T item = queue_arr[front];
		queue_arr[front] = null;
		front = (front + 1) % (max_size);
		count--;
		return item;
	}
	
	public String toString() {
		String s = "";
		for(T e: queue_arr) {
			if(e == null)
				s+="_";
			else
				s+=e.toString();
			s+=", ";
		}
		s = s.substring(0,s.length() - 2);
		return s;
	}
	
}
