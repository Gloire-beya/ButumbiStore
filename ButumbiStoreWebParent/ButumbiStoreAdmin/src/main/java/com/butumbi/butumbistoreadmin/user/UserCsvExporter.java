package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void exporter(List<User> userList, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "text/csv", ".csv");

        var csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "E-mail", "First name", "Last name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roleSet", "enabled"};

        csvWriter.writeHeader(csvHeader);

        for (User user : userList){
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();
    }
}
