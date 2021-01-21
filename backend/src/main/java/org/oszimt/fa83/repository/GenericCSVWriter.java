package org.oszimt.fa83.repository;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.oszimt.fa83.api.Entity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * a generic csv writer that writes a pojo extending entity.
 * @param <T>
 */
public class GenericCSVWriter<T extends Entity> {

    public GenericCSVWriter() {
    }

    /**
     * reads from csv and creates beans from it.
     * @param reader inputstream reader that has the file to read from.
     * @param clazz the pojo class to create.
     * @return Collection will all created beaands
     * @throws IOException thrown when something went wrong while reading.
     */
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

    /**
     * writes a list of entity extending pojos to a csv file.
     * @param beans the list of beans to write to csv.
     * @param writer the writer for writing to file.
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     * @throws IOException
     */
    public void write(List<T> beans, Writer writer) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {

        StatefulBeanToCsv<T> csvWriter = new StatefulBeanToCsvBuilder<T>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withOrderedResults(false)
                .build();
        csvWriter.write(beans);
        writer.close();
    }
}
