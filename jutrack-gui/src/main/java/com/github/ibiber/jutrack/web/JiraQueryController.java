package com.github.ibiber.jutrack.web;

import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.ibiber.jutrack.external.DefaultValueProviderService;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;

import jakarta.validation.Valid;

@Controller
public class JiraQueryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraQueryController.class);

    private final DefaultValueProviderService defaultValues;
    private final QueryResultService queryResultService;
    private final Clock clock;

    public JiraQueryController(DefaultValueProviderService defaultValues, QueryResultService queryResultService,
            Clock clock) {
        this.defaultValues = defaultValues;
        this.queryResultService = queryResultService;
        this.clock = clock;
    }

    @ModelAttribute("form")
    JiraQueryForm formDefaults() {
        return JiraQueryForm.withDefaults(defaultValues, clock);
    }

    @GetMapping({ "/", "" })
    public String showForm(@ModelAttribute("form") JiraQueryForm form, Model model) {
        model.addAttribute("resultView", ResultViewModel.empty());
        return "index";
    }

    @PostMapping(path = "/query")
    public String submit(@Valid @ModelAttribute("form") JiraQueryForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("resultView", ResultViewModel.empty());
            return "index";
        }

        JiraQueryParmeter parameter = form.toParameter();
        LOGGER.info("Processing jira query for {}", parameter.jiraRootUrl);
        ResultViewModel viewModel = queryResultService.fetch(parameter);
        model.addAttribute("resultView", viewModel);
        return "index";
    }
}
