package com.ud.virtualthreads.service;

import com.ud.virtualthreads.helper.CustomerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Slf4j
@Service
public class VirtualThreadReportServiceImpl implements ReportService{

    private final CustomerHelper customerHelper;

    private final Executor executor;

    public VirtualThreadReportServiceImpl(@Autowired Executor virtualThreadExecutor, CustomerHelper customerHelper) {
        this.customerHelper = customerHelper;
        this.executor = virtualThreadExecutor;
    }

    //sending 300 requests concurrently
    // virtual thread will be created for each task (request) to get the work done. Thus, not holding the platform thread until the full task is completed.
    // a virtual thread will be mounted/ unmounted to/from a platform thread.
    @Override
    public void generateCustomerReport() {
        log.info("Inside Virtual Thread implementation");
        executor.execute(customerHelper::generateCustomerReport); // example output - Thread name: VirtualThread[#71]/runnable@ForkJoinPool-1-worker-1
    }
}
