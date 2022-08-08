package ru.nvacenter.bis.appDocFz.client;

import ru.nvacenter.bis.appDocFz.client.activity.factories.StartActivityFactory;
import ru.nvacenter.bis.core.client.utils.ClientHelper;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Утилитный класс, необходимый только для дополнительной конфигурации приложения сразу после bootstrap'а GIN
 * @author aborisov
 * @since 30.03.2015.
 */
class InjectorHelper {

    private static final Logger logger = Logger.getLogger(InjectorHelper.class.getName());

    private InjectorHelper() {
        // Empty
    }

    @Inject
    public static void setActivityRequestHandlers(StartActivityFactory startActivityFactory, ClientHelper clientHelper) {


        logger.finest(startActivityFactory.toString());
    }
}
