package ru.nvacenter.bis.appDocFz.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import ru.nvacenter.bis.appDocFz.shared.AppRequestFactory;


@GinModules(value={InjectorModule.class}, properties={"gin.ginjector.modules"})
public interface Injector extends Ginjector,
        ru.nvacenter.bis.service.client.Injector,
        ru.nvacenter.bis.guidecom.client.Injector,
        ru.nvacenter.bis.core.client.Injector
{

    Injector INSTANCE = GWT.create(Injector.class);

    /** @return общая шина событий приложения */
    EventBus getEventBus();
    /**
     * @return экземпляр GWT RequestFactory используемый в приложении, поскольку создание экземпляра RequestFactory
     * это ресурсоемкая операция, не нужно создавать других экземпляров
     */
    AppRequestFactory getRequestFactory();

    PlaceController getPlaceController();
    /**
     * @return позволяет получать из {@link com.google.gwt.place.shared.Place Place} строку historyToken с помощью метода
     * {@link PlaceHistoryMapper#getToken(com.google.gwt.place.shared.Place) getToken(Place place)} и наоборот.
     * Вынес сюда потому что он мне нужен больше чем в одном месте. В примерах Google экземпляр этого класса создавался в EntryPoint
     * и более небыл напрямую доступен (использовался только внутри google методов).
     */
    PlaceHistoryMapper getPlaceHistoryMapper();
    /**
     * Контейнер в котором будут жить и сменяться View'хи.
     * Нужно быть аккуратными с выбором такого контейнера, потому что GXT 2.2.5
     * вываливалось в No such node found при удалении из главной SimplePanel всего нашего MVP.
     */
    AcceptsOneWidget getActivityManagerDisplay();

    ActivityManager getActivityManager();

    ActivityMapper getActivityMapper();

    /** EntryPoint создаётся не GIN а GWT.Create, который не знает что нужно инъектировать */
    void injectEntryPoint(AppEntryPoint entryPoint);
}