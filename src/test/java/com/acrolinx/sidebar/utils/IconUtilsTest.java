/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class IconUtilsTest
{
    @Test
    void getAcrolinxIcon16()
    {
        assertNotNull(IconUtils.getAcrolinxIcon_16_16_AsStream());
    }

    @Test
    void getAcrolinxIcon32()
    {
        assertNotNull(IconUtils.getAcrolinxIcon_32_32_AsStream());
    }
}
