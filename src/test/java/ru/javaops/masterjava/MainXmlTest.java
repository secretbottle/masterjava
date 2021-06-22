package ru.javaops.masterjava;

import org.junit.Test;
import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

public class MainXmlTest {

    @Test
    public void getProjectUsers() throws Exception {
        MainXml mainXml = new MainXml("src/test/resources/project.xml");
        List<User> users = mainXml.getProjectUsers("src/test/resources/payload.xml");
        users.forEach(user -> System.out.println(user.getFullName()));
    }
}