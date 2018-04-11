package algorithms;

import base.*;
import base.pages.*;

public class ALRU extends Simulator<ALRUPage> {
	int chosenIndex;

	public ALRU(int numberOfPages, int numberOfFrames, int simulationSize, double threshold) {
		super(numberOfPages, numberOfFrames, simulationSize, threshold);

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
		 chosenIndex = 0;

		//sprawdzamy czy sa jeszcze wolne ramki
		if (framesUsed < numberOfFrames) {
			sortFramesByPageUsed();
		}
		//jesli nie ma, wyrzucamy odpowiednia strone
		else {
			chosenIndex = indexToDump();
		}

		//jesli w ramce znajdowala sie jakas strona to znaczy
		//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
		if (frameTable.get(chosenIndex).getPageGiven() != null) {
			--framesUsed;

			//usun poprzednie polaczenie!
			frameTable.get(chosenIndex).getPageGiven().setFrameGiven(-1);
			frameTable.get(chosenIndex).getPageGiven().setReferenced(false);
		}
	}

	@Override
	public void allocatePage(ALRUPage requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(chosenIndex).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(chosenIndex).getFrameIndex());
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

	private int indexToDump() {
		for(int i = 0; i<frameTable.size(); ++i) {
			if(frameTable.get(i).getPageGiven().wasReferenced()) {
				frameTable.get(i).getPageGiven().setReferenced(false);
			}
			else return i;
		}
		return 0;
	}
}
