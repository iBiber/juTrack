package com.github.ibiber.jutrack.webuivaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.ibiber.jutrack.external.JiraIssuesProcessorService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {
	
	public MainView(@Autowired ParameterPanel parameterPanel, @Autowired ResultPanel resultPanel,
			@Autowired JiraIssuesProcessorService jiraIssuesProcessor
	) {
		add(parameterPanel);
		parameterPanel.setConsumer((parameter) -> jiraIssuesProcessor.getIssues(parameter, resultPanel));
		add(resultPanel);
	}
}
