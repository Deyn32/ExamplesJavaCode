package ru.nvacenter.bis.appDocFz.client.activity;

import ru.nvacenter.bis.appDocFz.client.view.StartView;
import ru.nvacenter.bis.core.client.place.LoginPlace;
import ru.nvacenter.bis.core.client.place.StartPlace;
import ru.nvacenter.bis.core.client.services.SessionService;
import ru.nvacenter.bis.core.shared.AppAuthority;
import ru.nvacenter.bis.core.shared.CoreRequestFactory;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;
import ru.nvacenter.bis.service.client.presenter.AccountLockedPresenter;
import ru.nvacenter.bis.service.client.presenter.AuthorizationPresenter;
import ru.nvacenter.bis.service.client.view.AccountLockedPopup;
import ru.nvacenter.bis.service.client.view.AccountLockedView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class StartActivity extends AbstractActivity implements
	StartView.Presenter {
	
	// Used to obtain views, eventBus, placeController
	// Alternatively, could be injected via GIN

	@SuppressWarnings("unused")
	private StartPlace place;
    @Inject
	private StartView view;
    @Inject
    private PlaceController placeController;
    @Inject
    private PlaceHistoryMapper placeHistoryMapper;
    @Inject
    /** Объект для осущетвления запросов на сервером */
    private CoreRequestFactory requestFactory;
    @Inject
    private SessionService sessionService;

    
    @Inject
    public StartActivity(@Assisted StartPlace place) {
		this.place = place;
	}
	
	/**
	* Invoked by the ActivityManager to start a new Activity
	*/
	@Override
	public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        final UserProxy user = sessionService.getCurrentUser();

        // если пользователь вошел на главную, после авторизации, но не активировал свою учетку

        if(user.getIsAccountLocked() == true) {
            AccountLockedView accountLockedView = new AccountLockedPopup();
            AccountLockedPresenter accountLockedPresenter = new AccountLockedPresenter(accountLockedView, user.getUsername(), true, requestFactory);
            accountLockedPresenter.go();
            // пока не пройдена активация лучше вернутся на окно логина
            placeController.goTo(new LoginPlace());
        }

		view.setPresenter(this);
		
		containerWidget.setWidget(view.asWidget());

		view.setInstituteId(user.getInstitute().getId());


	}
	
	/**
	* Navigate to a new Place in the browser
	*/
	public void goTo(Place place) {
		placeController.goTo(place);
	}

	@Override
	public String getHistoryToken(Place place) {
		return placeHistoryMapper.getToken(place);
	}
}

