package com.npwit.gateway;

import com.netflix.zuul.context.RequestContext;
import com.npwit.gateway.GatewayApplication;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GatewayApplication.class)
public class GatewayApplicationTest {

    @Autowired
    private TestRestTemplate rest;

    static ConfigurableApplicationContext bookService;

    @BeforeClass
    public static void startBookService() {
        bookService = SpringApplication.run(BookService.class,
                "--server.port=8601");
    }

    @AfterClass
    public static void closeBookService() {
        bookService.close();
    }

    @Before
    public void setup() {
        RequestContext.testSetCurrentContext(new RequestContext());
    }

    @Test
    public void test() {
        String resp = rest.getForObject("/poc-cielo-api-eurekaclient/available", String.class);
        assertThat(resp).isEqualTo("poc-cielo-api-eurekaclient");
    }

    @Configuration
    @EnableAutoConfiguration
    @RestController
    static class BookService {
        @RequestMapping("/available")
        public String getAvailable() {
            return "poc-cielo-api-eurekaclient";
        }
    }
}
