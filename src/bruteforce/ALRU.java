package bruteforce;

import sim.*;
import sim.pages.*;

import java.util.*;

public class ALRU {
	int numberOfPages;
	int numberOfFrames;
	int simulationSize;

	LinkedList<Integer> requestQueue;

	ArrayList<ALRUPage> pageTable;
	ArrayList<Frame> frameTable;

	int framesUsed;

	int pageErrors;

	public ALRU(int numberOfPages, int numberOfFrames, int simulationSize) {
		this.numberOfPages = numberOfPages;
		this.numberOfFrames = numberOfFrames;
		this.simulationSize = simulationSize;

		pageTable = new ArrayList<>();
		for(int i = 0; i<numberOfPages; ++i) {
			pageTable.add(new ALRUPage(i, -1));
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
			sortPagesByIndex();
			sortFramesByIndex();

			ALRUPage requestedPage = pageTable.get(requestQueue.pollFirst());

			System.out.println("Requested page: " + requestedPage.getPageNumber());

			System.out.println("memory:");
			System.out.print("[");
			for(Frame frame: frameTable) {
				if(frame.getPageGiven() == null) {
					System.out.print("-\t");
				}
				else {
					System.out.print(frame.getPageGiven().getPageNumber() + ", ref: " + ((ALRUPage) frame.getPageGiven()).wasReferenced() + "\t");
				}

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
					sortFramesByReference();

					System.out.println("page to dump: " + frameTable.get(0).getPageGiven().getPageNumber());

					//usun poprzednie polaczenie!
					frameTable.get(0).getPageGiven().setFrameGiven(-1);
					((ALRUPage) (frameTable.get(0).getPageGiven())).setReferenced(false);
					--framesUsed;

				}

				//utworz nowe polaczenie!
				frameTable.get(0).setPageGiven(requestedPage);
				requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
				++framesUsed;
			}

			requestedPage.setReferenced(true);

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

	private void sortFramesByReference() {
		frameTable.sort((o1, o2) -> {
			ALRUPage page1 = ((ALRUPage) o1.getPageGiven());
			ALRUPage page2 = ((ALRUPage) o2.getPageGiven());

			if(page1.wasReferenced() == false) return -1;
			else if(page2.wasReferenced() == false) return 1;
			else return 0;

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
