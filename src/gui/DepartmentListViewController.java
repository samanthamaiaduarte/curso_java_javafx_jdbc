package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListViewController implements Initializable {

	@FXML
	private Button btnNew;
	@FXML
	private TableView<Department> tbvDepartment;
	@FXML
	private TableColumn<Department, Integer> tbcId;
	@FXML
	private TableColumn<Department, String> tbcName;
	
	public void onBtnNewAction() {
		System.out.println("onBtnNewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tbvDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
