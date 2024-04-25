package getdatafromjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import modal.Blog;

public class GetBlog extends GetNFT implements GetHotTag {
	private ArrayList<String> tagList = new ArrayList<String>();
	private int newestMonth;
	private HashMap<String, Integer> hashMap = new HashMap<>();

	public ArrayList<Blog> getArrayList(String path) {
		try {
			ArrayList<Blog> blogList = new ArrayList<Blog>();
			JSONParser parser = new JSONParser();
			Object object = parser.parse(path);
			JSONArray mainJsonObject = (JSONArray) object;
			for (int i = 0; i < mainJsonObject.size(); i++) {
				Blog blog = new Blog();
				JSONObject jsonObject = (JSONObject) mainJsonObject.get(i);
				String Title = (String) jsonObject.get("title");
				blog.setTitle(Title);
				String autString = (String) jsonObject.get("author");
				blog.setAuthor(autString);
				String href = (String) jsonObject.get("href");
				blog.setHref(href);
				String dateString = (String) jsonObject.get("date");
				blog.setDate(dateString);
				String tagString = (String) jsonObject.get("tags");
				if (checkString(tagString))
					tagList.add(tagString);
				ArrayList<String> tagList2 = new ArrayList<String>();
				tagList2.add(tagString);
				blog.setTag(tagList2);
				String view = (String) jsonObject.get("views");
				if (view.equals("null")) {
					blog.setView(0);
				} else {

					blog.setView(Long.parseLong(view));
					// Calculate total views of each tag
					if (i == 0) {
						newestMonth = getMonth(dateString);
						hashMap.put(tagString, Integer.parseInt(view));
					} else {
						if (getMonth(dateString) == newestMonth) {
							if (hashMap.containsKey(tagString)) {
								int currentValue = hashMap.get(tagString);
								currentValue += Integer.parseInt(view);
								hashMap.put(tagString, currentValue);
							} else {
								hashMap.put(tagString, Integer.parseInt(view));
							}
						}
					}
				}
				blogList.add(blog);

			}
			return blogList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// Check tag has been existed in tagList
	public boolean checkString(String tag) {
		for (String a : tagList) {
			if (a.equalsIgnoreCase(tag)) {
				return false;
			}
		}
		return true;
	}

	// Return ArrayList of tag in data
	public ArrayList<String> getListTag() {
		return tagList;
	}

	// Get the newest month
	public int getMonth(String date) {
		String[] parts = date.split("/");
		return Integer.parseInt(parts[1]);
	}

	// Return top 3 hot tag in the newest month
	public List<String> getThreeHotTag() {
		List<Map.Entry<String, Integer>> sortedTags = new ArrayList<>(hashMap.entrySet());
		sortedTags.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
		List<String> topThreeTags = new ArrayList<>();
		for (int i = 0; i < Math.min(3, sortedTags.size()); i++) {
			topThreeTags.add(sortedTags.get(i).getKey());
		}

		return topThreeTags;
	}

}
