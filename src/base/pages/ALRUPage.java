package base.pages;

public class ALRUPage extends Page {
	boolean wasReferenced;

	public ALRUPage(int pageNumber, int frameGiven) {
		super(pageNumber, frameGiven);
		wasReferenced = false;
	}

	public boolean wasReferenced() {
		return wasReferenced;
	}

	public void setReferenced(boolean wasReferenced) {
		this.wasReferenced = wasReferenced;
	}

}
