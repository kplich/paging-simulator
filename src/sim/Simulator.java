package sim;

import sim.pages.*;

import java.util.*;

/**
 * An abstract class providing a base for each paging algorithm.
 */
public abstract class Simulator<T extends Page> {
	/**
	 * Random number generator, used mainly to generate requests.
	 */
	//TODO: might be local?
	Random rng = new Random();

	/**
	 * Queue containing all the page requests.
	 */
	protected LinkedList<Integer> requestQueue;

	//TODO: is it apparently going to be unnecessary?
	/**
	 * This table implements the physical memory - each cell of a table represents a frame in memory.
	 * Each frame contains the number of a page it's holding in its memory. If a frame isn't used, it contains a -1.
	 */
	@Deprecated
	protected int[] physicalMemory; //TODO: lepiej przedstawic jako ArrayList<Integer, Integer>?

	/**
	 * Size of physical memory available - number of frames.
	 */
	protected int framesAvailable;

	/**
	 * Number of frames used. Most of the time equal to the size of physical memory, smaller only at the beginning of
	 * the simulation
	 */
	protected int framesUsed;

	@Deprecated
	protected ArrayList<Page> pageTableOld;

	protected PageTable pageTable;

	/**
	 * I suppose this variable is meant to indicate the size of virtual memory -
	 * - the number of pages that can be allocated.
	 */
	protected int virtualMemory;

	/**
	 * This variable holds a number of times a page wasn't found in physical memory and had to be loaded from VM.
	 */
	protected int frameMissCount;

	@Deprecated
	/**
	 * Constructor, lol.
	 * @param pages size of virtual memory - number of pages possible to allocate.
	 * @param frames size of physical memory - number of frames to hold pages.
	 */
	public Simulator(int pagesOld, int framesOld, int shit) {
		virtualMemory = pagesOld;
		physicalMemory = new int[framesOld];
		Arrays.fill(physicalMemory, -1); //no pages allocated yet
	}

	public Simulator(int pages, int frames) {
		pageTable = new PageTable(pages, frames);

		framesAvailable = frames;
	}

	/**
	 * Generates a queue of requests to memory
	 * @param size number of requests to be generated
	 */
	private void generateRequests(int size) {

	}

	/**
	 * This method runs the simulation - on each loop run attempts to access a given page.
	 * @param simulationSize numer of pages to be accessed in a simulation
	 */
	public void run(int simulationSize) {
		generateRequests(simulationSize);

		while(!requestQueue.isEmpty()) {
			accessPage(requestQueue.peekFirst());
		}
	}

	/**
	 * Here we try to access a given page.
	 * If it's not in the physical memory, we load it and dump one of the pages already in memory, if necessary.
	 * @param page number of a page we try to access
	 */
	private void accessPage(int page) {
		if(!pageTable.isLoaded(page)) {
			if(framesUsed == framesAvailable) {
				//all the frames are allocated, so you need to dump one of them
			}
			else {
				//there's a free frame to allocate
				pageTable.sortByAscendingFrame();
			}
		}
	}

	/**
	 * Abstract method implementing a paging algorithm.
	 * @return an index of a frame that can be deallocated in a given moment
	 */
	abstract int frameToDump();
}
