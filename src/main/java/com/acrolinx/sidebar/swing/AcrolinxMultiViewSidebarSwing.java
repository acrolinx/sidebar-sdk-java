/**
 * Copyright (c) 2021-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.swing;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxMultiViewSidebarInterface;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFX;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.localization.LocalizedStrings;
import com.acrolinx.sidebar.localization.Localizer;
import com.acrolinx.sidebar.utils.AcrolinxException;
import com.acrolinx.sidebar.utils.IconUtils;

public class AcrolinxMultiViewSidebarSwing extends AcrolinxSidebarSwing implements AcrolinxMultiViewSidebarInterface
{
    private static final long serialVersionUID = 1L;

    private final Map<String, AcrolinxSidebarJFX> sidebars = new ConcurrentHashMap<>();

    /**
     *
     * @param integration Acrolinx Integration without Acrolinx Storage
     */
    public AcrolinxMultiViewSidebarSwing(AcrolinxIntegration integration)
    {
        super(integration);
        showEmptyMessage();
    }

    /**
     *
     * @param integration Acrolinx Integration with Acrolinx storage as external synchronous local
     *        storage
     */
    public AcrolinxMultiViewSidebarSwing(AcrolinxIntegration integration, AcrolinxStorage storage)
    {
        super(integration, storage);
        showEmptyMessage();
    }

    @Override
    protected void createScene()
    {
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(final ComponentEvent e)
            {
                for (AcrolinxSidebarJFX sidebarJFX : sidebars.values()) {
                    logger.debug("Component resized");
                    logger.debug("{} width", getWidth());
                    final float i = (float) getWidth() / 300;
                    logger.debug("{} Zoom", i);
                    sidebarJFX.setZoom(i);
                }
            }

            @Override
            public void componentMoved(final ComponentEvent e)
            {
                // we only need resize event to be handled
            }

            @Override
            public void componentShown(final ComponentEvent e)
            {
                // we only need resize event to be handled
            }

            @Override
            public void componentHidden(final ComponentEvent e)
            {
                // we only need resize event to be handled
            }
        });
    }

    /**
     *
     * @param client New integration dedicated per sidebar instance
     * @param documentId Unique document Id for the sidebar instance eg: file path
     * @throws AcrolinxException Throws exception is sidebar already exists for the document
     */
    @Override
    public void addSidebar(AcrolinxIntegration client, String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarJFX existingSidebar = sidebars.get(documentId);
        if (existingSidebar != null) {
            throw new AcrolinxException("Sidebar already exists for document id: " + documentId);
        }
        JFXUtils.invokeInJFXThread(() -> {
            sidebarJFX = new AcrolinxSidebarJFX(client, storage);
            final WebView webview = sidebarJFX.getWebView();
            GridPane.setHgrow(webview, Priority.ALWAYS);
            GridPane.setVgrow(webview, Priority.ALWAYS);
            webview.setPrefWidth(300);
            final float i = (float) getWidth() / 300;
            sidebarJFX.setZoom(i);
            Scene scene;
            if (getScene() != null) {
                scene = getScene();
                scene.setRoot(webview);
            } else {
                scene = new Scene(webview);
            }
            if (!sidebars.containsKey(documentId)) {
                sidebars.put(documentId, sidebarJFX);
            }
            setScene(scene);
            setVisible(true);
        });
    }

    /**
     *
     * @param documentId Document Id of the sidebar instance to switch active sidebar to.
     * @throws AcrolinxException Throws if sidebar not found for provided document id.
     */
    @Override
    public void switchSidebar(String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarJFX acrolinxSidebarJFX = sidebars.get(documentId);
        if (acrolinxSidebarJFX == null) {
            throw new AcrolinxException("Existing sidebar not found for document Id. " + documentId);
        }
        JFXUtils.invokeInJFXThread(() -> {
            if (sidebars.containsKey(documentId)) {
                Scene scene = getScene();
                scene.setRoot(acrolinxSidebarJFX.getWebView());
                setScene(scene);
                setVisible(true);
                sidebarJFX = acrolinxSidebarJFX;
            }
        });
    }

    /**
     *
     * @param documentId Document Id for the sidebar instance to be removed
     * @throws AcrolinxException Throws if sidebar not found for the provided document id.
     */
    @Override
    public void removeSidebar(String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarJFX removedJFXSidebar = sidebars.remove(documentId);

        if (removedJFXSidebar == null) {
            throw new AcrolinxException("Sidebar doesn't exist for the given document Id");
        }
        if (sidebars.isEmpty()) {
            JFXUtils.invokeInJFXThread(() -> sidebarJFX = null);
            showEmptyMessage();
        }
    }

    public void showEmptyMessage()
    {
        JFXUtils.invokeInJFXThread(() -> {
            Scene scene = getScene();
            if (scene == null) {
                setScene(new Scene(new Group()));
                scene = getScene();
            }

            Image acrolinxCheckIcon = new Image(IconUtils.getAcrolinxIcon_48_48_AsStream());
            ImageView imageView = new ImageView(acrolinxCheckIcon);

            Label label = new Label(
                    Localizer.getInstance().getText(LocalizedStrings.NO_CHECK_CONTENT_AVAILABLE_MESSAGE));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            Font defaultFont = Font.getDefault();
            label.setFont(new Font(defaultFont.getSize() * 2));

            VBox messageContainer = new VBox();
            messageContainer.setAlignment(Pos.CENTER);
            messageContainer.getChildren().addAll(imageView, label);
            messageContainer.setPadding(new Insets(0, defaultFont.getSize(), 0, defaultFont.getSize()));

            scene.setRoot(messageContainer);
            setVisible(true);
        });
    }
}
