package modal;

import java.util.ArrayList;

public class Blog extends NFT {
	private String title;
	private long view;
	private String href;

	public Blog() {

	}

	public Blog(String title, String author, String date, ArrayList<String> tag, long view, String href) {
		super(author, date, tag);
		this.view = view;
		this.title = title;
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getView() {
		return view;
	}

	public void setView(long view) {
		this.view = view;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
