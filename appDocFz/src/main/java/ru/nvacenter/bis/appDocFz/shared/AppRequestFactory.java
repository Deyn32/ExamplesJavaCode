package ru.nvacenter.bis.appDocFz.shared;

import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import ru.nvacenter.bis.core.shared.CoreRequestFactory;
import ru.nvacenter.bis.guidecom.shared.GuideCommonRequestFactory;
import ru.nvacenter.bis.service.shared.ServiceRequestFactory;

public interface AppRequestFactory extends RequestFactory, CoreRequestFactory,  ServiceRequestFactory, GuideCommonRequestFactory

{
    /**
	 * Подключет встроенный r-f логгер {@link com.google.web.bindery.requestfactory.server.Logging }
	 */
	LoggingRequest loggingRequest();
}
