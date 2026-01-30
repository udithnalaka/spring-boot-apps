package com.ud.virtualthreads.service;

import com.ud.virtualthreads.helper.CustomerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MainThreadReportServiceImpl implements ReportService{

    private final CustomerHelper customerHelper;

    public MainThreadReportServiceImpl(CustomerHelper customerHelper) {
        this.customerHelper = customerHelper;
    }

    //sending 300 requests concurrently
    //tomcat concurrent threads by default 200.
    // so, out of 300 requests, 200 will be processed by the tomcat's available 200 threads.
    // other 100 requests will be waiting to be processed depending on the freed up threads.
    @Override
    public void generateCustomerReport() {
        log.info("Inside Main Thread (Tomcat default) implementation");
        customerHelper.generateCustomerReport(); //example output - Thread name: Thread[#63,http-nio-8080-exec-8,5,main

    }
}
