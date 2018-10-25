/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Test;

public class IconUtilsTest
{
    @Test
    public void getAcrolinxIcon16()
    {
        InputStream acrolinxIcon_16_16_asStream = IconUtils.getAcrolinxIcon_16_16_AsStream();
        assertTrue(acrolinxIcon_16_16_asStream != null);
    }

    @Test
    public void getAcrolinxIcon32()
    {
        InputStream acrolinxIcon_32_32_asStream = IconUtils.getAcrolinxIcon_32_32_AsStream();
        assertTrue(acrolinxIcon_32_32_asStream != null);
    }

}