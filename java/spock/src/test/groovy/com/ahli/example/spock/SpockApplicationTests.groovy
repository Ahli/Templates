package com.ahli.example.spock


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class SpockApplicationTests extends Specification {

    @Autowired
    WebController webController

    def "context loads"() {
        when: "Spring started"
        true
        then: "beans are created"
        webController != null
    }

}
