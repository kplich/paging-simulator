package algorithms;

import base.*;
import base.pages.*;

import java.util.*;

public class RAND extends Simulator<Page> {
	public RAND(int numberOfPages, int numberOfFrames, int simulationSize, double threshold) {
		super(numberOfPages, numberOfFrames, simulationSize, threshold);

		for (int i = 0; i < numberOfPages; ++i) {
			pageTable.add(new Page(i, -1));
		}
	}

	@Override
	public void prepare() {
		//doNothing - nie zmieniamy kolejnosci stron, wiec nie ma potrzeby ich sortowania
	}

	@Override
	public void freeUpSomeMemory() {
		//nie mamy zadnego konkretnego algorytmu zwalniania ramki,
		//dlatego dla czytelnosci caly algorytm jest przeniesiony do metody allocatePage()
	}

	@Override
	public void allocatePage(Page requestedPage) {
		Random rng = new Random();

		int randomIndex = rng.nextInt(numberOfFrames); //wybieramy losowo ramke

		//jesli w ramce znajdowala sie jakas strona to znaczy,
		//ze trzeba ja bylo usunac i faktycznie mamy mniej uzytych ramek
		if (frameTable.get(randomIndex).getPageGiven() != null) {
			--framesUsed;

			//usun poprzednie polaczenie!
			frameTable.get(randomIndex).getPageGiven().setFrameGiven(-1);
		}

		//utworz nowe polaczenie!
		frameTable.get(randomIndex).setPageGiven(requestedPage);
		requestedPage.setFrameGiven(randomIndex);

		++framesUsed;
	}

	@Override
	public void whenPageWasLoaded(Page requestedPage) {
		//doNothing
	}
}
