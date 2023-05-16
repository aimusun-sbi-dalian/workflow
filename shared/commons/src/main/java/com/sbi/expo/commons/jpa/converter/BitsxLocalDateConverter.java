package com.sbi.expo.commons.jpa.converter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yue.xia
 */
@Converter(autoApply = true)
@Slf4j
public class BitsxLocalDateConverter implements AttributeConverter<LocalDate, Integer> {
    private static final int YEAR_COEFFICIENT = 10000;
    private static final int MONTH_COEFFICIENT = 100;

    /**
     * @param attribute the entity attribute value to be converted
     * @return the field to keep in database with format yyyyMMdd
     */
    @Override
    public Integer convertToDatabaseColumn(LocalDate attribute) {
        log.debug("convert LocalDate to Integer: {}", attribute);
        return Objects.isNull(attribute)
                ? null
                : attribute.getYear() * YEAR_COEFFICIENT
                        + attribute.getMonthValue() * MONTH_COEFFICIENT
                        + attribute.getDayOfMonth();
    }

    /**
     * @param dbData the data from the database column to be converted, format is yyyyMMdd
     * @return the java attribute converted
     */
    @Override
    public LocalDate convertToEntityAttribute(Integer dbData) {
        if (Objects.isNull(dbData)) {
            return null;
        }
        log.debug("convert Integer to LocalDate: {}", dbData);
        int year = dbData / YEAR_COEFFICIENT;
        int month = dbData % YEAR_COEFFICIENT / MONTH_COEFFICIENT;
        int dayOfMonth = dbData % MONTH_COEFFICIENT;
        try {
            return LocalDate.of(year, month, dayOfMonth);
        } catch (DateTimeException e) {
            log.error("db data illegal: {}", dbData);
            return null;
        }
    }
}
