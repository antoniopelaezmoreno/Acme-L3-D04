
package acme.components.formatters;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class FormatterRegistrar implements WebMvcConfigurer {

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		LocalisedBooleanFormatter booleanFormater;

		booleanFormater = new LocalisedBooleanFormatter();
		registry.addFormatter(booleanFormater);
	}

}
