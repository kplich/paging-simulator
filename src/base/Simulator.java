package base;

import sim.*;
import sim.pages.*;

import java.util.*;

public abstract class Simulator {
	private int numberOfPages;
	private int numberOfFrames;
	private int simulationSize;

	private LinkedList<Page> requestQueue;

	private ArrayList<Page> pageTable;
	private ArrayList<Frame> frameTable;

	private int framesUsed;

	private int pageErrors;

	public Simulator(int numberOfPages, int numberOfFrames, int simulationSize) {
		this.numberOfPages = numberOfPages;
		this.numberOfFrames = numberOfFrames;
		this.simulationSize = simulationSize;

		pageTable = new ArrayList<>();
		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new FIFOPage(i, -1));
		}

		frameTable = new ArrayList<>();
		for (int i = 0; i < numberOfFrames; ++i) {
			frameTable.add(new Frame(i, null));
		}

		framesUsed = 0;
	}

	private void generateRequests() {
		Random rng = new Random();

		requestQueue = new LinkedList<>();

		for (int i = 0; i < simulationSize; ++i) {
			requestQueue.add(pageTable.get(rng.nextInt(numberOfPages)));
		}
	}

	private void sortPagesByIndex() {
		pageTable.sort(Comparator.comparingInt(Page::getPageNumber));
	}

	private void sortFramesByIndex() {
		frameTable.sort(Comparator.comparingInt(Frame::getFrameIndex));
	}

	public void run() {
		sortPagesByIndex();
		sortFramesByIndex();
		generateRequests();

		while(!requestQueue.isEmpty()) {
			dealWithPage(requestQueue.getFirst());
		}
	}

	public void dealWithPage(Page requestedPage) {
		prepare(); //count some time/reference, etc.

		printOut(); //print out simulation details

		if(requestedPage.getFrameGiven() < 0) {//if the page isn't loaded into memory
			++pageErrors; //we have to deal with a page error

			//aaaand... that's it for today!
		}
	}

	public abstract void prepare();

	public abstract void printOut();


}
