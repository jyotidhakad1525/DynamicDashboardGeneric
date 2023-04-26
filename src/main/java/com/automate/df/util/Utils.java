package com.automate.df.util;

import com.automate.df.model.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Utils {

    static Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * Utility method to check if a string is empty.
     *
     * @param str Input string which has to be checked.
     * @return true, if checks if is empty
     */
    public static boolean isEmpty(String str) {
        return str == null || (str.trim().length() == 0);
    }

    /**
     * Utility method to check if a string is not empty.
     *
     * @param str Input string which has to be checked.
     * @return true, if checks if is not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * This method checks if the given map is NULL or Empty
     *
     * @param map instance of <code>java.util.Map</code>
     * @return true if this map is either NULL or contains no key-value
     * mappings.
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * This method checks if the given map is NOT NULL and NOT Empty
     *
     * @param map instance of <code>java.util.Map</code>
     * @return true if this map is NOT NULL and contains key-value mappings.
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


    /**
     * Checks if the given objArr is null or size 0
     *
     * @param objArr array of Object.
     * @return true if the array is null or size 0.
     */
    public static boolean isEmpty(Object[] objArr) {
        return (objArr == null) || (objArr.length < 1);
    }

    /**
     * Checks if the given objArr is not null and size not 0
     *
     * @param objArr array of Object.
     * @return true if the array is not null and size 0.
     */
    public static boolean isNotEmpty(Object[] objArr) {
        return !isEmpty(objArr);
    }

    /**
     * This method checks if the given list is null or is empty.
     *
     * @param listObj instance of java.util.List
     * @return true if list is null or size == 0
     */
    public static boolean isEmpty(List<?> listObj) {
        return listObj == null || listObj.isEmpty();
    }

    /**
     * This returns true is the given list is not null or not empty.
     *
     * @param listObj instance of java.util.List
     * @return boolean value.
     */
    public static boolean isNotEmpty(List<?> listObj) {
        return !isEmpty(listObj);
    }


    public static <T> String ObjectToJson(T t) {
        return ObjectToJson(t, false);
    }


    public static <T> String ObjectToJson(T t, boolean isPrety) {

        if (t == null) {
            return null;
        }
        String jsonData = null;
        ObjectMapper objMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            jsonData = isPrety ? objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t) :
                    objMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException", e);
        }
        log.info("{} jsonData : {} ", t.getClass().getSimpleName(), jsonData);
        return jsonData;
    }

    public static <T> T jsonToObject(String json, T t) {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            t = (T) objMapper.readValue(json, t.getClass());
        } catch (JsonProcessingException e) {
            log.error("" + e);
            return null;
        } catch (IOException e) {
            log.error("IOException", e);
            return null;
        }
        log.debug(t.getClass().getSimpleName());
        return t;
    }


    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

    public static <T> boolean isEmpty(T t) {
        return t == null;
    }


    @SuppressWarnings("unchecked")
    public static <T> T constructSuccessResponse(T t) {
        BaseResponse baseResponse = SuccessResponse();
        if (Utils.isEmpty(t)) {
            return (T) baseResponse;
        }
        BeanUtils.copyProperties(baseResponse, t);
        return t;
    }

    /**
     * @return
     */
    public static BaseResponse SuccessResponse() {
        BaseResponse baseResponse = new BaseResponse();
        ErrorMessages success = ErrorMessages.SUCCESS;
        baseResponse.setStatus(success.message());
        baseResponse.setStatusCode(success.code());
        baseResponse.setStatusDescription(success.message());
        return baseResponse;
    }

    public static <T> boolean checkMandetoryFieldsInclude(T t, List<String> fields, boolean isThrow) {
        return checkMandetoryFields(t, fields, "INCLUDE", isThrow);
    }

    private static <T> boolean checkMandetoryFields(T t, List<String> fields, String type, boolean isThrow) {

        Class<? extends Object> clz = t.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        List<String> missingFields = new ArrayList<>();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                String prop = field.getName();
                Object object = field.get(t);
                if (isEmpty(object) && fields.contains(prop) && !type.equalsIgnoreCase("EXCLUDE")) {
                    missingFields.add(prop);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isEmpty(missingFields)) {
            return true;
        } else if (isThrow) {
            String fildsData = String.join("|", missingFields);
            throw new BaseException(ErrorMessages.SOURCE_INPUT_REQUIRE, null, fildsData);
        } else {
            return false;
        }
    }

    public static Timestamp timeStamp() {
        return new Timestamp(new Date().getTime());
    }


    public static <S, T> List<T> copyListtoList(List<S> sourcelist, Class<T> clz) {

        List<T> targetList = new ArrayList<T>();
        try {
            for (S source : sourcelist) {
                T t = clz.newInstance();
                BeanUtils.copyProperties(source, t);
                targetList.add(t);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return targetList;
    }

    public static String formatDateToString(String toFormat, Date parsedDt) {
        SimpleDateFormat sdf = new SimpleDateFormat(toFormat);
        return sdf.format(parsedDt);
    }

    public static Date formatStringToDate(String date, String fromFormat) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(fromFormat);
        sdf1.setLenient(false);
        Date parsedDt;
        try {
            parsedDt = sdf1.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return parsedDt;
    }

}
