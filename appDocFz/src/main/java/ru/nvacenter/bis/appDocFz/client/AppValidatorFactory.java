/**
 * 
 */
package ru.nvacenter.bis.appDocFz.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;
import ru.nvacenter.bis.core.shared.proxies.entity.InstituteProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.NoticeProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.RoleProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy.Password;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy.Profile;

import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * Стандартная фабрика для создания экземпляра валидатора <u><b>на клиенте</b></u>, то есть до отправки на сервер!
 * Реализованы не все аннотации из hibernate-validator, но многие.
 * Если нужно валидировать ваш класс, добавляем его через запятую после @GwtValidation(value = {UserProxy.class, <ваеш класс>})
 * @author aborisov
 */
public class AppValidatorFactory extends AbstractGwtValidatorFactory {

	/**
	 * Только классы и группы указанные в аннотации @GwtValidation могут быть валидированны
	 */
	@GwtValidation(value = {UserProxy.class, RoleProxy.class, InstituteProxy.class, NoticeProxy.class},
			groups = {Default.class, Profile.class, Password.class})
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}

}
