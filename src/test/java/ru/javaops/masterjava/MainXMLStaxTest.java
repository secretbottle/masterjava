package ru.javaops.masterjava;

import org.junit.Test;

import java.util.Map;

public class MainXMLStaxTest {

    @Test
    public void getProjectUsers() {
        MainXMLStax mainXMLStax = new MainXMLStax("src/test/resources/project.xml");
        Map<String, String> users = mainXMLStax.getProjectUsers("src/test/resources/payload.xml");
        users.forEach((x,y) -> System.out.println(x + " - " + y));
    }
}