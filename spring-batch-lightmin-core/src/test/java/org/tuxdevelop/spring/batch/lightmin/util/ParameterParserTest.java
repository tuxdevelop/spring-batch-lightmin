package org.tuxdevelop.spring.batch.lightmin.util;

import org.junit.Test;
import org.tuxdevelop.spring.batch.lightmin.exception.SpringBatchLightminApplicationException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterParserTest {

    @Test
    public void parseParametersTest() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                ParameterParser.DATE_FORMAT_WITH_TIMESTAMP);
        final String parameters = "testS(String)=56788,testL(Long)=56,testD(date)=2015/03/27 23:19:24:120";
        final Map<String, Object> parameterMap = ParameterParser
                .parseParameters(parameters);
        assertThat(parameterMap).isNotNull();
        assertThat(parameterMap).isNotEmpty();
        assertThat(parameterMap.containsKey("testS")).isTrue();
        assertThat(parameterMap.containsKey("testL")).isTrue();
        assertThat(parameterMap.containsKey("testD")).isTrue();
        final String stringValue = (String) parameterMap.get("testS");
        final Long longValue = (Long) parameterMap.get("testL");
        final Date dateValue = (Date) parameterMap.get("testD");
        assertThat(stringValue).isEqualTo("56788");
        assertThat(longValue).isEqualTo(56L);
        assertThat(simpleDateFormat.format(dateValue)).isEqualTo(
                "2015/03/27 23:19:24:120");
    }

    @Test
    public void parseParametersNullTest() {
        final Map<String, Object> parameterMap = ParameterParser
                .parseParameters(null);
        assertThat(parameterMap).isNotNull();
        assertThat(parameterMap).isEmpty();
    }

    @Test
    public void generateParameterStringEntryTest() {
        final String parameter = "test(String)=56788";
        final Entry<String, Object> entry = ParameterParser
                .generateParameterEntry(parameter);
        assertThat(entry.getKey()).isEqualTo("test");
        assertThat(entry.getValue()).isEqualTo("56788");
    }

    @Test
    public void generateParameterLongEntryTest() {
        final String parameter = "test(Long)=56";
        final Entry<String, Object> entry = ParameterParser
                .generateParameterEntry(parameter);
        assertThat(entry.getKey()).isEqualTo("test");
        assertThat(entry.getValue()).isEqualTo(56L);
    }

    @Test
    public void generateParameterDateEntryTest() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                ParameterParser.DATE_FORMAT_WITH_TIMESTAMP);
        final String parameter = "test(date)=2015/03/27 23:19:24:120";
        final Entry<String, Object> entry = ParameterParser
                .generateParameterEntry(parameter);
        assertThat(entry.getKey()).isEqualTo("test");
        assertThat(simpleDateFormat.format(entry.getValue())).isEqualTo(
                "2015/03/27 23:19:24:120");
    }

    @Test
    public void generateParameterDoubleEntryTest() {
        final String parameter = "test(Double)=5.2212";
        final Entry<String, Object> entry = ParameterParser.generateParameterEntry(parameter);
        assertThat(entry.getKey()).isEqualTo("test");
        assertThat(entry.getValue()).isEqualTo(5.2212);
    }

    @Test
    public void parseDateWithTimeStampTest() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                ParameterParser.DATE_FORMAT_WITH_TIMESTAMP);
        final String dateString = "2015/03/27 23:19:24:120";
        final Date date = ParameterParser.parseDate(dateString);
        assertThat(date).isNotNull();
        assertThat(simpleDateFormat.format(date)).isEqualTo(dateString);
    }

    @Test
    public void parseDateTest() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                ParameterParser.DATE_FORMAT);
        final String dateString = "2015/03/27";
        final Date date = ParameterParser.parseDate(dateString);
        assertThat(date).isNotNull();
        assertThat(simpleDateFormat.format(date)).isEqualTo(dateString);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void parseDateExceptionTest() {
        final String dateString = "2015/03:27";
        ParameterParser.parseDate(dateString);
    }

    @Test(expected = SpringBatchLightminApplicationException.class)
    public void createValueInstanceExceptionTest() {
        ParameterParser.createValueInstance("CLASS", "1234");
    }
}
