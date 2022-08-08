package ru.nvacenter.bis.appDocFz.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import ru.nvacenter.bis.appDocFz.client.resources.Resources;

/**
 * @author aborisov
 */
public class StartViewImpl extends Composite implements StartView {
		
	interface StartViewImplUiBinder extends UiBinder<Widget, StartViewImpl> {}
	private static StartViewImplUiBinder uiBinder = GWT.create(StartViewImplUiBinder.class);

    @Inject
    private PlaceHistoryMapper placeHistoryMapper;

	
	private Presenter presenter;

	@UiField CaptionPanel adminPanel;
	@UiField CaptionPanel founderPanel;
    @UiField CaptionPanel institutePanel;
    @UiField CaptionPanel branchPanel;
    @UiField CaptionPanel lboPanel;

	@UiField Resources resources;
	
	String actualPlanId;
	private String instituteId;
	
	
	public StartViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		resources.startViewCss().ensureInjected();
		

	}
	

	@Override
	public void setPlaceHistoryMapper(PlaceHistoryMapper placeHistoryMapper) {

	}
	
	@Override
	public void setActualPlanId(String id) {
		actualPlanId = id;

	}
	
	@Override
	public void initHyperlinks() {
		assert placeHistoryMapper != null : "PlaceHistoryMapper не был установлен, без placeHistoryMapper сериализация place в HistoryToken не возможна";


	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setFounderPanelVisible(boolean visible) {
		this.founderPanel.setVisible(visible);
	}
	
	@Override
	public void setInstitutePanelVisible(boolean visible) {
		this.institutePanel.setVisible(visible);
	}
	
	@Override
	public void setBranchPanelVisible(boolean visible) {
		this.branchPanel.setVisible(visible);
	}
	
	@Override
	public void setAdminPanelVisible(boolean visible) {
		this.adminPanel.setVisible(visible);
	}

    @Override
    public void setLboListVisible(boolean isShow) {
        this.lboPanel.setVisible(isShow);
    }

    @Override
	public void setInstituteId(String instituteId) {
		this.instituteId = instituteId;

	}
}
