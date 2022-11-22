/* Copyright (c) 2022-present Acrolinx GmbH */
package com.acrolinx.sidebar.reuse;

public enum ReuseMessage {
    SEARCH_FAILED("SEARCH_FAILED"),
    NO_SIDEBAR("NO_SIDEBAR"),
    NO_MESSAGE("NO_MESSAGE"),
    INVALID_SENTENCE_OR_DOCUMENT_TYPE("INVALID_SENTENCE_OR_DOCUMENT_TYPE"),
    TARGET_DOES_NOT_SUPPORT_LIVE("TARGET_DOES_NOT_SUPPORT_LIVE");

    private final String message;

    /**
     * @param text
     */
    ReuseMessage(final String text) {
        this.message = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return message;
    }
}
