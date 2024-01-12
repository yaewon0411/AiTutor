package org.example.header;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import javax.swing.*;

@Configuration
public class FileHeaderConfiguration {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String OPENAI_API_KEY = "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";

    @Bean
    public RequestInterceptor fileRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header(AUTHORIZATION_HEADER, "Bearer "+OPENAI_API_KEY);
             }
        };
    }
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
