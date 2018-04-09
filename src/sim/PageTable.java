package sim;

import sim.pages.*;

import java.util.*;

/**
 * A wrapper class for an typical ArrayList<Page> created to make sorting in multiple ways possible.
 * PageTable also allows for keeping track of all the pages and all the frames.
 */
public class PageTable {
	ArrayList<Page> pageTable;
	int size;
	int maxFrames;
	int usedFrames;

	public PageTable(int size, int frames) {
		this.size = size;
		pageTable = new ArrayList<>();

		for(int i = 0; i<size; ++i) {
			pageTable.add(new Page(i, -1));
		}

		maxFrames = frames;
		usedFrames = 0;
	}

	public void overwritePage(int index, Page page) {
		//assuming the table is sorted by indices!
		pageTable.set(index, page);
	}

	/**
	 * Simple check if a page is loaded to memory.
	 * @param pageIndex index of a page to be checked
	 * @return true, if the page has a frame assigned, false otherwise.
	 */
	public boolean isLoaded(int pageIndex) {
		//TODO: make sure the page table is sorted by indices.

		//assuming the page is sorted by indices!
		return pageTable.get(pageIndex).getFrameGiven() > 0;
	}

	/**
	 * Checks if all the frames are used.
	 * @return true, if all the frames are used and false otherwise
	 */
	public boolean memoryFull() {
		return usedFrames == maxFrames;
	}

	/**
	 * Checks if there's a free frame in physical memory.
	 *
	 * @return index of a first free frame if it exists, or -1 if there's no available memory.
	 */
	public int nextFreeFrame() {
		//assuming the page is sorted by frame given in ascending order
		if(pageTable.get(0).getFrameGiven() > 0) return pageTable.get(0).getPageNumber();
		if (usedFrames < maxFrames) {
			for (int i = 0; i < physicalMemory.length; ++i) {
				if (physicalMemory[i] == -1) return i;
			}
		}

		return -1;
	}
}
