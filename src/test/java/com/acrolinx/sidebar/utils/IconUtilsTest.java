/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IconUtilsTest
{
    @Test
    public void getAcrolinxIcon16()
    {
        assertNotNull(IconUtils.getAcrolinxIcon_16_16_AsStream());
    }

    @Test
    public void getAcrolinxIcon32()
    {
        assertNotNull(IconUtils.getAcrolinxIcon_32_32_AsStream());
    }
}