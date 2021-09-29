package com.example.demo.util;

import com.example.demo.entity.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {
    public static ByteArrayInputStream userToCSV(List<User> users){
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<String> data = new ArrayList<>();
            data = Arrays.asList("Id", "Name", "Phone", "Address", "Email", "Is_Delete");
            csvPrinter.printRecord(data);
            for (User user : users) {
                data = Arrays.asList(
                        String.valueOf(user.getId()),
                        user.getName(),
                        user.getPhone(),
                        user.getAddress(),
                        user.getEmail(),
                        String.valueOf(user.getIsDelete())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
