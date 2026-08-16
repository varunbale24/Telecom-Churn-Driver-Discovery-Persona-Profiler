package com.telechurn.ai.controller;

import com.telechurn.ai.service.CustomerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String customers(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String query,
                            @RequestParam(required = false) String contract,
                            @RequestParam(required = false) String riskLevel,
                            @RequestParam(required = false) String churn,
                            @RequestParam(defaultValue = "customerId") String sort,
                            @RequestParam(defaultValue = "true") boolean ascending,
                            Model model) {
        model.addAttribute("customers", customerService.search(query, contract, riskLevel, churn, sort, ascending));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("query", query);
        model.addAttribute("contract", contract);
        model.addAttribute("riskLevel", riskLevel);
        model.addAttribute("churn", churn);
        model.addAttribute("sort", sort);
        model.addAttribute("ascending", ascending);
        return "customers";
    }

    @GetMapping("/customers/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found")));
        return "customer-details";
    }
}
