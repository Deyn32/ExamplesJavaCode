package ru.nvacenter.bis.appDocFz.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import ru.nvacenter.bis.appDocFz.client.view.MainView;
import ru.nvacenter.bis.core.client.PageUtils;
import ru.nvacenter.bis.core.client.place.LoginPlace;
import ru.nvacenter.bis.core.client.place.PasswordRecoverPlace;
import ru.nvacenter.bis.core.client.place.RegisterUserPlace;
import ru.nvacenter.bis.core.client.place.TitledPlace;
import ru.nvacenter.bis.core.client.widget.breadcrumb.BreadCrumbGenerator;
import ru.nvacenter.platform.gwt.mvp.event.ActivityRequestEvent;

//import ru.nvacenter.platform.gwt.mvp.event.ActivityRequestEvent;

public class AppActivityMapper implements ActivityMapper {

    @Inject
    private AssistedInjectFactory factory;
    /** Контейнер в котором будут жить и сменяться View'хи */
    @Inject
    private AcceptsOneWidget activityManagerDisplay;
    @Inject
    private MainView mainView;

    @Inject
    private PageUtils pageUtils;
    @Inject
    private BreadCrumbGenerator breadCrumbGenerator;
    @Inject
    private EventBus eventBus;
	/**
	 * Признак того что на RootLayoutPanel уже инъектированна навигация MainView.
	 * Завел для того чтобы не перевставлять MainView на каждом переходе по Place внутри приложения.
	 * Можно было сделать через <code>RootLayoutPanel.get1().getWidget(0) instanceof</code>,
	 * но это на мой взгляд было бы более ресурсоемко
	 */
	private Boolean mainViewEnabled;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 */
	public AppActivityMapper() {
		super();
	}

	/**
	 * Map each Place to its corresponding Activity. This would be a great use
	 * for GIN.
	 */
	@Override
	public Activity getActivity(final Place place) {
		
		// Если у этого place есть некое название, то помещаем это название в заголовок страницы
		//	иначе возвращаем название страницы по умолчанию
		if(place instanceof TitledPlace)
            pageUtils.setPageTitle((TitledPlace)place);
		else
            pageUtils.setDefaultPageTitle();
		
		
		// Для страницы авторизации и регестрации не нужно отображать меню-навигации, поэтому 
		// здесь меняем внешний вид интерфейса в зависимости от Place
		if(place instanceof LoginPlace ||
			place instanceof RegisterUserPlace ||
			place instanceof PasswordRecoverPlace) {
			if( mainViewEnabled == null || mainViewEnabled == true) {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add((Widget) activityManagerDisplay);
				mainViewEnabled = false;
			}
		} else if (place != null) {
			if(mainViewEnabled == null || mainViewEnabled == false) {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(mainView);
				mainViewEnabled = true;
				
				mainView.setDisplay(activityManagerDisplay);
			}			
			// устанавливаем хлебные крошки к новому Place
			mainView.setBreadCrumb(breadCrumbGenerator.getBreadCrumb(place));
			//mainView.setDisplayMenuBar();
			// в ClientFactoryImpl в обработчике события YearChangedEvent назначается ActualPlanHeader и ссылки на текущий план
		}

        ActivityRequestEvent event = new ActivityRequestEvent(place);
        eventBus.fireEvent(event);

        Activity activity = event.getActivity();
        return activity;
	}
}