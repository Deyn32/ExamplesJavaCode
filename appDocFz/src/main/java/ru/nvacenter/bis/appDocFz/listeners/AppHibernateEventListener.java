package ru.nvacenter.bis.appDocFz.listeners;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.nvacenter.bis.core.server.domain.Notice;
import ru.nvacenter.bis.core.server.listeners.HibernateEventListener;
import ru.nvacenter.bis.core.server.services.tracking.CommonTrackingService;
import ru.nvacenter.bis.core.server.services.tracking.NoticeTrackingService;
import ru.nvacenter.bis.service.server.services.dao.JournalOperationService;
import ru.nvacenter.platform.core.spring.SpringHelper;


public class AppHibernateEventListener extends HibernateEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AppHibernateEventListener.class);

    /**
     * Сервис для работы с журналом пользовательских действсий.
     * <p>
     * Инициализируем сервис в вызывающих его методах,
     * потому что этот Listener мы не можем создавать в Spring, его создает Hibernate
     * </p>
     */
    private JournalOperationService serviceOperation;
    private CommonTrackingService commonTrackingService;
    private NoticeTrackingService noticeTrackingService;

    ////// Methods /////////////////////////////////////////////////////////////////////////////////

    /**
     * инициализируем сервис записи здесь, потому что этот Listener
     * мы не можем создавать в Spring, если создает Hibernate
     */
    private void initDI() {
        if (serviceOperation == null)
            serviceOperation = SpringHelper.getBean(JournalOperationService.class);

        if (commonTrackingService == null)
            commonTrackingService = SpringHelper.getBean(CommonTrackingService.class);

        if (noticeTrackingService == null)
            noticeTrackingService = SpringHelper.getBean(NoticeTrackingService.class);


    }

    // Implements Interfaces:

    @Override
    public void onPostInsert(PostInsertEvent event) {
        try {
            initDI();

            super.onPostInsert(event);

            ///////// Ручное журналирование событий в JournalOperation: ///////
            if (event.getEntity() instanceof Notice) {
                // Журналируем события связанные с сущностью уведомления
                noticeTrackingService.doOnInsert(event);
            }
        } catch (Throwable caught) {
            logger.error("", caught);
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        try {
            initDI();

            super.onPostUpdate(event);

            ///////// Ручное журналирование событий в JournalOperation: ///////

            if (event.getEntity() instanceof Notice) {
                // Журналируем события связанные с сущностью уведомления
                noticeTrackingService.doOnUpdate(event);
            }
        } catch (Throwable caught) {
            logger.error("", caught);
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        try {
            initDI();

            super.onPostDelete(event);

            ///////// Ручное журналирование событий в JournalOperation: ///////

        } catch (Throwable caught) {
            logger.error("", caught);
        }
    }
}
