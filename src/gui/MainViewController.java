package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onMitDepartmentAction");
	}
	
	@FXML
	public void onMitboutAction() {
		System.out.println("onMitboutAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		// TODO Auto-generated method stub
		
	}


	

}
