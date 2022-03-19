package guru.qa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;

import com.opencsv.CSVReader;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Парсим файлы разных расширений")
public class HomeWorkTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void workWithZipFileContainsFilesWithDifferentExtensions() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream(
                "files/test.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {

                if (InputStream is1 = classLoader.getResourceAsStream(
                        "files/business-financial-data-sep-2021-quarter.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(is1))) {
                        List<String[]> contents = reader.readAll();
                        assertThat(contents.get(0)).contains("Month", "1958", "1959", "1960");
                    }
                } else if (zipEntry.getName().contains("pdf")) {
                    PDF pdf = new PDF(new File(zipEntry.getName()));
                    assertThat(pdf.author).isNull();
                } else {
                    XLS xls = new XLS(new File(zipEntry.getName()));
                    assertThat(xls.excel
                            .getSheetAt(0)
                            .getRow(5).
                            getCell(1).getStringCellValue()).contains("Вялов");
                }
            }
        }
    }
}
