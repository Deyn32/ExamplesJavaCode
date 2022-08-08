package ru.nvacenter.bis.appDocFz.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.IsWidget;

public interface StartView extends IsWidget {

	void setPresenter(Presenter presenter);

	/**
	 * Метод должен был заменить бестактное назначение targetHistoryTokens через строки, 
	 * на назначение через сериализацию экземпляров Place
	 */
	void initHyperlinks();
	/**
	 * Назначить признак видимости панели Учредитель
	 * @param visible - true значит сделать видимой, false - скрыть.
	 */
	void setFounderPanelVisible(boolean visible);
	/**
	 * Назначить признак видимости панели Учреждение
	 * @param visible - true значит сделать видимой, false - скрыть.
	 */
	void setInstitutePanelVisible(boolean visible);
	/**
	 * Назначить признак видимости панели Филиал
	 * @param visible - true значит сделать видимой, false - скрыть.
	 */
	void setBranchPanelVisible(boolean visible);
	/**
	 * Назначить признак видимости панели Администратор
	 * @param visible - true значит сделать видимой, false - скрыть.
	 */
	void setAdminPanelVisible(boolean visible);

    void setLboListVisible(boolean isShow);
	
	public interface Presenter {
		void goTo(Place place);
		/**
		 * Сериализует указанный place в historyToken
		 * @param place предназначенный для сериализации
		 * @return GWT historyToken, проще говоря "javascript hash" без символва #
		 */
		String getHistoryToken(Place place);
	}

	void setPlaceHistoryMapper(PlaceHistoryMapper placeHistoryMapper);

	void setActualPlanId(String id);
	
	/**
	 * Устанавливает идентификатор учреждения пользователя 
	 */
	void setInstituteId(String id);
}
