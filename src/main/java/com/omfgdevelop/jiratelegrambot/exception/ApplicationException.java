package com.omfgdevelop.jiratelegrambot.exception;

import lombok.Getter;
import org.apache.commons.lang3.exception.ContextedException;

@Getter
public class ApplicationException extends ContextedException {

    private final String code;

    public ApplicationException(IApplicationError error) {
        super(error.getMessage());
        code = error.getCode();
    }

    public ApplicationException with(final String label, final Object value) {
        this.addContextValue(label, value);

        return this;
    }

}