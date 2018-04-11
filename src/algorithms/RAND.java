package algorithms;

import base.*;
import base.pages.*;

import java.util.*;

public class RAND extends Simulator<Page> {
	public RAND(int numberOfPages, int numberOfFrames, int simulationSize) {
		super(numberOfPages, numberOfFrames, simulationSize);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new Page(i, -1));
		}
	}

	@Override
	public void prepare() {
		sortPagesByIndex();
		sortFramesByIndex();
	}

	@Override
	public void freeUpSomeMemory() {

	}

	@Override
	public void allocatePage(Page requestedPage) {
		if(requestedPage.getFrameGiven() < 0) {
			Random rng = new Random();

			int randomIndex = rng.nextInt(numberOfFrames); //wybieramy losowo ramke

			//jesli w ramce znajdowala sie jakas strona to znaczy,
			//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
			if (frameTable.get(randomIndex).getPageGiven().getPageNumber() != -1) --framesUsed;

			//usun poprzednie polaczenie!
			frameTable.get(randomIndex).getPageGiven().setFrameGiven(-1);

			//utworz nowe polaczenie!
			frameTable.get(randomIndex).setPageGiven(requestedPage);
			requestedPage.setFrameGiven(randomIndex);
			++framesUsed;
		}
	}

	@Override
	public void finish() {

	}
}
