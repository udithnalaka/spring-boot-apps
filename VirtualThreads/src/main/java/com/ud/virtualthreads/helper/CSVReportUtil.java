package com.ud.virtualthreads.helper;

import com.ud.virtualthreads.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class CSVReportUtil {

    public static void writeCustomersToCSV(List<Customer> customers, String filePath) {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("ID", "Name", "Email", "Phone", "Address", "Created Date"))) {

            for (Customer customer : customers) {
                csvPrinter.printRecord(
                        customer.getIndex(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getCustomerId(),
                        customer.getCompany(),
                        customer.getCountry()
                );
            }

            csvPrinter.flush();
           // log.info("Successfully wrote {} customers to {}", customers.size(), filePath);

        } catch (IOException e) {
            log.error("Error writing CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }
}
