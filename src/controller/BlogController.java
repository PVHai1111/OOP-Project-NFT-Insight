package controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import getdatafromjson.GetBlog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import modal.Blog;
import path.PathBlog;

public class BlogController implements Initializable {
	@FXML
	private TableColumn<Blog, Long> viewColumn;

	@FXML
	private TableColumn<Blog, String> titleColumn;

	@FXML
	private TableColumn<Blog, String> authorColumn;

	@FXML
	private TableColumn<Blog, ArrayList<String>> tagColumn;

	@FXML
	private TextField searchWord;

	@FXML
	private TableColumn<Blog, String> dateColumn;

	@FXML
	private TableView<Blog> tableView;

	@FXML
	private Text TextImage1;

	@FXML
	private Text TextImage2;

	@FXML
	private Button button1;

	@FXML
	private Button button2;

	@FXML
	private Button button3;

	@FXML
	private Button button4;

	@FXML
	private Button button5;

	@FXML
	private Text hotTagText;

	ObservableList<Blog> blogs = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
		viewColumn.setCellValueFactory(new PropertyValueFactory<>("view"));

		// Add data
		PathBlog path = new PathBlog();
		GetBlog data = new GetBlog();
		ArrayList<Blog> blogs = data.getArrayList(path.pathCoin());
		ObservableList<Blog> dataList = FXCollections.observableArrayList(data.getArrayList(path.pathCoin()));
		tableView.setItems(dataList);

		TextImage1.setText(blogs.get(0).getTitle());
		TextImage2.setText(blogs.get(2).getTitle());

		// Filter
		FilteredList<Blog> filteredList = new FilteredList<Blog>(dataList, b -> true);

		searchWord.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(Blog -> {
				String searchKeyWord = newValue.toLowerCase();
				if (Blog.getTitle().toLowerCase().contains(searchKeyWord))
					return true;
				if (Blog.getAuthor().toLowerCase().contains(searchKeyWord))
					return true;
				if (Blog.getDate().contains(searchKeyWord))
					return true;
				if (Blog.getTag().contains(searchKeyWord))
					return true;
				return false;
			});
		});
		SortedList<Blog> sortedList = new SortedList<Blog>(filteredList);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);

		// Open blog in browser when click
		tableView.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() == 1) {
				Blog selectedItem = tableView.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					try {
						Desktop.getDesktop().browse(new URI(selectedItem.getHref()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		/// Tag button
		ArrayList<String> tagList = data.getListTag();
		button1.setText(tagList.get(0));
		button2.setText(tagList.get(1));
		button3.setText(tagList.get(2));
		button4.setText(tagList.get(3));
		button5.setText(tagList.get(4));

		button1.setOnAction(e -> {
			String tagToFilter = button1.getText();
			tableView.setItems(dataList.filtered(blog -> blog.getTag().contains(tagToFilter)));
		});
		button2.setOnAction(e -> {
			String tagToFilter = button2.getText();
			tableView.setItems(dataList.filtered(blog -> blog.getTag().contains(tagToFilter)));
		});
		button3.setOnAction(e -> {
			String tagToFilter = button3.getText();
			tableView.setItems(dataList.filtered(blog -> blog.getTag().contains(tagToFilter)));
		});
		button4.setOnAction(e -> {
			String tagToFilter = button4.getText();
			tableView.setItems(dataList.filtered(blog -> blog.getTag().contains(tagToFilter)));
		});
		button5.setOnAction(e -> {
			String tagToFilter = button5.getText();
			tableView.setItems(dataList.filtered(blog -> blog.getTag().contains(tagToFilter)));
		});

		// Hot tag in the newest month
		String top3Tag = "";
		List<String> hotTag = data.getThreeHotTag();
		for (String tag : hotTag) {
			top3Tag += tag + "\n";
		}
		hotTagText.setText(top3Tag);

	}

}
