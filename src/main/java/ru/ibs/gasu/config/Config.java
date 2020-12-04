package ru.ibs.gasu.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.modelmapper.config.Configuration.AccessLevel.PUBLIC;
@SuppressWarnings("Duplicates")
@Configuration
@PropertySource({"classpath:application.properties"})

public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PUBLIC);
        return mapper;

    };


}
