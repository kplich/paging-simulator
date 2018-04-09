package bruteforce;

import sim.*;
import sim.pages.*;

import java.util.*;

public class LRU {
	int numberOfPages;
	int numberOfFrames;
	int simulationSize;

	LinkedList<Integer> requestQueue;

	ArrayList<LRUPage> pageTable;
	ArrayList<Frame> frameTable;

	int framesUsed;

	int pageErrors;

	public LRU(int numberOfPages, int numberOfFrames, int simulationSize) {
		this.numberOfPages = numberOfPages;
		this.numberOfFrames = numberOfFrames;
		this.simulationSize = simulationSize;

		pageTable = new ArrayList<>();
		for(int i = 0; i<numberOfPages; ++i) {
			pageTable.add(new LRUPage(i, -1));
		}

		frameTable = new ArrayList<>();
		for(int i = 0; i<numberOfFrames; ++i) {
			frameTable.add(new Frame(i, null));
		}

		framesUsed = 0;
	}

	public int run() {
		generateRequests();

		while(!requestQueue.isEmpty()) {
			countTimeSinceReference();

			sortPagesByIndex();
			sortFramesByIndex();

			Page requestedPage = pageTable.get(requestQueue.pollFirst());
			System.out.println("Requested page: " + requestedPage.getPageNumber());

			System.out.println("memory:");
			System.out.print("[");
			for(Frame frame: frameTable) {
				if(frame.getPageGiven() == null) {
					System.out.print("-\t");
				}
				else {
				System.out.print(frame.getPageGiven().getPageNumber() + "\t"); }

			}
			System.out.println("]");

			//sprawdzamy czy trzeba wczytac pozadana strone
			if(requestedPage.getFrameGiven() < 0) {
				++pageErrors; //rozwiazujemy kolejny blad strony

				//sprawdzamy czy sa jeszcze wolne ramki
				if(framesUsed < numberOfFrames) {
					sortFramesByPageUsed();
				}
				//jesli nie ma, wyrzucamy odpowiednia strone
				else {
					sortFramesByTimeSinceReference();

					System.out.println("page to dump: " + frameTable.get(0).getPageGiven().getPageNumber());
					System.out.println("time since last ref: " + ((LRUPage) (frameTable.get(0).getPageGiven())).getTimeSinceLastReference());

					//usun poprzednie polaczenie!
					frameTable.get(0).getPageGiven().setFrameGiven(-1);
					((LRUPage) (frameTable.get(0).getPageGiven())).setTimeSinceLastReference(0);
					--framesUsed;

				}

				//utworz nowe polaczenie!
				frameTable.get(0).setPageGiven(requestedPage);
				requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
				++framesUsed;
			}

			((LRUPage) requestedPage).setTimeSinceLastReference(0);

			System.out.println("-----");
		}

		return pageErrors;
	}

	private void generateRequests() {
		Random rng = new Random();

		requestQueue = new LinkedList<>();

		for (int i = 0; i < simulationSize; ++i) {
			requestQueue.add(rng.nextInt(numberOfPages));
		}
	}

	private void sortPagesByIndex() {
		pageTable.sort(Comparator.comparingInt(Page::getPageNumber));
	}

	private void sortFramesByIndex() {
		frameTable.sort(Comparator.comparingInt(Frame::getFrameIndex));
	}

	private void countTimeSinceReference() {
		for (Frame frame: frameTable) {
			if (frame.getPageGiven() != null) {
				((LRUPage) frame.getPageGiven()).countTimeSinceLastReference();
			}
		}
	}

	private void sortFramesByTimeSinceReference() {
		frameTable.sort((o1, o2) -> {
			int timeOfPage1 = ((LRUPage) o1.getPageGiven()).getTimeSinceLastReference();
			int timeOfPage2 = ((LRUPage) o2.getPageGiven()).getTimeSinceLastReference();

			return -Integer.compare(timeOfPage1, timeOfPage2);
		});
	}

	//po prostu przesun puste ramki na poczatek listy
	private void sortFramesByPageUsed() {
		frameTable.sort((o1, o2) -> {
			if(o1.getPageGiven() == null) return -1;
			else if(o2.getPageGiven() == null) return 1;
			else return 0;
		});
	}
}
