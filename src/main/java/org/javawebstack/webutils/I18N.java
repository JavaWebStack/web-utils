package org.javawebstack.webutils;

import java.util.ArrayList;
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
        if(translations.size() == 0)
            return null;
        if(!translations.containsKey(defaultLocale))
            return new ArrayList<>(translations.keySet()).get(0);
        return defaultLocale;
    }

    public void set(Locale locale, Translation translation) {
        translations.put(locale, translation);
    }

    public Translation get(Locale locale) {
        return translations.getOrDefault(locale, translations.get(getDefaultLocale()));
    }

    public Translation get() {
        return get(getDefaultLocale());
    }

    public String translate(Locale locale, String key, Object... params) {
        Translation translation = get(locale);
        String msg = translation.getMessage(key, params);
        if(msg.equals(key))
            return get(getDefaultLocale()).getMessage(key, params);
        return msg;
    }

    public String translate(String key, Object... params) {
        return translate(getDefaultLocale(), key, params);
    }

}
