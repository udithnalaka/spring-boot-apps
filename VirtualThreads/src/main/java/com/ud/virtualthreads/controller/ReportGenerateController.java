package com.ud.virtualthreads.controller;

import com.ud.virtualthreads.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportGenerateController {

    @Autowired
    @Qualifier("mainThreadReportServiceImpl")
    private ReportService mainReportService;


    @Autowired
    @Qualifier("platformThreadReportServiceImpl")
    private ReportService platformReportService;

    @Autowired
    @Qualifier("virtualThreadReportServiceImpl")
    private ReportService virtualReportService;

    @GetMapping("/single-thread/")
    public ResponseEntity<String> generateCustomerReportUsingMainThread() {

        mainReportService.generateCustomerReport();
        return ResponseEntity.ok("Customer CSV report generated.");

    }

    @GetMapping("/platform-thread/")
    public ResponseEntity<String> generateCustomerReportUsingPlatformThreads() {

        platformReportService.generateCustomerReport();
        return ResponseEntity.ok("Customer CSV report generated.");

    }

    @GetMapping("/virtual-thread/")
    public ResponseEntity<String> generateCustomerReportUsingVirtualThreads() {

        virtualReportService.generateCustomerReport();
        return ResponseEntity.ok("Customer CSV report generated.");

    }
}
