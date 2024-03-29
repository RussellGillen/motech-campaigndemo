package org.motechproject.mobileforms.api.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.motechproject.mobileforms.api.domain.FormBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

@Component
public class MapToBeanConvertor {
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    private BeanUtilsBean beanUtilsBean;

    public MapToBeanConvertor() {
        beanUtilsBean = new BeanUtilsBean(new EnumAndDateConverter());
    }

    public void convert(FormBean formBean, Map<String, String> attributeMap) throws InvocationTargetException, IllegalAccessException {
        beanUtilsBean.populate(formBean, attributeMap);
    }

    class EnumAndDateConverter extends ConvertUtilsBean {

        private final EnumConverter enumConverter = new EnumConverter();
        private final DateConverter dateConverter = new DateConverter();

        public EnumAndDateConverter() {
            dateConverter.setPattern(DATE_PATTERN);
        }

        public Converter lookup(Class clazz) {
            final Converter converter = super.lookup(clazz);
            if (converter == null && clazz.isEnum()) {
                return enumConverter;
            } else if (clazz == Date.class) {
                return dateConverter;
            } else {
                return converter;
            }
        }

        private class EnumConverter implements Converter {
            public Object convert(Class type, Object value) {
                return Enum.valueOf(type, (String) value);
            }
        }
    }
}
