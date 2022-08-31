/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swt;

import java.util.List;

public class Cluster {

    private final String uuid;
    private final String preferredPhrase;
    private final List<String> deprecatedPhrases;
    private final boolean active;
    private final List<String> reuseDomains;
    private final String description;
    private final String language;

    public Cluster(String uuid, String preferredPhrase, List<String> deprecatedPhrases, boolean active, List<String> reuseDomains, String description, String language) {
        this.uuid = uuid;
        this.preferredPhrase = preferredPhrase;
        this.deprecatedPhrases = deprecatedPhrases;
        this.active = active;
        this.reuseDomains = reuseDomains;
        this.description = description;
        this.language = language;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPreferredPhrase() {
        return preferredPhrase;
    }

    public List<String> getDeprecatedPhrases() {
        return deprecatedPhrases;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getReuseDomains() {
        return reuseDomains;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }
}
