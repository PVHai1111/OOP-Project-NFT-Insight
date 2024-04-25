package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class NavigationController implements Initializable {
	@FXML
	private BorderPane layout;

	@FXML
	private AnchorPane sublayout;

	@FXML
	void homepage(ActionEvent event) {
		loadPage("homepage");
	}

	@FXML
	void tweets(ActionEvent event) {
		loadPage("tweets");
	}

	@FXML
	void blogs(ActionEvent event) {
		loadPage("blogs");
	}

	@FXML
	void collections(ActionEvent event) {
		loadPage("collections");
	}

	@FXML
	void relation(ActionEvent event) {
		loadPage("relation");
	}

	void loadPage(String Page) {
		Parent rootParent = null;
		try {
			rootParent = FXMLLoader.load(this.getClass().getResource("/view/" + Page + ".fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		layout.setCenter(rootParent);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

}
