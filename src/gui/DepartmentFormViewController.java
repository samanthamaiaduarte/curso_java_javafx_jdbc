package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormViewController implements Initializable {

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label lblMessage;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	
	@FXML
	public void onBtnSaveAction() {
		System.out.println("onBtnSaveAction");
	}
	
	@FXML
	public void onBtnCancelAction() {
		System.out.println("onBtnCancelAction");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
}
