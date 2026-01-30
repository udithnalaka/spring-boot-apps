package com.ud.virtualthreads.service;

import com.ud.virtualthreads.helper.CustomerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Slf4j
@Service
public class PlatformThreadReportServiceImpl implements ReportService{

    private final CustomerHelper customerHelper;

    private final Executor executor;

    public PlatformThreadReportServiceImpl(@Autowired Executor platformThreadExecutor, CustomerHelper customerHelper) {
        this.customerHelper = customerHelper;
        this.executor = platformThreadExecutor;
    }

    //sending 300 requests concurrently
    // using 5 dedicated threads from platform threads.
    @Override
    public void generateCustomerReport() {
        log.info("Inside Platform Thread implementation");
        executor.execute(customerHelper::generateCustomerReport); // example output - Thread name: Thread[#70,pool-2-thread-1,5,main]
    }
}
