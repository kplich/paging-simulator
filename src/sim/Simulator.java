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

	//TODO: is it apparently going to be unnecessary?
	/**
	 * This table implements the physical memory - each cell of a table represents a frame in memory.
	 * Each frame contains the number of a page it's holding in its memory. If a frame isn't used, it contains a -1.
	 */
	@ToBeDeleted
	protected int[] physicalMemory; //TODO: lepiej przedstawic jako ArrayList<Integer, Integer>?

	/**
	 * Size of physical memory available - number of frames.
	 */
	protected int frames;

	/**
	 * Number of frames used. Most of the time equal to the size of physical memory, smaller only at the beginning of
	 * the simulation
	 */
	protected int framesUsed;

	@ToBeDeleted
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

	@ToBeDeleted
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
		pageTableOld = new ArrayList<>();
		for(int i = 0; i < pages; ++i) {
			pageTableOld.add(new Page(i, -1));
		}

		this.frames = frames;
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
			accessPage(requestQueue.peekFirst());
		}
	}

	/**
	 * Here we try to access a given page.
	 * If it's not in the physical memory, we load it and dump one of the pages already in memory, if necessary.
	 * @param page number of a page we try to access
	 */
	private void accessPage(int page) {
		if(!isLoaded(page)) {
			int destination = nextFreeFrame(); //we assume there's some space left for a given page

			if (destination < 0) { //if there isn't, we look for a new space
				destination = frameToDump();
				--framesUsed; //now that's what I call awkward
				++frameMissCount;
			}

			physicalMemory[destination] = page; //ultimately, there was some space for our page
			++framesUsed;
		}
	}

	/**
	 * Simple check if a page is loaded to memory.
	 * @param page number of a page to be checked
	 * @return true, if the page is in memory, false otherwise.
	 */
	private boolean isLoaded(int page) {
		//TODO: make sure the page table is sorted by indices.
		return true; //TODO
	}

	/**
	 * Checks if there's a free frame in physical memory.
	 * @return index of a first free frame if it exists, or -1 if there's no available memory.
	 */
	private int nextFreeFrame() {
		if(framesUsed < physicalMemory.length) {
			for(int i = 0; i<physicalMemory.length; ++i) {
				if(physicalMemory[i] == -1) return i;
			}
		}

		return -1;
	}

	/**
	 * Abstract method implementing a paging algorithm.
	 * @return an index of a frame that can be deallocated in a given moment
	 */
	abstract int frameToDump();
}
