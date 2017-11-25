package com.ebook.fx.core.model;

import java.sql.Date;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maykoone
 */
public class LocalDateAttributeConverterTest {

    LocalDateAttributeConverter  subject;
    
    @Before
    public void setUp() {
        subject = new LocalDateAttributeConverter();
    }

    @Test
    public void given_localdate_isnull_when_convert_then_sql_date_should_not_be_null() {
        System.out.println("Given Local Date is null when convert to Sql Date then the result shouldn't be null");
        LocalDate date = null;
        Date result = subject.convertToDatabaseColumn(date);
        assertNotNull(result);
    }

    @Test
    public void given_non_null_localdate_when_converted_then_sqldate_should_be_same_time() {
        System.out.println("Given a non null Local Date when it is converted then the returned Sql Date should be same time");
        LocalDate date = LocalDate.now();
        Date expResult = Date.valueOf(date);
        Date result = subject.convertToDatabaseColumn(date);
        assertEquals(expResult, result);
    }
    
}
