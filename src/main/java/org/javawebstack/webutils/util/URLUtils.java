package org.javawebstack.webutils.util;

import org.javawebstack.abstractdata.util.QueryString;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLUtils {

    public static String urlEncode(String content) {
        try {
            return URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String urlDecode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class URLBuilder {

        private String protocol = "http";
        private String domain = "localhost";
        private String path = "";
        private QueryString queryParameters = new QueryString();

        public URLBuilder() {

        }

        public String build() {
            String url = protocol + "://" + domain;

            url += getFullPath();

            return url;
        }

        public URLBuilder setDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public URLBuilder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public String getDomain() {
            return domain;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getPath() {
            return path;
        }

        public String getFullPath() {
            String fullPath = "";
            if (!path.equals(""))
                fullPath += (path.startsWith("/") ? "" : "/") + path;

            if (queryParameters.size() > 0)
                fullPath += (fullPath.contains("/") ? "" : "/") + "?" + queryParameters.toString();

            return fullPath;
        }

        public QueryString getQueryParameters() {
            return queryParameters;
        }

        public String getQueryParameter(String key) {
            return queryParameters.get(key);
        }

        public URLBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public URLBuilder setQueryParameters(QueryString queryParameters) {
            this.queryParameters = queryParameters;
            return this;
        }

        public URLBuilder setQueryParameter(String key, String value) {
            queryParameters.set(key, value);
            return this;
        }

        public String toString() {
            return build();
        }

        public static URLBuilder from(String url) {
            URLBuilder urlBuilder = new URLBuilder();
            String[] protocolAndUrl = url.split("://", 2);
            urlBuilder.protocol = protocolAndUrl[0];
            if (protocolAndUrl.length > 1) {
                String[] domainAndPath = protocolAndUrl[1].split("/", 2);
                urlBuilder.domain = domainAndPath[0];
                if (domainAndPath.length > 1) {
                    String[] pathAndQueryParameters = domainAndPath[1].split("\\?", 2);
                    urlBuilder.path = pathAndQueryParameters[0];
                    if (pathAndQueryParameters.length > 1) {
                        String[] queryParameters = pathAndQueryParameters[1].split("&");
                        for (String queryParameter : queryParameters) {
                            String[] keyValue = queryParameter.split("=", 2);
                            urlBuilder.queryParameters.set(URLUtils.urlDecode(keyValue[0]), URLUtils.urlDecode(keyValue.length > 1 ? keyValue[1] : ""));
                        }
                    }
                }
            }
            return urlBuilder;
        }

    }

}
