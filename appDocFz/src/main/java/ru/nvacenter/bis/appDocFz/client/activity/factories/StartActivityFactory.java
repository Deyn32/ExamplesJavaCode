package ru.nvacenter.bis.appDocFz.client.activity.factories;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import ru.nvacenter.bis.appDocFz.client.AssistedInjectFactory;
import ru.nvacenter.bis.appDocFz.client.view.StartView;
import ru.nvacenter.bis.core.client.place.StartPlace;
import ru.nvacenter.platform.gwt.mvp.activity.ActivityFactory;

/**
 * Фабрика, возвращающая {@link Acticity} со стартовой информацией.
 * @author Orlov P.
 */
public class StartActivityFactory extends ActivityFactory<StartPlace, StartView> {

    @Inject
    AssistedInjectFactory factory;

    @Inject
    public StartActivityFactory(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected Activity create(Place place) {
        if(place instanceof StartPlace) {
            return factory.createStartActivity((StartPlace) place);
        }
        return null;
    }
}