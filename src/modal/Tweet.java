package modal;

import java.util.ArrayList;

public class Tweet extends NFT {
	private String replies;
	private String reposts;
	private String likes;
	private String views;
	private String text;

	public Tweet() {

	}

	public Tweet(String author, String date, ArrayList<String> tag, String replies, String reposts, String likes,
			String views, String text) {
		super(author, date, tag);
		this.replies = replies;
		this.reposts = reposts;
		this.likes = likes;
		this.views = views;
		this.text = text;
	}

	public String getReplies() {
		return replies;
	}

	public String getReposts() {
		return reposts;
	}

	public String getLikes() {
		return likes;
	}

	public String getViews() {
		return views;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	public void setReposts(String reposts) {
		this.reposts = reposts;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
