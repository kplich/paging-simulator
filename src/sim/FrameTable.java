package sim;

import base.*;

import java.util.*;

public class FrameTable {
	ArrayList<Frame> frameTable;

	public FrameTable(int size) {
		frameTable = new ArrayList<>();

		for(int i = 0; i < size; ++i) {
			//frameTable.add(new Frame(i, new Page()));
		}
	}
}
