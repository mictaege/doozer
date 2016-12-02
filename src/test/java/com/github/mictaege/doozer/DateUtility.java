package com.github.mictaege.doozer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** */
public final class DateUtility {

    public static Date asDay(final String yyyyMMdd) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(yyyyMMdd);
    }

    private DateUtility() {
        super();
    }
}
