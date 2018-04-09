package sim.pages;

public class FIFOPage extends Page {
	private int timeSinceAllocation;

	public FIFOPage(int pageNumber, int frameGiven) {
		super(pageNumber, frameGiven);
	}

	public int getTimeSinceAllocation() {
		return timeSinceAllocation;
	}

	public void setTimeSinceAllocation(int timeSinceAllocation) {
		this.timeSinceAllocation = timeSinceAllocation;
	}
}
