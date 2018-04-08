package sim;

import java.util.*;

/**
 * A wrapper class for an typical ArrayList</Page> created to make sorting in multiple ways possible.
 * PageTable also allows for keeping track of all the pages and all the frames.
 */
public class PageTable {
	ArrayList<Page> pageTable;

	public PageTable(int size) {
		pageTable = new ArrayList<>();

		for(int i = 0; i<size; ++i) {
			pageTable.add(new Page(i, -1));
		}
	}

	public void overwritePage(int index, Page page) {
		pageTable.set(index, page);
	}


}
