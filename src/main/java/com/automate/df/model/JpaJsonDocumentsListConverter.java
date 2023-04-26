package com.automate.df.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JpaJsonDocumentsListConverter implements AttributeConverter<List<String>, String> {

    private static final Logger log = LoggerFactory.getLogger(JpaJsonDocumentsListConverter.class);

    private final static ObjectMapper om = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> meta) {
        log.debug("convertToDatabaseColumn()..........");

        String jsonString = "";

        try {

            // convert list of POJO to json
            jsonString = om.writeValueAsString(meta);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        log.debug("convertToEntityAttribute()..........");

        List<String> list = new ArrayList<String>();

        try {

            // convert json to list
            list = (List<String>) om.readValue(dbData, ArrayList.class);

        } catch (JsonParseException e) {
            e.printStackTrace();

        } catch (JsonMappingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return list;
    }

}
