package com.omfgdevelop.jiratelegrambot.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.omfgdevelop.jiratelegrambot.exception.ExceptionsConstants.METHOD_NAME;

@Aspect
@Component
@Log4j2
public class MessageLogAspect {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Pointcut("execution(public * com.omfgdevelop.jiratelegrambot.controller.TelegramController.*(..))")
    public void callUserRequest() {
    }

    @Before("callUserRequest()")
    public void beforeCallUserRequest(JoinPoint jp) {
        logRequest(jp.getSignature().getName(), jp.getArgs()[0]);
    }

    private void logRequest(String methodName, Object data) {
        try {
            log.info(new EcsEvent("Request from mobile device.").withContext("methodname", methodName).withContext("requestBody", MAPPER.writeValueAsString(data).replace("\\", "")));
        } catch (Exception e) {
            log.error(new EcsEvent("Error writing request log.", e).withContext(METHOD_NAME, methodName));
        }
    }
}
