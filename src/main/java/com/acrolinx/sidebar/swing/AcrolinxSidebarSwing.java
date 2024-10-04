/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxSidebar;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFX;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.pojo.settings.SidebarMessage;
import com.acrolinx.sidebar.utils.LogMessages;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Swing implementation of Acrolinx Sidebar.
 *
 * @see AcrolinxSidebar
 */
public class AcrolinxSidebarSwing extends JFXPanel implements AcrolinxSidebar {
  private static final long serialVersionUID = 3813416489627785478L;
  protected static final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarSwing.class);

  protected transient AcrolinxSidebarJFX sidebarJFX;
  protected final transient AcrolinxStorage acrolinxStorage;
  protected final transient AcrolinxIntegration acrolinxIntegration;

  public AcrolinxSidebarSwing(final AcrolinxIntegration acrolinxIntegration) {
    this(acrolinxIntegration, null);
  }

  public AcrolinxSidebarSwing(
      final AcrolinxIntegration acrolinxIntegration, final AcrolinxStorage acrolinxStorage) {
    LogMessages.logJavaVersionAndUiFramework(logger, "Java Swing with Java FX Sidebar component");
    this.acrolinxStorage = acrolinxStorage;
    this.acrolinxIntegration = acrolinxIntegration;
    Platform.setImplicitExit(false);
    JFXUtils.invokeInJFXThread(this::createScene);
  }

  @Override
  protected void processKeyEvent(final KeyEvent keyEvent) {
    // Consume all paste events (CTRL+V) in the sidebar to prevent inserting the content into
    // the editor
    // too.
    if (keyEvent.getKeyCode() == KeyEvent.VK_V && isMetaOrCtrlModifier(keyEvent)) {
      keyEvent.consume();
    }

    super.processKeyEvent(keyEvent);
  }

  private static boolean isMetaOrCtrlModifier(KeyEvent keyEvent) {
    return (keyEvent.getModifiers() & InputEvent.META_MASK) != 0
        || (keyEvent.getModifiers() & InputEvent.CTRL_MASK) != 0;
  }

  protected void createScene() {
    sidebarJFX = new AcrolinxSidebarJFX(acrolinxIntegration, acrolinxStorage);
    final WebView webview = sidebarJFX.getWebView();
    GridPane.setHgrow(webview, Priority.ALWAYS);
    GridPane.setVgrow(webview, Priority.ALWAYS);
    webview.setPrefWidth(300);
    addComponentListener(
        new ComponentListener() {
          @Override
          public void componentResized(final ComponentEvent componentEvent) {
            logger.debug("Component resized");
            logger.debug("{} width", getWidth());
            final float zoomFactor = (float) getWidth() / 300;
            logger.debug("{} Zoom", zoomFactor);
            sidebarJFX.setZoom(zoomFactor);
          }

          @Override
          public void componentMoved(final ComponentEvent componentEvent) {
            // we only need resize event to be handled
          }

          @Override
          public void componentShown(final ComponentEvent componentEvent) {
            // we only need resize event to be handled
          }

          @Override
          public void componentHidden(final ComponentEvent componentEvent) {
            // we only need resize event to be handled
          }
        });

    final Scene scene = new Scene(webview);
    setScene(scene);
    setVisible(true);
  }

  @Override
  public void configure(final SidebarConfiguration sidebarConfiguration) {
    if (sidebarJFX != null) {
      sidebarJFX.configure(sidebarConfiguration);
    }
  }

  @Override
  public void checkGlobal() {
    if (sidebarJFX != null) {
      sidebarJFX.checkGlobal();
    }
  }

  @Override
  public void onGlobalCheckRejected() {
    if (sidebarJFX != null) {
      sidebarJFX.onGlobalCheckRejected();
    }
  }

  @Override
  public void invalidateRanges(final List<CheckedDocumentPart> invalidCheckedDocumentRanges) {
    if (sidebarJFX != null) {
      sidebarJFX.invalidateRanges(invalidCheckedDocumentRanges);
    }
  }

  @Override
  public void invalidateRangesForMatches(final List<? extends AbstractMatch> abstractMatches) {
    if (sidebarJFX != null) {
      sidebarJFX.invalidateRangesForMatches(abstractMatches);
    }
  }

  @Override
  public void loadSidebarFromServerLocation(final String serverAddress) {
    if (sidebarJFX != null) {
      sidebarJFX.loadSidebarFromServerLocation(serverAddress);
    }
  }

  @Override
  public void reload() {
    if (sidebarJFX != null) {
      sidebarJFX.reload();
    }
  }

  @Override
  public String getLastCheckedDocumentReference() {
    if (sidebarJFX != null) {
      return sidebarJFX.getLastCheckedDocumentReference();
    }

    return "";
  }

  @Override
  public String getLastCheckedDocument() {
    if (sidebarJFX != null) {
      return sidebarJFX.getLastCheckedDocument();
    }

    return "";
  }

  public ExternalContent getLastCheckedExternalContent() {
    if (sidebarJFX != null) {
      return sidebarJFX.getLastCheckedExternalContent();
    }

    return null;
  }

  @Override
  public void showMessage(SidebarMessage sidebarMessage) {
    if (sidebarJFX != null) {
      sidebarJFX.showMessage(sidebarMessage);
    }
  }

  @Override
  public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions) {
    if (sidebarJFX != null) {
      sidebarJFX.initBatchCheck(batchCheckRequestOptions);
    }
  }

  @Override
  public void checkDocumentInBatch(
      String documentIdentifier, String documentContent, CheckOptions checkOptions) {
    if (sidebarJFX != null) {
      sidebarJFX.checkDocumentInBatch(documentIdentifier, documentContent, checkOptions);
    }
  }

  public AcrolinxSidebarJFX getSidebarJFX() {
    return sidebarJFX;
  }
}
