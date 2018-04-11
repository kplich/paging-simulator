package base;

import sim.*;
import sim.pages.*;

import java.util.*;

public abstract class Simulator<P extends Page> {
	protected int numberOfPages;
	protected int numberOfFrames;
	protected int simulationSize;

	protected LinkedList<P> requestQueue;

	protected ArrayList<P> pageTable;
	protected ArrayList<Frame<P>> frameTable;

	protected int framesUsed;

	protected int pageErrors;

	public Simulator(int numberOfPages, int numberOfFrames, int simulationSize) {
		this.numberOfPages = numberOfPages;
		this.numberOfFrames = numberOfFrames;
		this.simulationSize = simulationSize;

		frameTable = new ArrayList<>();
		for (int i = 0; i < numberOfFrames; ++i) {
			frameTable.add(new Frame<>(i, null));
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

	private void printOut() {
		System.out.println("memory:");
		System.out.print("[");
		for (Frame frame : frameTable) {
			if (frame.getPageGiven() == null) {
				System.out.print("-\t");
			}
			else {
				System.out.print(frame.getPageGiven().getPageNumber() + "\t");
			}

		}
		System.out.println("]");
	}

	protected void sortPagesByIndex() {
		pageTable.sort(Comparator.comparingInt(Page::getPageNumber));
	}

	protected void sortFramesByIndex() {
		frameTable.sort(Comparator.comparingInt(Frame::getFrameIndex));
	}

	public int run() {
		sortPagesByIndex();
		sortFramesByIndex();
		generateRequests();

		while(!requestQueue.isEmpty()) {
			dealWithPage(requestQueue.getFirst());
		}

		return pageErrors;
	}

	private void dealWithPage(P requestedPage) {
		prepare(); //count some time/reference, etc.

		printOut(); //print out simulation details

		if(requestedPage.getFrameGiven() < 0) {//if the page isn't loaded into memory
			++pageErrors; //we have to deal with a page error

			freeUpSomeMemory();

			allocatePage(requestedPage);

			//aaaand... that's it for today!
		}

		finish();
	}

	public abstract void prepare();

	public abstract void freeUpSomeMemory();

	public abstract void allocatePage(P requestedPage);

	public abstract void finish();
}