package de.jenswurm.assertifier;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JSONAssertifier {

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String customDateFormat = null;

	ObjectMapper om = new ObjectMapper();

	private boolean relativeIndexing = true;

	public JSONAssertifier() {

		SimpleModule isoDateTimeModule = new SimpleModule("LocalDateModule", new Version(1, 0, 0, null));
		isoDateTimeModule.addSerializer(this.new CustomDateSerializer(Date.class));
		isoDateTimeModule.addSerializer(this.new CustomLocalDateSerializer(LocalDate.class));
		om.registerModule(isoDateTimeModule);
		om.configure(SerializationFeature.INDENT_OUTPUT, true);
		om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		om.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		om.setSerializationInclusion(Include.NON_EMPTY);
	}

	public void assertify(Object object, String name) {

		System.out.println();
		assertifyInternal(object, name);

	}

	/**
	 * Marshals a class as a json string, nicely formatted.
	 * 
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public String[] marshalAsJSON(Object obj, Class... clazz) {
		String string = marshall(obj);
		// remove linebreaks preceded by comma
		string = string.replaceAll(",\\r", "");
		// remove other linebreaks
		string = string.replaceAll("\\r", "");
		// remove quotation marks
		string = string.replaceAll("\"", "");

		// split at newline characters
		String[] split = string.split("\n");
		return split;
	}

	/**
	 * Adds names of methods to the ignore list
	 * 
	 * @param string
	 * @return
	 */
	public JSONAssertifier ignore(String... string) {
		this.ignoreList.addAll(Arrays.asList(string));
		return this;
	}

	private void assertifyInternal(Object object, String name) {

		String nameString = name + "JSON";
		String indexString = name + "Index";
		String[] jsonString = marshalAsJSON(object);
		System.out.println("String[] " + nameString + " = new JSONAssertifier()" + buildDateFormatCommand()
				+ ".marshalAsJSON(" + name + ");");

		if (relativeIndexing) {
			// relative indexing
			System.out.println("int " + indexString + " = 0;");
			for (String line : jsonString) {

				System.out.println(skipLinePrefix(line, indexString) + "Assert.assertEquals(\"" + line + "\", "
						+ nameString + "[" + indexString + "++]);");
			}
		} else {
			// absolute indexing
			int i = 0;
			for (String line : jsonString) {
				// the first line with the xml header is irrelevant
				String string = skipLinePrefix(line) + "Assert.assertEquals(\"" + line + "\", " + nameString + "[" + i
						+ "]);";
				System.out.println(string);
				i++;
			}
		}

	}

	private String buildDateFormatCommand() {
		if (customDateFormat != null) {
			return ".marshalDateFormat(\"" + customDateFormat + "\")";
		}
		return "";
	}

	private List<String> ignoreList = new ArrayList<String>();

	public JSONAssertifier absoluteIndexing() {
		relativeIndexing = true;
		return this;
	}

	/**
	 * Returns a // comment in order to remove a line from being evaluated
	 * 
	 * @param line
	 * @param indexString
	 * @return
	 */
	private String skipLinePrefix(String line, String indexString) {
		for (String currIgnore : ignoreList) {
			if (line.contains(currIgnore)) {
				return indexString + "++; // ";
			}
		}
		return "";
	}

	/**
	 * Returns a // comment in order to remove a line from being evaluated
	 * 
	 * @param line
	 * @param indexString
	 * @return
	 */
	private String skipLinePrefix(String line) {
		for (String currIgnore : ignoreList) {
			if (line.contains(currIgnore)) {
				return "// ";
			}
		}
		return "";
	}

	private String marshall(Object object) {
		try {
			return om.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	class CustomLocalDateSerializer extends StdSerializer<LocalDate> {

		protected CustomLocalDateSerializer(Class<LocalDate> t) {
			super(t);
		}

		private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		@Override
		public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider arg2)
				throws IOException, JsonProcessingException {
			gen.writeString(formatter.print(value));
		}
	}

	class CustomDateSerializer extends StdSerializer<Date> {

		protected CustomDateSerializer(Class<Date> t) {
			super(t);
		}

		@Override
		public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
				throws IOException, JsonProcessingException {

			gen.writeString(formatter.format(value));
		}
	}

	/**
	 * Use this date format for marshalling Date instances
	 * 
	 * @param e.g.
	 *            yyyy-MM-dd
	 * @return
	 */
	public JSONAssertifier marshalDateFormat(String dateFormat) {
		this.customDateFormat = dateFormat;
		this.formatter = new SimpleDateFormat(customDateFormat);
		return this;
	}
}
