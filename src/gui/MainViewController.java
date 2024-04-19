package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem mitSeller;
	@FXML
	private MenuItem mitDepartment;
	@FXML
	private MenuItem mitAbout;
	
	@FXML
	public void onMitSellerAction() {
		System.out.println("onMitSellerAction");
	}

	@FXML
	public void onMitDepartmentAction() {
		loadView("/gui/DepartmentListView.fxml");
	}
	
	@FXML
	public void onMitboutAction() {
		loadView("/gui/AboutView.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		// TODO Auto-generated method stub
		
	}

	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//Encontra o MainMenu dentro do vbox, ou seja, o primeiro Children
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//Limpa todo o node VBox
			mainVBox.getChildren().clear();
			
			//Adiciona o MainMenu
			mainVBox.getChildren().add(mainMenu);
			
			//Adiciona os filhos da nova view
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
