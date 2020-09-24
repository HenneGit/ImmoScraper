package org.oszimt.fa83.repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.pojo.SearchQuery;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class SearchQueryFileWriter {

    private final String FILE_PATH = "queries.csv";

    public SearchQueryFileWriter() {
    }

    public Collection<SearchQuery> findAll() {

        try(Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            CsvToBean<SearchQuery> csvToBean = new CsvToBeanBuilder<SearchQuery>(reader)
                    .withType(SearchQuery.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }

    public void write(List<SearchQuery> queries){

        try(Writer writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {

            StatefulBeanToCsv<SearchQuery> csvWriter = new StatefulBeanToCsvBuilder<SearchQuery>(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .withOrderedResults(false)
                    .build();

            csvWriter.write(queries);
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e){
            e.printStackTrace();
        }
    }

}