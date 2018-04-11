package algorithms;

import base.*;
import base.pages.*;

public class OPT extends Simulator<OPTPage> {
	public OPT(int numberOfPages, int numberOfFrames, int simulationSize) {
		super(numberOfPages, numberOfFrames, simulationSize);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new OPTPage(i, -1));
		}
	}

	@Override
	public void prepare() {
		markTime();
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
			sortFramesByTimeUntilReference();
		}

		//jesli w ramce znajdowala sie jakas strona to znaczy
		//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
		if (frameTable.get(0).getPageGiven() != null) {
			--framesUsed;

			//usun poprzednie polaczenie!
			frameTable.get(0).getPageGiven().setFrameGiven(-1);
		}
	}

	@Override
	public void allocatePage(OPTPage requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(0).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());
		measureTimeUntilNextRef(requestedPage);
		++framesUsed;
	}

	@Override
	public void whenPageWasLoaded(OPTPage requestedPage) {
		measureTimeUntilNextRef(requestedPage);
	}

	private void markTime() {
		for(Frame<OPTPage> frame: frameTable) {
			if(frame.getPageGiven() != null) {
				frame.getPageGiven().countTimeUntilNextReference();
			}
		}
	}

	private void measureTimeUntilNextRef(OPTPage requestedPage) {
		int result = 0;

		for(int i = 0; i<requestQueue.size(); ++i) {
			if(requestQueue.get(0) == requestedPage) {
				break;
			}
			else {
				++result;
			}
		}

		requestedPage.setTimeUntilNextReference(result);
	}

	private void sortFramesByTimeUntilReference() {
		frameTable.sort((o1, o2) -> {
			int timeUntilPage1 = o1.getPageGiven().getTimeUntilNextReference();
			int timeUntilPage2 = o2.getPageGiven().getTimeUntilNextReference();

			return -Integer.compare(timeUntilPage1, timeUntilPage2);
		});
	}
}

