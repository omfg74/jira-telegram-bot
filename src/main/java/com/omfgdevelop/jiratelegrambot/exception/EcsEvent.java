package com.omfgdevelop.jiratelegrambot.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.util.MultiFormatStringBuilderFormattable;

import java.util.HashMap;

public class EcsEvent implements MultiFormatStringBuilderFormattable {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private final transient HashMap<String, Object> msg = new HashMap<>();

    public EcsEvent(String message, Throwable ex) {
        this(message);
        with(ex);
    }

    public EcsEvent(String message) {
        with(message);
    }

    public EcsEvent with(Throwable ex) {
        msg.put("error.message", ex.getMessage());
        msg.put("error.stack_trace", ExceptionUtils.getStackTrace(ex));

        return this;
    }

    public EcsEvent with(String message) {
        msg.put("message", message);

        return this;
    }

    public EcsEvent with(ApplicationException ex) {
        msg.put("error.code", ex.getCode());

        return with((Throwable) ex);
    }

    public EcsEvent withContext(String key, Object value) {
        msg.put("ctx." + key, value);

        return this;
    }

    @Override
    public void formatTo(String[] formats, StringBuilder buffer) {
        buffer.append(toString());
    }

    @SneakyThrows
    @Override
    public String toString() {
        return objectMapper.writeValueAsString(msg);
    }

    @Override
    public String getFormattedMessage(String[] formats) {
        return toString();
    }

    @Override
    public String[] getFormats() {
        return new String[]{"JSON"};
    }

    @Override
    public String getFormattedMessage() {
        return toString();
    }

    @Override
    public String getFormat() {
        return "JSON";
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }

    @Override
    public void formatTo(StringBuilder buffer) {
        buffer.append(toString());
    }
}

