
package acme.components.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class LocalisedBooleanFormatter implements Formatter<Boolean> {

	@Override
	public String print(final Boolean object, final Locale locale) {
		String result;

		if (locale.getLanguage().equals("en"))
			if (object)
				result = "true";
			else
				result = "false";
		else if (!object)
			result = "No";
		else
			result = "Sí";
		return result;
	}

	@Override
	public Boolean parse(final String text, final Locale locale) throws ParseException {
		boolean result = false;

		if (text.equals("true") || text.equals("Sí"))
			result = true;

		return result;
	}

}
