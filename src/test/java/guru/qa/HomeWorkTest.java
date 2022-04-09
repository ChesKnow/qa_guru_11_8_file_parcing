package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Парсим файлы разных расширений")
public class HomeWorkTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    public void ZipFewFilesParseTest() throws Exception {

        ZipFile zipFile = new ZipFile("files/test.zip");
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(entry.getName().contains(".csv")){
                try (InputStream is = classLoader.getResourceAsStream(entry.getName());
                     CSVReader reader = new CSVReader(new InputStreamReader(is))) {
                    List<String[]> contents = reader.readAll();
                    assertThat(contents.get(0)).contains("Series_reference",
                            "Period",
                            "Data_value",
                            "Suppressed",
                            "STATUS",
                            "UNITS",
                            "Magnitude",
                            "Subject",
                            "Group",
                            "Series_title_1",
                            "Series_title_2",
                            "Series_title_3",
                            "Series_title_4",
                            "Series_title_5");

                }
            } else if (entry.getName().contains(".pdf")) {
                File pdf_download = new File(entry.getName());
                PDF pdf = new PDF(pdf_download);
                assertThat(pdf.author).isNull();
            } else {
                File xls_download = new File(entry.getName());
                XLS xls = new XLS(xls_download);
                assertThat(xls.excel
                        .getSheetAt(0)
                        .getRow(2).
                        getCell(1).getStringCellValue()).contains("ФИО");
            }
        }
    }

}

