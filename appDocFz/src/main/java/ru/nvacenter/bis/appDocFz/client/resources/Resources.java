package ru.nvacenter.bis.appDocFz.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Интерфейс, с помощью которого можно работать с картинками, стилями и прочими файлами на клиенте.
 * @author Borisov A., Orlov P.
 */
public interface Resources extends ClientBundle {
	
	public static Resources INSTANCE = GWT.create(Resources.class);


	/**
	 * CSS стили для {@liplanhdru.nvacenter.bis.client.planhd.view.login.StartViewImpl StartViewImpl}.
	 */
	public interface StartViewCss extends CssResource {		
		@ClassName("gwt-FlowPanel-horizontal")
		String gwtFlowPanelHorizontal();
		
		@ClassName("captionPanel")
		String captionPanel();

		@ClassName("gwt-Hyperlink")
		String gwtHyperlink();

		@ClassName("gwt-Hyperlink-disabled")
		String gwtHyperlinkDisabled();
	}
	
	@Source("StartView.css")
	StartViewCss startViewCss();


	@Source("MenuBar.css")
	MenuBarCss menuBarCss();



}
