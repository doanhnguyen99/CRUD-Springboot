package com.example.demo.util;

import com.example.demo.entity.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Name", "Phone", "Address", "Email", "Is_Delete" };

    public static boolean hasCSVFormat(MultipartFile file){
        if (!TYPE.equals(file.getContentType())){
            return false;
        }

        return true;
    }

    public static List<User> uploadCSV(InputStream is){
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

             List<User> users = new ArrayList<>();

             Iterable<CSVRecord> csvRecords = csvParser.getRecords();

             for(CSVRecord csvRecord : csvRecords){
                 User user = new User(
                         csvRecord.get("Name"),
                         csvRecord.get("Phone"),
                         csvRecord.get("Address"),
                         csvRecord.get("Email"),
                         Integer.parseInt(csvRecord.get("Is_Delete"))
                 );
                 users.add(user);
             }

             return users;

        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    // export csv file
    public static ByteArrayInputStream userToCSV(List<User> users){
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            List<String> data = new ArrayList<>();
            data = Arrays.asList("Name", "Phone", "Address", "Email", "Is_Delete");
            csvPrinter.printRecord(data);
            for (User user : users) {
                data = Arrays.asList(
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
