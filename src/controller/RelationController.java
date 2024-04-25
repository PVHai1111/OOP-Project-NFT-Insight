package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import getdatafromjson.GetBlog;
import getdatafromjson.GetCollection;
import getdatafromjson.GetTweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modal.Blog;
import modal.Collection;
import modal.Tweet;
import path.PathBlog;
import path.PathCollection;
import path.PathTweet;

public class RelationController implements Initializable {

	@FXML
	private TableView<Collection> tableNFTs;

	@FXML
	private TableColumn<Collection, String> volumnColumn;

	@FXML
	private TableColumn<Collection, String> collectionColumn;

	@FXML
	private TableView<Tweet> tableTweets;

	@FXML
	private TableColumn<Tweet, String> dateColumn1;

	@FXML
	private TableColumn<Tweet, ArrayList<String>> tagColumn1;

	@FXML
	private TableColumn<Tweet, String> authorColumn1;

	@FXML
	private TextField keywordTextField;

	@FXML
	private TableView<Blog> tableBlogs;

	@FXML
	private TableColumn<Blog, String> authorColumn2;

	@FXML
	private TableColumn<Blog, ArrayList<String>> tagColumn2;

	@FXML
	private TableColumn<Blog, String> titleColumn2;

	@FXML
	private TableColumn<Blog, String> dateColumn2;

	@FXML
	private Button updateButton;

	@FXML
	private TabPane tpDetail;

	@FXML
	private Tab tweetsTab;

	@FXML
	private Tab blogsTab;

	ObservableList<Collection> Collections = FXCollections.observableArrayList();
	private ObservableList<Tweet> tweetList;
	private ObservableList<Blog> blogList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		collectionColumn.setCellValueFactory(new PropertyValueFactory<>("collection"));
		volumnColumn.setCellValueFactory(new PropertyValueFactory<>("volumn"));

		// Add collection
		GetCollection dataCollection = new GetCollection();
		PathCollection pathCollection = new PathCollection();
		ObservableList<Collection> nftsList = FXCollections
				.observableArrayList(dataCollection.getArrayList(pathCollection.getPathBinance()));
		tableNFTs.setItems(nftsList);

		// Filter

		FilteredList<Collection> filteredData3 = new FilteredList<>(nftsList, b -> true);

		keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData3.setPredicate(collection -> {
				if (newValue == null) {
					return true;
				}
				String searchKeyword = newValue.toLowerCase();

				if (collection.getCollection().toLowerCase().indexOf(searchKeyword) > -1) {
					return true;
				} else
					return false;
			});
		});
		SortedList<Collection> sortedData3 = new SortedList<>(filteredData3);
		sortedData3.comparatorProperty().bind(tableNFTs.comparatorProperty());
		tableNFTs.setItems(sortedData3);

		// Add tweet

		dateColumn1.setCellValueFactory(new PropertyValueFactory<>("date"));
		authorColumn1.setCellValueFactory(new PropertyValueFactory<>("author"));
		tagColumn1.setCellValueFactory(new PropertyValueFactory<>("tag"));
		PathTweet pathTweet = new PathTweet();
		GetTweet dataTweet = new GetTweet();
		ObservableList<Tweet> tweetList = FXCollections
				.observableArrayList(dataTweet.getArrayList(pathTweet.getPathTweet()));

		// Add blogs & news

		titleColumn2.setCellValueFactory(new PropertyValueFactory<Blog, String>("title"));
		authorColumn2.setCellValueFactory(new PropertyValueFactory<Blog, String>("author"));
		dateColumn2.setCellValueFactory(new PropertyValueFactory<Blog, String>("date"));
		tagColumn2.setCellValueFactory(new PropertyValueFactory<>("tag"));
		PathBlog pathBlog = new PathBlog();
		GetBlog dataBlog = new GetBlog();
		ObservableList<Blog> blogList = FXCollections
				.observableArrayList(dataBlog.getArrayList(pathBlog.pathDecrypt()));

		FilteredList<Tweet> filteredData1 = new FilteredList<>(tweetList);

		FilteredList<Blog> filteredData2 = new FilteredList<>(blogList);
		tableTweets.setItems(filteredData1);
		tableBlogs.setItems(filteredData2);

		// Display tweets and blog related selected collection

		tableNFTs.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			tableTweets.setItems(filteredData1);
			tableBlogs.setItems(filteredData2);
			if (newSelection != null) {
				String selectedName = newSelection.getCollection();
				filteredData1.setPredicate(tweet -> tweet.getTag().contains(selectedName));
				filteredData2.setPredicate(blog -> checkContains(blog, selectedName));
			}
		});

	}

	// Check collection in and blog
	boolean checkContains(Blog blog, String s) {
		String[] words = s.split(" ");
		for (String a : words)
			return blog.getTitle().toLowerCase().contains(a.toLowerCase());
		return false;
	}
}
