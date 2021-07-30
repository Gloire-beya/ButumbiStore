package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter {
    public void exporter(List<User> userList, HttpServletResponse response) throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy- MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "users_" + timestamp + ".csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String heaverValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, heaverValue);

        CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "E-mail", "First name", "Last name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roleSet", "enabled"};

        csvWriter.writeHeader(csvHeader);

        for (User user : userList){
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();
    }
}
