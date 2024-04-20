package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormViewController implements Initializable {
	
	private Seller seller;
	private SellerService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dtpBirthDate;
	@FXML
	private TextField txtBaseSalary;	
	@FXML
	private ComboBox<Department> cbbDepartment;
	@FXML
	private Label lblErrorName;
	@FXML
	private Label lblErrorEmail;
	@FXML
	private Label lblErrorBirthDate;
	@FXML
	private Label lblErrorBaseSalary;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;
	
	public void setSeller(Seller Seller) {
		this.seller = Seller;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);		
	}
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(seller == null) {
			throw new IllegalStateException("Seller was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			seller = getFormData();
			service.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Database Exception", "Error saving object", e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(dtpBirthDate, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtBaseSalary);
	}
	
	public void updateFormData() {
		if(seller == null) {
			throw new IllegalStateException("Seller was null");
		}
		
		if(seller.getId() != null) {
			txtId.setText(String.valueOf(seller.getId()));
		}
		
		txtName.setText(seller.getName());
		txtEmail.setText(seller.getEmail());
		dtpBirthDate.setValue(seller.getBirthDate());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
	}
	
	private Seller getFormData() {
		Seller obj = new Seller();
		
		ValidationException exceptions = new ValidationException("Validation errors");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exceptions.addError("name", "field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(exceptions.getErrors().size() > 0) {
			throw exceptions;
		}
		
		return obj;
	}

	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
	}
}
