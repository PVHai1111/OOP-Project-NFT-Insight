package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class HomepageController implements Initializable {

	@FXML
	private BorderPane layout;

	@FXML
	private AnchorPane sublayout;

	@FXML
	private ImageView homeLogoView;

	@FXML
	private ImageView iconView;

	@FXML
	private ImageView decryptView;

	@FXML
	private ImageView coinDeskView;

	@FXML
	private ImageView nftInsiderView;

	@FXML
	private ImageView twitterView;

	@FXML
	private ImageView openSeaView;

	@FXML
	private ImageView binanceView;

	@FXML
	private ImageView niftyGatewayView;

	@FXML
	private ImageView raribleView;

	private Image homeLogo;
	private Image icon;
	private Image decrypt;
	private Image coinDesk;
	private Image nftInsider;
	private Image twitter;
	private Image openSea;
	private Image binance;
	private Image niftyGateway;
	private Image rarible;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize your Image objects here
		homeLogo = new Image(getClass().getResourceAsStream("/img/home_icon.png"));
		icon = new Image(getClass().getResourceAsStream("/img/logo.png"));
		decrypt = new Image(getClass().getResourceAsStream("/img/decrypt.png"));
		coinDesk = new Image(getClass().getResourceAsStream("/img/coindesk.jpg"));
		nftInsider = new Image(getClass().getResourceAsStream("/img/nft_insider.jpg"));
		twitter = new Image(getClass().getResourceAsStream("/img/twitter.png"));
		openSea = new Image(getClass().getResourceAsStream("/img/opensea.png"));
		binance = new Image(getClass().getResourceAsStream("/img/binance.png"));
		niftyGateway = new Image(getClass().getResourceAsStream("/img/nifty_gateway.jpg"));
		rarible = new Image(getClass().getResourceAsStream("/img/rarible.jpg"));

		// Set the Image objects to the corresponding ImageView objects
		homeLogoView.setImage(homeLogo);
		iconView.setImage(icon);
		decryptView.setImage(decrypt);
		coinDeskView.setImage(coinDesk);
		nftInsiderView.setImage(nftInsider);
		twitterView.setImage(twitter);
		openSeaView.setImage(openSea);
		binanceView.setImage(binance);
		niftyGatewayView.setImage(niftyGateway);
		raribleView.setImage(rarible);
	}

}