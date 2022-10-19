package ru.job4j.pooh;

import java.util.Optional;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        return new Req(
                parseHttpRequestType(content),
                parsePoohMode(content),
                parseSourceName(content),
                parseParam(content)
        );
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

    private static String parseHttpRequestType(String content) {
        String httpRequestType = null;
        String type = content.substring(0, content.indexOf(" "));
        if ("GET".equals(type)) {
            httpRequestType = "GET";
        }
        if ("POST".equals(type)) {
            httpRequestType = "POST";
        }
        return httpRequestType;
    }

    private static String parsePoohMode(String content) {
        String poohMode = null;
        String subs = content.substring(0, content.indexOf(System.lineSeparator()));
        subs = subs.substring(subs.indexOf(" "), subs.lastIndexOf(" "));
        String parseMode = subs.split("/", 3)[1];
        if ("queue".equals(parseMode)) {
            poohMode = "queue";
        }
        if ("topic".equals(parseMode)) {
            poohMode = "topic";
        }
        return poohMode;
    }

    private static String parseSourceName(String content) {
        String subs = content.substring(0, content.indexOf(System.lineSeparator()));
        subs = subs.substring(subs.indexOf(" "), subs.lastIndexOf(" "));
        return subs.split("/", 3)[2];
    }

    private static String parseParam(String content) {
        String param = null;
        Optional<String> contentLengthLine = content.lines().filter(s -> s.startsWith("Content-Length: ")).findFirst();
        if (contentLengthLine.isPresent()) {
            int contentLengthInt = Integer.parseInt(contentLengthLine.get()
                    .substring(contentLengthLine.get().lastIndexOf(": ") + 2));
            param = content.substring(content.length() - contentLengthInt - System.lineSeparator().length()).trim();
        }
        return param;
    }
}
