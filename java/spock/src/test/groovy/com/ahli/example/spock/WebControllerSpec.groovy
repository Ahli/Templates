package com.ahli.example.spock

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import spock.lang.Specification

@ContextConfiguration // bug workaround: https://github.com/spockframework/spock/issues/1539
@SpringBootTest
@EnableWebMvc
@AutoConfigureMockMvc
class WebControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def "/hello returns valid"() {
        when: "calling GET /hello"
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/hello")).andReturn();

        then: "status is OK"
        result.getResponse().getStatus() == HttpStatus.OK.value()
        and: "content as expected"
        result.getResponse().getContentAsString().equals("Hello world!")
    }

}