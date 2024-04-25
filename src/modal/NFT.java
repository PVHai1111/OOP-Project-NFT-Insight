package modal;

import java.util.ArrayList;

public class NFT {
	private String author;
	private String date;
	private ArrayList<String> tag = new ArrayList<String>();

	public NFT() {

	}

	public NFT(String author, String date, ArrayList<String> tag) {
		this.author = author;
		this.date = date;
		this.tag = tag;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTag(ArrayList<String> tag) {
		this.tag = tag;
	}

	public ArrayList<String> getTag() {
		return tag;
	}

	public String printTag() {
		String s = "";
		for (String hashtag : tag) {
			s += hashtag + " ";
		}
		return s;
	}

}
