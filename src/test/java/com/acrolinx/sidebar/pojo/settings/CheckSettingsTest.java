/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckSettingsTest
{
    @Test
    void toStringTest()
    {
        CheckSettings checkSettings = new CheckSettings("en", "foo", new String[]{"buzz"}, true, true, true, true, true,
                true, new String[]{"buzz"});

        Assertions.assertEquals(
                "{\"language\":\"en\",\"ruleSetName\":\"foo\",\"termSets\":[\"buzz\"],\"checkSpelling\":true,\"checkGrammar\":true,\"checkStyle\":true,\"checkReuse\":true,\"harvestTerms\":true,\"checkSeo\":true,\"termStatuses\":[\"buzz\"]}",
                checkSettings.toString());
    }
}