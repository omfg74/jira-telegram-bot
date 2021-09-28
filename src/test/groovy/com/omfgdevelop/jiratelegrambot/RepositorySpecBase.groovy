package com.omfgdevelop.jiratelegrambot

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.web.WebAppConfiguration
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

/**
 * abstract root test class
 */
@ContextConfiguration(classes = [DbConfigGroovy.class,ObjectMapperConfig.class])
@TestPropertySource("classpath:test-application.properties")
@WebAppConfiguration
abstract class RepositorySpecBase extends Specification {

    public static final String JSONS_PATH = "src/test/resources/jsons/"


}
