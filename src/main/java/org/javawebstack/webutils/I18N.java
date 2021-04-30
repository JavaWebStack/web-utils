package org.javawebstack.webutils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class I18N {

    private Locale defaultLocale = Locale.ENGLISH;
    private final Map<Locale, Translation> translations = new HashMap<>();

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void set(Locale locale, Translation translation) {
        translations.put(locale, translation);
    }

    public String translate(Locale locale, String key, Object... params) {
        if(!translations.containsKey(locale))
            locale = getDefaultLocale();
        String msg = translations.get(locale).getMessage(key, params);
        if(msg.equals(key) && locale != getDefaultLocale())
            return translations.get(getDefaultLocale()).getMessage(key, params);
        return msg;
    }

    public String translate(String key, Object... params) {
        return translate(getDefaultLocale(), key, params);
    }

}
