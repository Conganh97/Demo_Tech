package com.demo.i18n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    public String getMessage(String key, Object... args) throws NoSuchMessageException {
        return messageSource.getMessage(key, args, locale);
    }

    public String getMessage(String key) {
        try {
            return this.getMessage(key, (Object) null);
        } catch (Exception e) {
            return "";
        }

    }
}