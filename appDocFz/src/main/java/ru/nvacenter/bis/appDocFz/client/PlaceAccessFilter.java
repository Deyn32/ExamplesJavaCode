package ru.nvacenter.bis.appDocFz.client;

import ru.nvacenter.bis.core.client.place.AccessDeniedPlace;
import ru.nvacenter.bis.core.client.place.LoginPlace;
import ru.nvacenter.bis.core.client.place.PasswordRecoverPlace;
import ru.nvacenter.bis.core.client.place.RegisterUserPlace;
import ru.nvacenter.bis.core.client.place.StartPlace;
import ru.nvacenter.bis.core.client.place.UserProfilePlace;
import ru.nvacenter.bis.core.client.services.SessionService;
import ru.nvacenter.bis.core.shared.AppAuthority;
import ru.nvacenter.bis.core.shared.AppSecurity;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;
import ru.nvacenter.bis.guidecom.client.place.GuideRolesPlace;
import ru.nvacenter.bis.service.client.place.JournalNoticePlace;
import ru.nvacenter.bis.service.client.place.UsersPlace;
import ru.nvacenter.bis.service.client.place.UsersRolesPlace;
import ru.nvacenter.platform.gwt.widget.notification.Notification.NotificationType;

import com.google.gwt.activity.shared.FilteredActivityMapper.Filter;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;

/**
 * Фильтр Place, проверяет наличие прав доступа к указанному Place у пользователя.
 * В случае если пользователь не авторизован - его отправляют на LoginPlace
 * Если не достаточно полномочий пользователь отправляется на AccessDeniedPlace,
 *  где уведомляется о том что не обладает указанными полномочиями.
 * @author npotapova
 */
public class PlaceAccessFilter implements Filter {
	
	private SessionService sessionService;
	/** сериализованный StartPlace, нужен для проверок ниже*/
	private String startPlaceToken;

    private PlaceHistoryMapper placeHistoryMapper;

	public PlaceAccessFilter(SessionService sessionService, PlaceHistoryMapper placeHistoryMapper) {
		this.sessionService = sessionService;
		this.placeHistoryMapper = placeHistoryMapper;
		this.startPlaceToken = placeHistoryMapper.getToken(new StartPlace());
	}
	
	@Override
	public Place filter(Place place) {
		
		// если текущий Place - StartPlace, и пользователь нажал History.back(), то не дадим переходить на LoginPlace
		if(History.getToken().startsWith(startPlaceToken) && 
			(place instanceof LoginPlace ||
			place instanceof RegisterUserPlace ||
			place instanceof PasswordRecoverPlace)) {
			return new StartPlace();
		}
		
		// Проверка на доступ к том или иному place, не должна касаться LoginPlace
		// 	доступ к остальным Place ограничен и проверяется при каждом переходе по ссылке
		if(place instanceof LoginPlace || 
			place instanceof RegisterUserPlace ||
			place instanceof PasswordRecoverPlace)
			return place;
		
		UserProxy user = sessionService.getCurrentUser();
		if(user == null) {
			// Если текущий пользователь не был ещё считан, или был потерян
			// Сохраняем place, в который просился пользователь
			// чтобы перевести пользователя на него, когда завершим авторизацию.
			return new LoginPlace(NotificationType.WARNING, "Пожалуйста авторизуйтесь",
					History.getToken());
		}
		
		// если пользователь решил пойти в режим редактирования профиля
		if(place instanceof UserProfilePlace) {
			UserProfilePlace userProfilePlace = (UserProfilePlace)place;
			// если пользователь идет в свой профиль, то пускаем его, 
			if(user.getId().equalsIgnoreCase(userProfilePlace.getUserId()))
				return userProfilePlace;
			else {
				// а если пользователь собрался в чужой профиль, то это доступно только админу
				if(AppSecurity.hasRole(user, AppAuthority.ROLE_ADMIN)) 
					return userProfilePlace;
				else
					new AccessDeniedPlace(placeHistoryMapper.getToken(place));
			}
		}
			
		
		// Если у пользователя вообще нет ролей, то ему запрещено куда либо заходить, кроме главной страницы
		if(!AppSecurity.hasGrantedAuthorities(user)
			&& !(place instanceof StartPlace)) {
			// Если у пользователя нет каких то прав, 
			// то никуда он перейдет в AccessDeniedPlace в котором:
			// 1. популярно объяснят почему не хватило полномочий (какие полномочия есть, какие необходимы). 
			// 2. К кому обратиться за получением полномочий.
			// 3. кнопка вернуться назад.
			// Сохраняем place в который просился пользователь
			return new AccessDeniedPlace(placeHistoryMapper.getToken(place));
		}
		
		// В режимы редактирования:
		//	1) Пользователей, 
		//	2) Справочника Ролей, 
		//  3) Связок пользователей и ролей 
		//	5) Журнал уведомлений пользователей
		//	6) Просмотр логов
		//	7) Обслуживание сервера
		// может зайти только админ.
		if((
				place instanceof UsersPlace ||
				place instanceof GuideRolesPlace ||
				place instanceof UsersRolesPlace ||
				place instanceof JournalNoticePlace
			)
			&&	!AppSecurity.hasRole(user, AppAuthority.ROLE_ADMIN)) {
			// Если у пользователя нет каких то прав, 
			// то никуда он перейдет в AccessDeniedPlace в котором:
			// 1. популярно объяснят почему не хватило полномочий (какие полномочия есть, какие необходимы). 
			// 2. К кому обратиться за получением полномочий.
			// 3. кнопка вернуться назад.
			// Сохраняем place в который просился пользователь
			return new AccessDeniedPlace(new String[] { AppAuthority.ROLE_ADMIN }, placeHistoryMapper.getToken(place));
		}

		// Внутренний фин. контроль открыт для админов и учредителей

		
		// по умолчанию пускаем пользователя куда попросился
		return place;
	}
}