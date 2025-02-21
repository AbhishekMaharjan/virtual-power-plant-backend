package com.virtual_power_plant.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@Getter
@Setter
public class LogConfig {

    private static final String FILE_NAME = "initial.log";

    @Value("${logPathLocal}")
    private String logFilePrefix;

    @Bean
    public LoggerContext loggerContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Bean
    public PatternLayoutEncoder patternLayoutEncoder(LoggerContext loggerContext) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();
        return encoder;
    }

    @Bean
    public ConsoleAppender<ILoggingEvent> consoleAppender(LoggerContext loggerContext, PatternLayoutEncoder encoder) {
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setName("CONSOLE");
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();
        return consoleAppender;
    }

    @Bean
    public FileAppender<ILoggingEvent> fileAppender(LoggerContext loggerContext, PatternLayoutEncoder encoder) {
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setAppend(true);
        fileAppender.setName("FILE");
        fileAppender.setFile(latestFileName());
        fileAppender.setEncoder(encoder);
        fileAppender.start();
        return fileAppender;
    }

    public String latestFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date());
        return String.format(logFilePrefix, formattedDate);
    }

    @Bean
    public Logger rootLogger(LoggerContext loggerContext, ConsoleAppender<ILoggingEvent> consoleAppender,
                             FileAppender<ILoggingEvent> fileAppender) {
        Logger rootLogger = loggerContext.getLogger("com.virtual_power_plant");
        rootLogger.setLevel(Level.INFO);
        rootLogger.addAppender(consoleAppender);
        rootLogger.addAppender(fileAppender);
        return rootLogger;
    }

}
