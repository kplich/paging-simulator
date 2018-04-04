package sim;

import java.util.*;

public abstract class Simulator {
	Random generator = new Random();

	LinkedList<Integer> requestQueue;
	int[] physicalMemory;
	int virtualMemory;

	public Simulator(int simulationSize, int pages, int frames) {
		virtualMemory = pages;
		physicalMemory = new int[frames];
		generateRequests(simulationSize);
	}

	private void generateRequests(int size) {
		requestQueue = new LinkedList<>();

		for(int i = 0; i<size; ++i) {
			requestQueue.add(generator.nextInt(virtualMemory) + 1);
		}
	}

	public void run() {
		while(!requestQueue.isEmpty()) {
			allocatePage();
		}
	}

	protected abstract void allocatePage();
}
