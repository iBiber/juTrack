package com.github.ibiber.jutrack.webuivaadin;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

@Tag("parameter-panel")
@Component
@Scope("prototype")
public class ParameterPanel extends VerticalLayout{
	private Consumer<JiraQueryParmeter> callback;
	
	public void setConsumer(Consumer<JiraQueryParmeter> callback) {
		this.callback = callback;
	}
	
	public ParameterPanel() {
		TextField jiraRootUrlField = new TextField();
		jiraRootUrlField.setLabel("Jira root URL:");
		jiraRootUrlField.setPlaceholder("http://jira.domain.net");
		jiraRootUrlField.setRequired(true);
		add(jiraRootUrlField);
		
		TextField userNameField = new TextField();
		userNameField.setLabel("Jira user Name: ");
		add(userNameField);
		
		PasswordField passwordField = new PasswordField();
		passwordField.setLabel("Jira password: ");
		add(passwordField);
		
		DatePicker startDatePicker = new DatePicker();
		startDatePicker.setLabel("Start date");
		add(startDatePicker);
		
		DatePicker endDatePicker = new DatePicker();
		endDatePicker.setLabel("End date");
		add(endDatePicker);
		
		Button okButton = new Button("Start query", (event) -> {
			JiraQueryParmeter parameter = new JiraQueryParmeter(jiraRootUrlField.getValue(),
			        userNameField.getValue(), passwordField.getValue(), startDatePicker.getValue(),
			        endDatePicker.getValue());
			callback.accept(parameter);
		});
		add(okButton);
	}
}
