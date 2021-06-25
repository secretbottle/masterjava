package ru.javaops.masterjava;

import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MainXMLStax {
    private String projectName;

    public MainXMLStax(String projectXml) {
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Paths.get(projectXml)))) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT &&
                        "name".equals(reader.getLocalName())) {
                    projectName = reader.getElementText();
                    break;
                }
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getProjectUsers(String payloadxml) {
        Map<String, String> users = new HashMap<>();

        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Paths.get(payloadxml)))) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.END_ELEMENT && "Users".equals(reader.getLocalName())) {
                    break;
                }

                if (event == XMLEvent.START_ELEMENT && "User".equals(reader.getLocalName())) {
                    String email = reader.getAttributeValue(null, "email");
                    String fullname = null;
                    while (reader.hasNext()) {
                        event = reader.next();

                        if (event == XMLEvent.END_ELEMENT && "User".equals(reader.getLocalName())) {
                            break;
                        } else if (event == XMLEvent.END_ELEMENT && "Groups".equals(reader.getLocalName())) {
                            break;
                        } else if (event == XMLEvent.START_ELEMENT && "fullName".equals(reader.getLocalName())) {
                            fullname = reader.getElementText();
                        } else if (event == XMLEvent.START_ELEMENT && "Groups".equals(reader.getLocalName())) {
                            while (reader.hasNext()) {
                                event = reader.next();

                                if (event == XMLEvent.END_ELEMENT && "Groups".equals(reader.getLocalName())) {
                                    break;
                                } else if (event == XMLEvent.START_ELEMENT && "name".equals(reader.getLocalName())
                                        && reader.getElementText().contains(projectName)){
                                    users.put(email, fullname);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
        return users;
    }

}
