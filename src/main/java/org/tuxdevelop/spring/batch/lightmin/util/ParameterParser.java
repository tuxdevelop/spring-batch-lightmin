package org.tuxdevelop.spring.batch.lightmin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.tuxdevelop.spring.batch.lightmin.execption.SpringBatchLightminApplicationException;

@Slf4j
public class ParameterParser {

	public static final String DATE_FORMAT_WITH_TIMESTAMP = "yyyy/MM/dd HH:mm:ss:SSS";
	public static final String DATE_FORMAT = "yyyy/MM/dd";

	private static final SimpleDateFormat simpleDateFormatTimeStamp = new SimpleDateFormat(DATE_FORMAT_WITH_TIMESTAMP);

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

	private ParameterParser() {
	}

	public static JobParameters parseParametersToJobParameters(final String parameters) {
		final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		final Map<String, Object> parametersMap = parseParameters(parameters);
		for (final Map.Entry<String, Object> entry : parametersMap.entrySet()) {
			final Object value = entry.getValue();
			if (value instanceof String) {
				jobParametersBuilder.addString(entry.getKey(), (String) value);
			} else if (value instanceof Long) {
				jobParametersBuilder.addLong(entry.getKey(), (Long) value);
			} else if (value instanceof Date) {
				jobParametersBuilder.addDate(entry.getKey(), (Date) value);
			} else if (value instanceof Double) {
				jobParametersBuilder.addDouble(entry.getKey(), (Double) value);
			} else {
				throw new SpringBatchLightminApplicationException("Unknown Parameter type:"
						+ value.getClass().getName());
			}
		}
		return jobParametersBuilder.toJobParameters();
	}

	public static Map<String, Object> parseParameters(final String parameters) {
		final Map<String, Object> parameterMap;
		if (parameters == null || parameters.isEmpty()) {
			log.info("parameters null or empty, nothing to map!");
			parameterMap = new HashMap<String, Object>();
		} else {
			final List<String> parameterList = splitParameters(parameters);
			parameterMap = evaluateParameters(parameterList);
		}
		return parameterMap;
	}

	static List<String> splitParameters(final String parameters) {
		final List<String> parameterList = new LinkedList<String>();
		final String[] parametersArray = parameters.split(",");
		for (final String parameter : parametersArray) {
			parameterList.add(parameter);
		}
		return parameterList;
	}

	static Map<String, Object> evaluateParameters(final List<String> parameterList) {
		final Map<String, Object> parameters = new HashMap<String, Object>();
		for (final String parameter : parameterList) {
			final Map.Entry<String, Object> entry = generateParameterEntry(parameter);
			parameters.put(entry.getKey(), entry.getValue());
		}
		return parameters;
	}

	static Entry<String, Object> generateParameterEntry(final String paramater) {
		final String key;
		final Object value;
		log.debug("parsing: " + paramater);
		final String[] firstSplit = paramater.split(Pattern.quote("("), 2);
		key = firstSplit[0].trim();
		log.debug("got key: " + key);
		final String tempString = firstSplit[1];
		log.debug("parsing second part: " + tempString);
		final String[] secondSplit = tempString.split(Pattern.quote(")"), 2);
		final String type = secondSplit[0].trim();
		log.debug("got type: " + type);
		final String tempSecondString = secondSplit[1];
		log.debug("parsing third part:" + tempSecondString);
		final String[] thirdSplit = tempSecondString.split("=", 2);
		final String valueString = thirdSplit[1];
		log.debug("got value: " + valueString);
		value = createValueInstance(type, valueString);
		return new AbstractMap.SimpleEntry<String, Object>(key, value);
	}

	static Object createValueInstance(final String type, final String valueString) {
		final String upperCaseType = type.toUpperCase();
		final Object value;
		if (StringTypes.STRING.typeString.equals(upperCaseType)) {
			value = new String(valueString);
		} else if (StringTypes.LONG.typeString.equals(upperCaseType)) {
			value = Long.parseLong(valueString);
		} else if (StringTypes.DOUBLE.typeString.equals(upperCaseType)) {
			value = Double.parseDouble(valueString);
		} else if (StringTypes.DATE.typeString.equals(upperCaseType)) {
			value = parseDate(valueString);
		} else {
			throw new SpringBatchLightminApplicationException("Unknown parameter Type: " + type);
		}
		return value;
	}

	static Date parseDate(final String dateString) {
		Date date;
		try {
			date = simpleDateFormatTimeStamp.parse(dateString);
		} catch (final ParseException e) {
			log.info("could not parse date: " + dateString);
			try {
				date = simpleDateFormat.parse(dateString);
			} catch (final ParseException e1) {
				log.error(e.getMessage());
				throw new SpringBatchLightminApplicationException(e1, e1.getMessage());
			}
		}
		return date;
	}

	private enum StringTypes {

		STRING("STRING"), LONG("LONG"), DATE("DATE"), DOUBLE("DOUBLE");

		private String typeString;

		private StringTypes(final String typeString) {
			this.typeString = typeString;
		}
	}

}
