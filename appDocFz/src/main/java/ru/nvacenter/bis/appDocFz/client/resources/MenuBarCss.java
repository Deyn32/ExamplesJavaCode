package ru.nvacenter.bis.appDocFz.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * Created by aburaev on 07.07.2015.
 */

public interface MenuBarCss extends CssResource {
    @ClassName("gwt-MenuBar")
    String gwtMenuBar();
    @ClassName("gwt-MenuBar-vertical")
    String vertical();

    String menuPopupTopCenter();

    @ClassName("gwt-MenuBarPopup")
    String gwtMenuBarPopup();

    @ClassName("gwt-MenuItem-active")
    String gwtMenuItemActive();

    String menuPopupMiddleCenter();

    String menuPopupBottomCenterInner();

    @ClassName("gwt-MenuBar-horizontal")
    String gwtMenuBarHorizontal();

    String menuPopupTopRightInner();

    String menuPopupBottomRight();

    String menuPopupMiddleLeftInner();

    @ClassName("gwt-MenuItem-SubItem")
    String gwtMenuItemSubItem();

    String menuPopupBottomRightInner();

    String menuPopupTopLeft();

    String menuPopupMiddleCenterInner();

    @ClassName("gwt-MenuItem-selected")
    String gwtMenuItemSelected();

    String menuSeparatorInner();

    String menuPopupMiddleRightInner();

    String menuPopupTopRight();

    @ClassName("gwt-MenuBar-vertical-left")
    String gwtMenuBarVerticalLeft();

    String menuPopupTopLeftInner();

    String menuPopupTopCenterInner();

    @ClassName("gwt-MenuItem")
    String gwtMenuItem();

    String menuPopupMiddleRight();

    String menuPopupBottomLeft();

    String menuPopupBottomCenter();

    String menuPopupMiddleLeft();

    @ClassName("gwt-MenuItemSeparator")
    String gwtMenuItemSeparator();

    String menuPopupBottomLeftInner();
}
