package sim;

import java.util.*;

public abstract class Simulator {
	Random generator = new Random();

	LinkedList<Integer> requestQueue;
	int[] physicalMemory;
	int virtualMemory;

	public Simulator(int simulationSize, int pages, int frames) {
		virtualMemory = pages;
		physicalMemory = new int[frames];
		generateRequests(simulationSize);
	}

	private void generateRequests(int size) {
		requestQueue = new LinkedList<>();

		for(int i = 0; i<size; ++i) {
			if(requestQueue.isEmpty() || generator.nextDouble() < 0.05) {
				//jesli kolejka zadan jest pusta lub bog tak chcial, po prostu dodaj nowe odwolanie do strony do kolejki
				requestQueue.add(generator.nextInt(virtualMemory) + 1);
			}
			else {
				//w 95% przypadkow dodajemy do kolejki odwolanie do tej samej strony, do ktorej odwolano sie wczesniej
				requestQueue.add(requestQueue.peekLast());
			}
		}
	}
}
