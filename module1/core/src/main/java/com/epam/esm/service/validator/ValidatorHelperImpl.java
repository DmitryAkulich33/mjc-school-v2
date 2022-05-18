package com.epam.esm.service.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorHelperImpl implements ValidatorHelper {
    @Override
    public boolean validateStringParameter(String regex, String parameter) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parameter);
        return matcher.matches();
    }
}
