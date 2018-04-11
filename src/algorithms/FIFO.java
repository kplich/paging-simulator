package algorithms;

import base.Simulator;
import sim.*;
import sim.pages.*;

public class FIFO extends Simulator<FIFOPage> {

	public FIFO(int numberOfPages, int numberOfFrames, int simulationSize) {
		super(numberOfPages, numberOfFrames, simulationSize);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new FIFOPage(i, -1));
		}
	}

	@Override
	public void prepare() {
		countTimeSinceAllocation(); //for each frame in memory, mark another tick since it was allocated

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
			sortFramesByTimeSinceAllocation();
		}

		//jesli w ramce znajdowala sie jakas strona, tzn. ze trzeba ja bylo usunac i mamy mniej uzytych ramek
		if(frameTable.get(0).getPageGiven().getPageNumber() != -1) --framesUsed;

		//usun poprzednie polaczenie!
		frameTable.get(0).getPageGiven().setFrameGiven(-1);
		frameTable.get(0).getPageGiven().setTimeSinceAllocation(0);
	}

	@Override
	public void allocatePage(FIFOPage requestedPage) {
		//utworz nowe polaczenie!
		frameTable.get(0).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(frameTable.get(0).getFrameIndex());

		++framesUsed;
	}

	@Override
	public void finish() {
		//nie trzeba nic robic na koncu petli
	}

	private void countTimeSinceAllocation() {
		for (Frame<FIFOPage> frame: frameTable) {
			if (frame.getPageGiven() != null) {
				frame.getPageGiven().countTimeSinceAllocation();
			}
		}
	}

	//po prostu przesun puste ramki na poczatek listy
	private void sortFramesByPageUsed() {
		frameTable.sort((o1, o2) -> {
			if (o1.getPageGiven() == null) return -1;
			else if (o2.getPageGiven() == null) return 1;
			else return 0;
		});
	}

	//sortowanie malejace, tak aby strona z najdluzszym czasem byla na pierwszym miejscu
	private void sortFramesByTimeSinceAllocation() {
		frameTable.sort((o1, o2) -> {
			int timeOfPage1 = ((FIFOPage) o1.getPageGiven()).getTimeSinceAllocation();
			int timeOfPage2 = ((FIFOPage) o2.getPageGiven()).getTimeSinceAllocation();

			return -Integer.compare(timeOfPage1, timeOfPage2);
		});
	}
}
