package com.ebook.fx.core.model;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author maykoone
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate date) {
        return Date.valueOf(date == null ? LocalDate.now() : date);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date value) {
        Instant instant = value == null ? Instant.now() : value.toInstant();
        return LocalDate.from(instant);
    }
}
