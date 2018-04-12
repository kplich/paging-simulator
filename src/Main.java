import algorithms.*;

public class Main {
	public static void main(String[] args) {
		int pages = 10000;
		int frames = 500;
		double randomness = 0.05;

		int size = 100000;

		//System.out.println(pages + " pages, " + frames + " frames, " + size + " references");

		/*System.out.print("FIFO:\t");
		for(int i = 0; i < 5; ++i) {
			System.out.print(new FIFO(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nRAND:\t");
		for (int i = 0; i < 5; ++i) {
			System.out.print(new RAND(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nLRU:\t");
		for (int i = 0; i < 5; ++i) {
			System.out.print(new LRU(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nALRU:\t");
		for (int i = 0; i < 5; ++i) {
			System.out.print(new ALRU(pages, frames, size, randomness).run() + "\t");
		}
		System.out.print("\nOPT:\t");
		for (int i = 0; i < 5; ++i) {
			System.out.print(new OPT(pages, frames, size, randomness).run() + "\t");
		}*/

			System.out.println("FIFO:\t" + new FIFO(pages, frames, size, randomness).run());
			System.out.println("RAND:\t" + new RAND(pages, frames, size, randomness).run());
			System.out.println("LRU:\t" + new LRU(pages, frames, size, randomness).run());
			System.out.println("ALRU:\t" + new ALRU(pages, frames, size, randomness).run());
			System.out.println("OPT:\t" + new OPT(pages, frames, size, randomness).run());
			System.out.println("--");

	}
}
