package com.epam.esm.util.impl;

import com.epam.esm.util.LocaleTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleTranslatorImpl implements LocaleTranslator {
    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public LocaleTranslatorImpl(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public  String getString(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }

    @Override
    public String getString(String msgCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, args, locale);
    }
}
