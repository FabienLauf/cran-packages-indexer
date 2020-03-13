package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static final Map<String, String> dcfToMap(String dbf) {
        return Arrays.stream(dbf.split("\n"))
                .filter(s -> s.matches("^\\w+: .+$"))
                .map(s -> s.split(": ", 2)) //"Description: foo:bar" -> ["Description", "foo:bar"]
                .collect(Collectors.toMap(k -> k[0], v -> v[1]));
    }
}
