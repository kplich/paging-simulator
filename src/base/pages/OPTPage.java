package base.pages;

public class OPTPage extends Page {
	private int timeUntilNextReference;

	public OPTPage(int pageNumber, int frameGiven) {
		super(pageNumber, frameGiven);
		timeUntilNextReference = 0;
	}

	public int getTimeUntilNextReference() {
		return timeUntilNextReference;
	}

	public void setTimeUntilNextReference(int timeUntilNextReference) {
		this.timeUntilNextReference = timeUntilNextReference;
	}

	public void countTimeUntilNextReference() {
		--timeUntilNextReference;
	}
}
