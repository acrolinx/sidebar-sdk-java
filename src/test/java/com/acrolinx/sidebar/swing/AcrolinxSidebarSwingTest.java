package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.AcrolinxIntegration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.event.KeyEvent;

class AcrolinxSidebarSwingTest {

    @Test
    void processKeyEvent_PasteOnMacIsConsumed() {
        // Given
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);
        AcrolinxIntegration acrolinxIntegration = Mockito.mock(AcrolinxIntegration.class);
        AcrolinxSidebarSwing acrolinxSidebarSwing = new AcrolinxSidebarSwing(acrolinxIntegration);

        // When
        Mockito.when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_V);
        Mockito.when(keyEvent.getModifiers()).thenReturn(KeyEvent.META_MASK);

        // Then
        acrolinxSidebarSwing.processKeyEvent(keyEvent);
        Mockito.verify(keyEvent, Mockito.atLeast(1)).consume();
    }

    @Test
    void processKeyEvent_PasteOnWindowsIsConsumed() {
        // Given
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);
        AcrolinxIntegration acrolinxIntegration = Mockito.mock(AcrolinxIntegration.class);
        AcrolinxSidebarSwing acrolinxSidebarSwing = new AcrolinxSidebarSwing(acrolinxIntegration);

        // When
        Mockito.when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_V);
        Mockito.when(keyEvent.getModifiers()).thenReturn(KeyEvent.CTRL_MASK);

        // Then
        acrolinxSidebarSwing.processKeyEvent(keyEvent);
        Mockito.verify(keyEvent, Mockito.atLeast(1)).consume();
    }

    @Test
    void processKeyEvent_AnyOtherEventIsNotConsumed() {
        // Given
        KeyEvent keyEvent = Mockito.mock(KeyEvent.class);
        AcrolinxIntegration acrolinxIntegration = Mockito.mock(AcrolinxIntegration.class);
        AcrolinxSidebarSwing acrolinxSidebarSwing = new AcrolinxSidebarSwing(acrolinxIntegration);

        // When
        Mockito.when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_V);
        Mockito.when(keyEvent.getModifiers()).thenReturn(KeyEvent.SHIFT_MASK);

        // Then
        acrolinxSidebarSwing.processKeyEvent(keyEvent);
        Mockito.verify(keyEvent, Mockito.times(0)).consume();
    }
}