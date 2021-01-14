package org.oszimt.fa83.repository.api;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

public class GenericCSVWriter<T extends Entity> {

    public GenericCSVWriter() {
    }

    public Collection<T> getBeans(InputStreamReader reader, Class<? extends T> clazz) {

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();

    }

    public void write(List<T> queries, Writer writer) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        StatefulBeanToCsv<T> csvWriter = new StatefulBeanToCsvBuilder<T>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .withOrderedResults(false)
                .build();

        csvWriter.write(queries);
    }
}
