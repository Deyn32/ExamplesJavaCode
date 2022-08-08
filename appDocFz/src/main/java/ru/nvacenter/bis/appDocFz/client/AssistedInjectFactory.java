package ru.nvacenter.bis.appDocFz.client;

import ru.nvacenter.bis.appDocFz.client.activity.StartActivity;
import ru.nvacenter.bis.core.client.place.StartPlace;

/**
 * Created by aburaev on 13.04.2015.
 */
public interface AssistedInjectFactory {

    StartActivity createStartActivity(StartPlace place);

}
