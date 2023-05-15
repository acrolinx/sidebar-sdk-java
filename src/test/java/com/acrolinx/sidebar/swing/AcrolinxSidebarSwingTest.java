/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.swing;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.acrolinx.sidebar.AcrolinxIntegration;

class AcrolinxSidebarSwingTest
{

    private static void testPasteCommandIsConsumed(int key, int modifier)
    {
        AcrolinxIntegration acrolinxIntegration = Mockito.mock(AcrolinxIntegration.class);
        AcrolinxSidebarSwing acrolinxSidebarSwing = new AcrolinxSidebarSwing(acrolinxIntegration);
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);

        Mockito.when(keyEvent.getKeyCode()).thenReturn(key);
        Mockito.when(keyEvent.getModifiers()).thenReturn(modifier);
        acrolinxSidebarSwing.processKeyEvent(keyEvent);

        Mockito.verify(keyEvent).consume();
    }

    private static void testKeyEventIsNotConsumed(int key, int modifier)
    {
        AcrolinxIntegration acrolinxIntegration = Mockito.mock(AcrolinxIntegration.class);
        AcrolinxSidebarSwing acrolinxSidebarSwing = new AcrolinxSidebarSwing(acrolinxIntegration);
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);

        Mockito.when(keyEvent.getKeyCode()).thenReturn(key);
        Mockito.when(keyEvent.getModifiers()).thenReturn(modifier);
        acrolinxSidebarSwing.processKeyEvent(keyEvent);

        Mockito.verify(keyEvent, Mockito.never()).consume();
    }

    @Test
    void processKeyEvent()
    {
        // Command + V on Mac
        testPasteCommandIsConsumed(KeyEvent.VK_V, KeyEvent.META_MASK);
        // CTRL + V on Mac
        testPasteCommandIsConsumed(KeyEvent.VK_V, KeyEvent.CTRL_MASK);
        // SHIFT + A
        testKeyEventIsNotConsumed(KeyEvent.VK_A, KeyEvent.SHIFT_MASK);
    }
}