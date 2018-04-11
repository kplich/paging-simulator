package algorithms;

import base.*;
import base.pages.*;

public class ALRU extends Simulator<ALRUPage> {
	public ALRU(int numberOfPages, int numberOfFrames, int simulationSize) {
		super(numberOfPages, numberOfFrames, simulationSize);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new ALRUPage(i, -1));
		}
	}

	@Override
	public void prepare() {
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
			sortFramesByReference();
			//System.out.println("page to dump: " + frameTable.get(0).getPageGiven().getPageNumber());
		}

		//jesli w ramce znajdowala sie jakas strona to znaczy
		//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
		if (frameTable.get(0).getPageGiven() != null) {
			--framesUsed;

			//usun poprzednie polaczenie!
			frameTable.get(0).getPageGiven().setFrameGiven(-1);
			frameTable.get(0).getPageGiven().setReferenced(false);
		}
	}

	@Override
	public void allocatePage(ALRUPage requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(0).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
		requestedPage.setReferenced(true);

		++framesUsed;
	}

	@Override
	public void whenPageWasLoaded(ALRUPage requestedPage) {
		requestedPage.setReferenced(true);
	}

	private void sortFramesByReference() {
		frameTable.sort((o1, o2) -> {
			ALRUPage page1 = ((ALRUPage) o1.getPageGiven());
			ALRUPage page2 = ((ALRUPage) o2.getPageGiven());

			if (page1.wasReferenced() == false) return -1;
			else if (page2.wasReferenced() == false) return 1;
			else return 0;

		});
	}
}
