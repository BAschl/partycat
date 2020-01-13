package at.aschl.pgs.configuration.properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Validated
@Configuration
@ConfigurationProperties("spring.application")
public class ApplicationConfigurationProperties extends WebMvcConfigurerAdapter {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private GsonHttpMessageConverter customGsonHttpMessageConverter() {
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'")
            .create();

        GsonHttpMessageConverter gsonMessageConverter = new GsonHttpMessageConverter();
        gsonMessageConverter.setGson(gson);

        return gsonMessageConverter;
    }
}
