package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.Contact;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DescriptionMapToCranPackageProcessor implements Processor {

    private final String DATE_PATTERN = "yyyy-dd-MM";
    private final String CONTACT_SEPARATOR_REGEX = ", | and ";
    private final String NAME_AND_EMAIL_REGEX = "([a-zA-Z .\\-]+) <([a-zA-Z0-9_.\\-]+@[a-zA-Z0-9_.\\-]+?\\.[a-zA-Z]{2,4})>";

    @Override
    public void process(Exchange exchange) {
        exchange.setPattern(ExchangePattern.InOut);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());

        final Map<String, String> descriptionMap = exchange.getIn().getBody(Map.class);

        final CranPackage cranPackage = new CranPackage(
                descriptionMap.get("Package"),
                descriptionMap.get("Version"),
                mapDate(descriptionMap.get("Date")),
                descriptionMap.get("Title"),
                descriptionMap.get("Description"),
                mapContact(descriptionMap.get("Author")),
                mapContact(descriptionMap.get("Maintainer"))
        );

        exchange.getOut().setBody(cranPackage);
    }

    private Date mapDate(String date) {
        final DateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Contact> mapContact(String contactsStr) {
        if(contactsStr==null || contactsStr.isEmpty())
            return Collections.emptyList();

        String[] parts = contactsStr.split(CONTACT_SEPARATOR_REGEX);
        return Arrays.stream(parts)
                .map(s -> {
                    Pattern pattern = Pattern.compile(NAME_AND_EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find()) return new Contact(matcher.group(1), matcher.group(2));
                    else return new Contact(s);
                })
                .collect(Collectors.toList());
    }
}