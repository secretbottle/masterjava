package ru.javaops.masterjava;

import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainXml {
    private final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    private final String projectName;

    public MainXml(String projectXml) throws JAXBException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(projectXml);
        Project project = JAXB_PARSER.unmarshal(inputStream);
        projectName = project.getName();
    }

    public List<User> getProjectUsers(String payloadxml) throws FileNotFoundException, JAXBException {
        List<User> projectUsers = new ArrayList<>();

        InputStream inputStream = new FileInputStream(payloadxml);
        Payload payload = JAXB_PARSER.unmarshal(inputStream);
        List<User> users = payload.getUsers().getUser();

        for (User user : users) {
            List<Group> groups = user.getGroups().getGroup();
            for (Group group : groups) {
                if (group.getName().contains(projectName)) {
                    projectUsers.add(user);
                    break;
                }
            }
        }

        return projectUsers;
    }
}
