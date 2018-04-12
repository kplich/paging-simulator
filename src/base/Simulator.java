package base;

import base.pages.*;

import java.util.*;

public abstract class Simulator<P extends Page> {
	protected int numberOfPages;
	protected int numberOfFrames;
	protected int simulationSize;
	private double threshold;

	protected LinkedList<P> requestQueue;

	protected ArrayList<P> pageTable;
	protected ArrayList<Frame<P>> frameTable;

	protected int framesUsed;

	protected int pageErrors;

	public Simulator(int numberOfPages, int numberOfFrames, int simulationSize, double threshold) {
		this.numberOfPages = numberOfPages;
		this.numberOfFrames = numberOfFrames;
		this.simulationSize = simulationSize;
		this.threshold = threshold;

		pageTable = new ArrayList<>(); //not initialized here because of generics

		frameTable = new ArrayList<>();
		for (int i = 0; i < numberOfFrames; ++i) {
			frameTable.add(new Frame<>(i, null));
		}

		framesUsed = 0;
		//generateRequests();
	}

	private void generateRequests() {
		Random rng = new Random();

		requestQueue = new LinkedList<>();

		while(requestQueue.size() < simulationSize) {
			double chance = rng.nextDouble();

			try {
				if(chance < threshold) {
					requestQueue.add(pageTable.get(rng.nextInt(numberOfPages)));
				}
				else if (chance < threshold + ((1-threshold)/2)) {
					requestQueue.add(pageTable.get(requestQueue.peek().getPageNumber() - (int) rng.nextGaussian() * numberOfPages));
				}
				else {
					requestQueue.add(pageTable.get(requestQueue.peek().getPageNumber() +
												   (int) rng.nextGaussian() * numberOfPages));
				}
			}
			catch (IndexOutOfBoundsException | NullPointerException e) {
				//System.err.println("lolz"); ignore
			}

		}
	}

	private void printOut(P requestedPage) {
		System.out.println("number of page errors: " + pageErrors + "\n");
		System.out.println("requested page number: " + requestedPage.getPageNumber());
		System.out.println("requested page's frame: " + requestedPage.getFrameGiven());

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
		System.out.println("]\n------------------\n");
	}

	protected void sortPagesByIndex() {
		pageTable.sort(Comparator.comparingInt(Page::getPageNumber));
	}

	protected void sortFramesByIndex() {
		frameTable.sort(Comparator.comparingInt(Frame::getFrameIndex));
	}

	//po prostu przesun puste ramki na poczatek listy
	protected void sortFramesByPageUsed() {
		frameTable.sort((o1, o2) -> {
			if (o1.getPageGiven() == null) return -1;
			else if (o2.getPageGiven() == null) return 1;
			else return 0;
		});
	}

	public int run() {
		sortPagesByIndex();
		sortFramesByIndex();
		generateRequests();

		while(!requestQueue.isEmpty()) {
			dealWithPage(requestQueue.poll());
		}

		int tempPageErrors = pageErrors;
		pageErrors = 0;

		return tempPageErrors;
	}

	private void dealWithPage(P requestedPage) {
		prepare(); //count some time/reference, etc.

		//printOut(requestedPage); //print out simulation details

		if(requestedPage.getFrameGiven() < 0) {//if the page isn't loaded into memory
			++pageErrors; //we have to deal with a page error

			freeUpSomeMemory();

			allocatePage(requestedPage);

			//aaaand... that's it for today!
		}
		else {
			whenPageWasLoaded(requestedPage);
		}
	}

	public abstract void prepare();

	public abstract void freeUpSomeMemory();

	public abstract void allocatePage(P requestedPage);

	public abstract void whenPageWasLoaded(P requestedPage);

	public void setRequestQueue(LinkedList<P> requestQueue) {
		this.requestQueue = requestQueue;
	}

	public LinkedList<P> getRequestQueue() {
		return requestQueue;
	}
}
