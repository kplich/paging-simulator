package algorithms;

import base.*;
import base.Simulator;
import base.pages.*;

public class LRU extends Simulator<LRUPage> {
	public LRU(int numberOfPages, int numberOfFrames, int simulationSize) {
		super(numberOfPages, numberOfFrames, simulationSize);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new LRUPage(i, -1));
		}
	}

	@Override
	public void prepare() {
		countTimeSinceReference();
		sortPagesByIndex();
		sortFramesByIndex();
	}

	@Override
	public void freeUpSomeMemory() {
		//sprawdzamy czy sa jeszcze wolne ramki
		if (framesUsed < numberOfFrames) {
			sortFramesByPageUsed();
		}
		//jesli nie ma, wyrzucamy odpowiednia strone
		else {
			sortFramesByTimeSinceReference();

			//System.out.println("page to dump: " + frameTable.get(0).getPageGiven().getPageNumber());
			//System.out.println("time since last ref: " + ((LRUPage) (frameTable.get(0).getPageGiven())).getTimeSinceLastReference());
		}

		//jesli w ramce znajdowala sie jakas strona to znaczy
		//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
		if (frameTable.get(0).getPageGiven().getPageNumber() != -1) --framesUsed;

		//usun poprzednie polaczenie!
		frameTable.get(0).getPageGiven().setFrameGiven(-1);
		frameTable.get(0).getPageGiven().setTimeSinceLastReference(0);
	}

	@Override
	public void allocatePage(LRUPage requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(0).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
		requestedPage.setTimeSinceLastReference(0);
		++framesUsed;
	}

	@Override
	public void finish() {
		//whoops, i'm stupid
	}

	private void countTimeSinceReference() {
		for (Frame<LRUPage> frame: frameTable) {
			if (frame.getPageGiven() != null) {
				frame.getPageGiven().countTimeSinceLastReference();
			}
		}
	}

	private void sortFramesByTimeSinceReference() {
		frameTable.sort((o1, o2) -> {
			int timeOfPage1 = o1.getPageGiven().getTimeSinceLastReference();
			int timeOfPage2 = o2.getPageGiven().getTimeSinceLastReference();

			return -Integer.compare(timeOfPage1, timeOfPage2);
		});
	}

	//po prostu przesun puste ramki na poczatek listy
	private void sortFramesByPageUsed() {
		frameTable.sort((o1, o2) -> {
			if (o1.getPageGiven() == null) return -1;
			else if (o2.getPageGiven() == null) return 1;
			else return 0;
		});
	}
}
