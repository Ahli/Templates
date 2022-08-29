package com.ahli.example.spock


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class SpockApplicationTest extends Specification {

    // test examples: https://github.com/spockframework/spock-example/tree/master/src/test/groovy

    @Autowired
    WebController webController

    def "context loads"() {
        expect:
        webController != null
    }

}
