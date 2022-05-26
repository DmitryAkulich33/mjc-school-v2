package com.epam.esm.util;

public interface LocaleTranslator {
    String getString(String msgCode);
    String getString(String msgCode, Object... args);
}
