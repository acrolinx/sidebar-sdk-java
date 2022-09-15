package com.acrolinx.sidebar.reuse;

public class QueryInfo {

    public static QueryInfo emptyInfo = new QueryInfo("","");

    private final String originalString;

    private final String queryString;
    private final String nodeType;

    public QueryInfo(String originalString, String nodeType) {
        this.originalString = originalString;
        this.queryString = originalString.trim();
        this.nodeType = nodeType;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getOriginalString() {
        return originalString;
    }
}
