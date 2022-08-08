package ru.nvacenter.bis.appDocFz.client.view;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;

public interface MainView extends IsWidget {
	/**
	 * TODO:
	 * @param acceptsOneWidget
	 */
	void setDisplay(AcceptsOneWidget acceptsOneWidget);
	/**
	 * TODO:
	 * @param breadCrumb
	 */
	void setBreadCrumb(SafeHtml breadCrumb);

	void setEventBus(EventBus eventBus);
	/** экземпляр класса преобразуещего Place в строки url */
	void setPlaceHistoryMapper(PlaceHistoryMapper placeHistoryMapper);
	/**
	 * Метод используя requestFactory осуществляет запрос текущего залогиневшегося пользователя, и в случае успеха
	 * записывает логин и имя пользователя в соответвующий logedInLabel.
	 * В случае неудачи покажет ошибку либо вернет на окно авторизации, если пользователь ещё не логинился
	 */
	void setLogedInUser(UserProxy currentUser);

    void setDefaultYear();

	/**
	 * Инициализирует ссылки меню используя экземпляр placeHistoryMapper передаваемый из clientFactory
	 */
	void initHyperlinks(UserProxy currentUser);
	
	/**
	 * Событие для обновления westPanel'е меню в зависимости от приходящего Place
	 */
//	void setDisplayMenuBar();
	/**  идентификатор текщуего плана для пользователя, нужен для генерации некоторых url */
//	void setActualPlanId(String id);
	
	/**
	 * Событие для скрытия/отображения westPanel'е меню в зависимости от приходящего isShow
	 * @param isShow
	 */
//	void setWestMenuShown(boolean isShow);

}
