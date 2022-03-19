package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void filesPdfTest() throws Exception {
        //open("https://junit.org/junit5/docs/current/user-guide/");
        File pdf_download = new File("src/test/resources/files/EE052942424RU.pdf");
        PDF pdf = new PDF(pdf_download);
        assertThat(pdf.author).isNull();
    }

    @Test
    void filesXLsTest() throws Exception {
        open("http://romashka2008.ru/price");
        File xls_download = $(".site-main__inner a[href*='prajs_ot']").download();
        XLS xls = new XLS(xls_download);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(11).
                getCell(1).getStringCellValue()).contains("Сахалинская обл, Южно-Сахалинск");
    }

    @Test
    void filesCsvTest() throws Exception{

        try (InputStream is = classLoader.getResourceAsStream(
                "files/business-financial-data-sep-2021-quarter.csv");
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
        
    }

    @Test
    void parseZipTest() throws Exception {

        try (InputStream is = classLoader.getResourceAsStream(
                "files/business-financial-data-september-2021-quarter.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                assertThat(zipEntry.getName()).isEqualTo("business-financial-data-sep-2021-quarter.csv");
            }
        }
    }
    @Test
    void jsonParsingTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = classLoader.getResourceAsStream("files/simple.json")) {
             String json = new String(is.readAllBytes(),UTF_8);
            JsonObject jsonobject = gson.fromJson(json, JsonObject.class);
            assertThat(jsonobject.get("name").getAsString()).isEqualTo("Dmitrii");
            assertThat(jsonobject.get("address").getAsJsonObject().get("street").getAsString()).isEqualTo("Mira");

        }
    }
}
