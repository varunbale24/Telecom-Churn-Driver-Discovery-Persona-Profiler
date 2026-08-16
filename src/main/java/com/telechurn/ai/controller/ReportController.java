package com.telechurn.ai.controller;

import com.telechurn.ai.service.ReportService;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("summary", reportService.buildSummary());
        model.addAttribute("csvPreview", reportService.buildCsv().lines().limit(6).toList());
        return "reports";
    }

    @GetMapping("/reports/generate")
    public String generate(Model model) {
        model.addAttribute("summary", reportService.buildSummary());
        model.addAttribute("csvPreview", reportService.buildCsv().lines().limit(6).toList());
        model.addAttribute("generatedMessage", "Report generated successfully.");
        return "reports";
    }

    @GetMapping("/reports/download")
    public ResponseEntity<byte[]> downloadCsv() {
        byte[] csv = reportService.buildCsv().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("telechurn-report.csv").build());
        return ResponseEntity.ok().headers(headers).body(csv);
    }
}
