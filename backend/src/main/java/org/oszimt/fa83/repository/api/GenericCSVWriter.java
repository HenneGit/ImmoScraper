package org.oszimt.fa83.repository.api;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Entity;
import org.oszimt.fa83.pojo.ScrapeQuery;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GenericCSVWriter<T extends Entity> {

    public GenericCSVWriter() {
    }

    public Collection<T> getBeans(InputStreamReader reader, Class<? extends T> clazz) throws IOException {

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                .withSkipLines(0)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<T> it = csvToBean.iterator();
        List<T> all = new ArrayList<>();
        while (it.hasNext()){
            all.add(it.next());
        }
        reader.close();
        return all;

    }

    public void write(List<T> queries, Writer writer) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

        StatefulBeanToCsv<T> csvWriter = new StatefulBeanToCsvBuilder<T>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withOrderedResults(false)
                .build();
        csvWriter.write(queries);
        writer.close();
    }
}
