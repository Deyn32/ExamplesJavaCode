package ru.nvacenter.bis.appDocFz.client;

import com.google.common.collect.Lists;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import ru.nvacenter.bis.appDocFz.client.services.LettersSyncService;
import ru.nvacenter.bis.appDocFz.shared.AppRequestFactory;
//import ru.nvacenter.bis.audit.client.place.AuditBasePlace;
//import ru.nvacenter.bis.audit.client.place.AuditMainPlace;
import ru.nvacenter.bis.core.client.place.FramePlace;
import ru.nvacenter.bis.core.client.place.LoginPlace;
import ru.nvacenter.bis.core.client.place.PasswordRecoverPlace;
import ru.nvacenter.bis.core.client.place.RegisterUserPlace;
import ru.nvacenter.bis.core.client.services.SessionService;
import ru.nvacenter.bis.core.client.services.UserSyncService;
import ru.nvacenter.bis.core.client.widget.errordialog.ErrorDialog;
import ru.nvacenter.bis.core.shared.proxies.entity.InstituteProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;
import ru.nvacenter.platform.gwt.utils.client.AppUncaughtExceptionHandler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * EntryPoint для Activites And Places версии
 * @author aborisov
 */
public class AppEntryPoint implements EntryPoint {

	private static final Logger logger = Logger.getLogger(AppEntryPoint.class.getName());

	public static final String VERSION_DIV_ID = "versionDiv";
	public static final String LOADING_DIV_ID = "loadingDiv";
	/**
	 * div на странице index.jsp который содержит <img> "индикатор загрузки" loading.gif
	 */
	DivElement loadingDiv = (DivElement)Document.get().getElementById(LOADING_DIV_ID);

    @Inject
    private com.google.gwt.event.shared.EventBus eventBus;

    @Inject
    private ActivityManager activityManager;
    @Inject
    private AcceptsOneWidget activityManagerDisplay;
    @Inject
    private PlaceHistoryMapper placeHistoryMapper;
    @Inject
    private PlaceController placeController;
    @Inject
    private SessionService sessionService;
    /** Страницей по умолчанию, будет Главная Страница */
    @Inject
    @Named("Default")
    private Place defaultPlace;
    /** Объект для осущетвления вызовов на сервере */
    @Inject
    private AppRequestFactory requestFactory;
    @Inject
    private UserSyncService userSyncService;
    @Inject
    private LettersSyncService lettersSyncService;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new AppUncaughtExceptionHandler());
		Logger.getLogger("").addHandler(new ErrorDialog().getHandler());
		
		logger.info("Инициализация модуля \"" + GWT.getModuleName() + "\", вер. GWT " + GWT.getVersion());
        initInjection();

		// назначает виджет-контейнер, в котором будем менять представления в зависимости от Place
		activityManager.setDisplay(activityManagerDisplay);
		
		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(placeHistoryMapper);
        final HandlerRegistration historyHandlerRegistration = historyHandler.register(placeController, eventBus, defaultPlace);
		
		logger.info("	вызванный history token: \"#" + History.getToken() + "\"");
		
		// Если пользователь хочет перейти в режим авторизации или регистрации нового пользователя
		// мы пускаем его без проверок у SpringSecurity
		if(History.getToken().startsWith(placeHistoryMapper.getToken(new LoginPlace()))
			|| History.getToken().startsWith(placeHistoryMapper.getToken(new RegisterUserPlace()))
			|| History.getToken().startsWith(placeHistoryMapper.getToken(new PasswordRecoverPlace()))) {
			historyHandler.handleCurrentHistory();
		} else {
			// Попробуем считать информацию о залогинившемся пользователе
			// 	если это не удастся (благодаря SpringSecurity), 
			// 	то в AuthenticationRequestTransport будет выброшено событие AuthenticationFailureEvent
			requestFactory.userRequest()
				.getCurrentUser()
				.with(UserSyncService.DEFAULT_USER_PROPS)
				.fire(new Receiver<UserProxy>() {
                    @Override
                    public void onSuccess(UserProxy response) {
                        // устанавливаем текущего залогиневшегося пользователя
                        sessionService.setCurrentUser(response);
						// преднастройки приложения
						sessionService.setProposalAcceptWithoutCurator(true);

                        InstituteProxy institute = response.getInstitute();
                        // если пользователь принадлежит казённому учреждению, то его главная страница будет Росписью
                        // закоментил Головин Р.
                        //if (institute != null && institute.getTypeInstitute() == 2) {
                        //    historyHandlerRegistration.removeHandler(); // стираем прошлую регистрацию главной страницы
                        //    historyHandler.register(placeController, eventBus, new LboModelsPlace());
                        //}

                        historyHandlerRegistration.removeHandler(); // стираем прошлую регистрацию главной страницы
                        historyHandler.register(placeController, eventBus, new FramePlace("audit-violation-npa.html#/activityHeaders", "НСИ/Администрирование/Анализ формулировок нарушений"
						));



                        // Если авторизация выполнена - то SpringSecurity даст доступ к RequestFactory,
                        // 	можно смело подключать логгирования с клиента на сервер
                        addRequestFactoryLogHandling();

                        // начнем опрашивать сервер на предмет изменения данных текущего пользователя, например ролей или его учреждения
                        if (GWT.isProdMode()) // включаем синхронизацию только в промышленном режиме
                            userSyncService.start();

                        // начнем опрашивать сервер на предмет наличия непрочитанных сообщений
                        lettersSyncService.start();

                        // Goes to place represented on URL or default place
                        historyHandler.handleCurrentHistory();
                    }
                });
		}
		
	    //Прячем loading.gif после того как выполнение пришло сюда -> значит мы уже готовы отобразить что-то пользовалю
  		loadingDiv.getStyle().setDisplay(Display.NONE);
	}

    /** DI в поля EntryPoint, потому что AppEntryPoint создаётся GWT и он не знает о том что нужно сделать инъекцию */
    private void initInjection() {
		Injector injector = Injector.INSTANCE;
        injector.injectEntryPoint(this);
    }

	/**
	 * Осуществляет конфигурацию логгирования ошибок клиента на сервер через requestFactory {@link LoggingRequest}
	 */
	private void addRequestFactoryLogHandling() {
		RequestFactoryLogHandler.LoggingRequestProvider requestProvider = new RequestFactoryLogHandler.LoggingRequestProvider() {
			public LoggingRequest getLoggingRequest() {
				return requestFactory.loggingRequest();
			}
		};
		
		List<String> ignoredLoggerNames = Lists.newArrayList(
				/* 
				 * C одной стороны:	исключаем этот класс, потому что в случае проблем с RequestFactory, 
				 *	получим бесконечный цикл в попытке отправить лог на сервер
				 * 	A с другой стороны: все логи на сервер идут от этого логгера, если его игнорировать.
				 *	то ни один лог на сервер не пойдёт
				 */
				//Logger.getLogger(AuthenticationRequestTransport.class.getName()).getName()
			);
		
		Logger.getLogger("").addHandler(
				// независимо от настроек в .gwt.xml на сервер передаются только критически важные логи: WARNING, SEVERE
				new RequestFactoryLogHandler(requestProvider, Level.WARNING, ignoredLoggerNames)
			);
	}
}