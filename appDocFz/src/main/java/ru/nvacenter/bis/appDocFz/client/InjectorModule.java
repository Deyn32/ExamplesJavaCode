package ru.nvacenter.bis.appDocFz.client;


import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.FilteredActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import ru.nvacenter.bis.appDocFz.client.services.LettersSyncService;
import ru.nvacenter.bis.appDocFz.client.view.MainView;
import ru.nvacenter.bis.appDocFz.client.view.MainViewImpl;
import ru.nvacenter.bis.appDocFz.client.view.StartView;
import ru.nvacenter.bis.appDocFz.client.view.StartViewImpl;
import ru.nvacenter.bis.appDocFz.shared.AppRequestFactory;

import ru.nvacenter.bis.core.client.place.StartPlace;
import ru.nvacenter.bis.core.client.services.SessionService;
import ru.nvacenter.bis.core.client.utils.ClientHelper;
import ru.nvacenter.bis.core.shared.CoreRequestFactory;

import ru.nvacenter.bis.guidecom.shared.GuideCommonRequestFactory;

import ru.nvacenter.bis.service.shared.ServiceRequestFactory;
import ru.nvacenter.platform.gwt.mvp.placecontroller.AppPlaceController;
import ru.nvacenter.platform.gwt.requestfactory.gwt.client.PlatformRequestTransport;

import javax.inject.Named;
import java.util.logging.Logger;

public class InjectorModule extends AbstractGinModule {

    private static final Logger logger = Logger.getLogger(InjectorModule.class.getName());

    @Override
    protected void configure() {
        bind(StartView.class).to(StartViewImpl.class).in(Singleton.class);
        bind(MainView.class).to(MainViewImpl.class).in(Singleton.class);

        bind(Place.class).annotatedWith(Names.named("Default")).to(StartPlace.class);
        install(new GinFactoryModuleBuilder().build(AssistedInjectFactory.class));
        bind(ClientHelper.class).in(Singleton.class);
        bind(LettersSyncService.class).in(Singleton.class);

        requestStaticInjection(InjectorHelper.class);



        bind(CoreRequestFactory.class).to(AppRequestFactory.class);
        bind(ServiceRequestFactory.class).to(AppRequestFactory.class);

        bind(GuideCommonRequestFactory.class).to(AppRequestFactory.class);

    }

    @Provides
    @Singleton
    public PlaceHistoryMapper getPlaceHistoryMapper() {
        PlaceHistoryMapper placeHistoryMapper = GWT.create(AppPlaceHistoryMapper.class);
        ((AbstractPlaceHistoryMapper)placeHistoryMapper).setFactory(Injector.INSTANCE);
        return placeHistoryMapper;
    }

    @Provides
    @Singleton
    public PlaceController getPlaceController(EventBus eventBus) {
        return new AppPlaceController(eventBus);
    }

    @Provides
    @Singleton
    public AppRequestFactory getRequestFactory(EventBus eventBus) {
        AppRequestFactory requestFactory = GWT.create(AppRequestFactory.class);
        // иницализируем RequestFactory для обмена данным с сервером
        String requestUrl = GWT.getHostPageBaseURL() + "gwt/rf";
        logger.info("инициализируем RequestFactory, url-mapping для RequestFactoryServlet на сервере: \"" + requestUrl + "\"");
        PlatformRequestTransport requestTransport = new PlatformRequestTransport(eventBus);
        requestTransport.setRequestUrl(requestUrl);
        requestFactory.initialize(eventBus, requestTransport);

        return requestFactory;
    }

    @Provides
    @Singleton
    public AcceptsOneWidget getActivityManagerDisplay() {
        // Контейнер в котором будут жить и сменяться View'хи.
        AcceptsOneWidget activityManagerDisplay = new SimpleLayoutPanel();
        return activityManagerDisplay;
    }

    @Provides
    @Singleton
    public ActivityMapper getActivityMapper(SessionService sessionService, PlaceHistoryMapper placeHistoryMapper,
                                            Provider<AppActivityMapper> appActivityMapperProvider) {
        // возвращает экземпляр AppActivityMapper с проиницилизированными инъекциями.
        AppActivityMapper appActivityMapper = appActivityMapperProvider.get();
        return new FilteredActivityMapper(new PlaceAccessFilter(sessionService, placeHistoryMapper), appActivityMapper);
    }

    /**
     * Считывает информацию из спрятанного div на странице index.jsp,
     * который содержит информацию о версии ПО на сервере.
     */
    @Provides
    @Named("appVersion")
    @Singleton
    public String getAppVersion() {
        DivElement versionDiv = (DivElement) Document.get().getElementById(AppEntryPoint.VERSION_DIV_ID);
        return versionDiv.getInnerHTML();
    }
}