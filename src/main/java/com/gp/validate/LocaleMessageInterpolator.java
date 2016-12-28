package com.gp.validate;

import java.util.Locale;

import javax.validation.MessageInterpolator;

public class LocaleMessageInterpolator implements MessageInterpolator {
   
	private final Locale defaultLocale;
	
	private final MessageInterpolator delegate;  
	
    public LocaleMessageInterpolator(MessageInterpolator delegate, Locale locale) {
        this.delegate = delegate;
        this.defaultLocale = locale;
    }

    @Override
    public String interpolate(String s, Context context) {

        return delegate.interpolate(s, context, defaultLocale);
    }
   
    @Override
    public String interpolate(String s, Context context, Locale locale) {

        return delegate.interpolate(s, context, locale);
    }
}
