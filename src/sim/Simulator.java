package sim;

import java.util.*;

/**
 * An abstract class providing a base for each paging algorithm.
 */
public abstract class Simulator {
	/**
	 * Random number generator, used mainly to generate requests.
	 */
	Random rng = new Random();

	/**
	 * Queue containing all the page requests.
	 */
	protected LinkedList<Integer> requestQueue;

	/**
	 * This table implements the physical memory - each cell of a table represents a frame in memory.
	 * Each frame contains the number of a page it's holding in its memory. If a frame isn't used, it contains a -1.
	 */
	protected int[] physicalMemory;

	/**
	 * Number of frames used. Most of the time equal to the size of physical memory, smaller only at the beginning of
	 * the simulation
	 */
	protected int framesUsed;

	/**
	 * I suppose this variable is meant to indicate the size of virtual memory -
	 * - the number of pages that can be allocated.
	 */
	protected int virtualMemory;

	/**
	 * This variable holds a number of times a page wasn't found in physical memory and had to be loaded from VM.
	 */
	protected int frameMissCount;

	/**
	 * Constructor, lol.
	 * @param pages size of virtual memory - number of pages possible to allocate.
	 * @param frames size of physical memory - number of frames to hold pages.
	 */
	public Simulator(int pages, int frames) {
		virtualMemory = pages;
		physicalMemory = new int[frames];
		Arrays.fill(physicalMemory, -1); //no pages allocated yet
	}

	/**
	 * Generates a queue of requests to memory
	 * @param size number of requests to be generated
	 */
	private void generateRequests(int size) {
		requestQueue = new LinkedList<>();

		for(int i = 0; i<size; ++i) {
			requestQueue.add(rng.nextInt(virtualMemory) + 1);
		}
	}

	/**
	 * This method runs the simulation - on each loop run attempts to access a given page.
	 * @param simulationSize numer of pages to be accessed in a simulation
	 */
	public void run(int simulationSize) {
		generateRequests(simulationSize);

		while(!requestQueue.isEmpty()) {
			accessPage();
		}
	}

	private void accessPage() {

	}
}
