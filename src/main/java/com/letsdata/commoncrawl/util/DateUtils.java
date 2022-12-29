package com.letsdata.commoncrawl.util;

import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.resonance.letsdata.data.util.StringFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static Date getDateFromDateString(String dateStr) {
        StringFunctions.validateStringIsNotBlank(dateStr, "date str should not be blank");
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WARCFileReaderConstants.WARC_INFO_EXTRACTED_DATE_FORMAT);
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException pe) {
            logger.warn("Could not parse date in from WARC_INFO_EXTRACTED_DATE_FORMAT "+dateStr, pe);
        }

        if (date != null) {
            return date;
        }

        logger.info("Trying to parse date as WARC_DATE_FORMAT ");
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WARCFileReaderConstants.WARC_DATE_FORMAT, Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException pe) {
            logger.warn("Could not parse date in from WARC_DATE_FORMAT "+dateStr, pe);
            throw new RuntimeException("Could not parse date in from WARC_DATE_FORMAT "+dateStr, pe);
        }

        if (date == null) {
            throw new RuntimeException("Could not parse date");
        }

        return date;
    }
}
