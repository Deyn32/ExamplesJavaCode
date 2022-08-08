package ru.nvacenter.bis.appDocFz.client.services;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import ru.nvacenter.bis.core.client.services.SessionService;
import ru.nvacenter.bis.core.shared.proxies.entity.InstituteProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;
import ru.nvacenter.platform.gwt.event.shared.AuthenticationFailureEvent;
import ru.nvacenter.platform.gwt.event.shared.AuthenticationFailureEventHandler;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для поиска количества непрочитанных сообщений
 * @author: mshitov, aborisov
 * @since 11/26/2015
 */
public class LettersSyncService {

    /** планируем каждый сеанс синхронизации через указанное количество секунд */
    public static final int SYNC_INTERVAL = 30 * 1000;
    private final String settingViewInfo = "ViewInfoByLetters";
    private Boolean ViewMessage = true;

    @Inject
    SessionService sessionService;

    /** Таймер для синхронизации поиска непрочитанных сообщений. */
    private Timer timer = new Timer() {
        public void run() {
            doOnTimerRun();
        }
    };

    /**
     * Обработчик ответа от сервера на запрос количества непрочитанных пользователя
     * Посылаем очередной запрос, только после получения ответа (или ошибки) от прошлого запроса.
     **/
    private Receiver<Integer> receiver = new Receiver<Integer>() {

        @Override
        public void onFailure(ServerFailure error) {
            timer.schedule(SYNC_INTERVAL);

            super.onFailure(error);
        }

        @Override
        public void onSuccess(final Integer count) {
            timer.schedule(SYNC_INTERVAL);

            // count - количество непрочитанных сообщений
            if (count != null && count > 0 && ViewMessage) {
                sessionService.setCountLettersNotRead(count);
            }
        }
    };

    /* Constructors */


    @Inject
    public LettersSyncService(EventBus eventBus) {
        eventBus.addHandler(AuthenticationFailureEvent.TYPE, new AuthenticationFailureEventHandler() {
            public void onAuthenticationFailure(AuthenticationFailureEvent event) {
                doOnAuthenticationFailure(event);
            }
        });


    }

    /** Метод инициализирует и запускает синхронизацию непрочитанных сообщений по таймеру {@link #timer} */
    public void start() {
        timer.schedule(SYNC_INTERVAL);
    }

    public void stop() {
        timer.cancel();
    }

    private void doOnTimerRun() {
        //UserProxy currentUser = sessionService.getCurrentUser();
        //InstituteProxy institute = currentUser.getInstitute();



    }

    private void viewInfo()
    {
        //if(!ViewMessage) return;

    }

    private void doOnAuthenticationFailure(AuthenticationFailureEvent event) {
        stop();
    }
}
