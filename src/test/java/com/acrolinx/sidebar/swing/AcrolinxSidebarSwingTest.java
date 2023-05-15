/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.swing;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.acrolinx.sidebar.AcrolinxIntegration;

class AcrolinxSidebarSwingTest
{

    private static void testPasteCommand(int key, int modifier, boolean consume)
    {
        AcrolinxIntegration acrolinxIntegration = Mockito.mock(AcrolinxIntegration.class);
        AcrolinxSidebarSwing acrolinxSidebarSwing = new AcrolinxSidebarSwing(acrolinxIntegration);
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);

        Mockito.when(keyEvent.getKeyCode()).thenReturn(key);
        Mockito.when(keyEvent.getModifiers()).thenReturn(modifier);
        acrolinxSidebarSwing.processKeyEvent(keyEvent);

        if (consume) {
            Mockito.verify(keyEvent, Mockito.times(1)).consume();
        } else {
            Mockito.verify(keyEvent, Mockito.times(0)).consume();
        }
    }

    @Test
    void processKeyEventPasteOnMacIsConsumed()
    {
        // Command + V on Mac
        testPasteCommand(KeyEvent.VK_V, KeyEvent.META_MASK, true);
        // CTRL + V on Mac
        testPasteCommand(KeyEvent.VK_V, KeyEvent.CTRL_MASK, true);
        // SHIFT + V
        testPasteCommand(KeyEvent.VK_A, KeyEvent.SHIFT_MASK, false);
    }
}