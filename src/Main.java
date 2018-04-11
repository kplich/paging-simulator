import algorithms.*;

public class Main {
	public static void main(String[] args) {
		int pages = 5000;
		int frames = 50;
		//double randomness = 0.02;

		int size = 10000;

		//System.out.println(pages + " pages, " + frames + " frames, " + size + " references");

		System.out.print("FIFO:\t");
		for(double randomness = 0.2; randomness <= 1; randomness += 0.02) {
			System.out.print(new FIFO(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nRAND:\t");
		for (double randomness = 0.2; randomness <= 1; randomness += 0.02) {
			System.out.print(new RAND(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nLRU:\t");
		for (double randomness = 0.2; randomness <= 1; randomness += 0.02) {
			System.out.print(new LRU(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nALRU:\t");
		for (double randomness = 0.2; randomness <= 1; randomness += 0.02) {
			System.out.print(new ALRU(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nOPT:\t");
		for (double randomness = 0.2; randomness <= 1; randomness += 0.02) {
			System.out.print(new OPT(pages, frames, size, randomness).run() + "\t");
		}
			/*System.out.println("FIFO:\t" + new FIFO(pages, frames, size, randomness).run());
			System.out.println("RAND:\t" + new RAND(pages, frames, size, randomness).run());
			System.out.println("LRU:\t" + new LRU(pages, frames, size, randomness).run());
			System.out.println("ALRU:\t" + new ALRU(pages, frames, size, randomness).run());
			System.out.println("OPT:\t" + new OPT(pages, frames, size, randomness).run());
			System.out.println("--");*/

	}
}
