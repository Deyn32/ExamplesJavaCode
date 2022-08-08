package ru.nvacenter.bis.appDocFz.listeners;

import org.hibernate.boot.Metadata;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * <p>
 * {@link Integrator} - класс который автоматически вызывается
 * hibernate во время конфигурации и позволяет "подстроить под себя": в частности можно подписаться на события. 
 * </p>
 * <p>
 * Согласно документации, этот класс вызывается, если создать файл: 
 * <br/><code><b>META-INF\services\org.hibernate.integrator.spi.Integrator</b></code><br/>
 * и в нем указать полное имя этого класса. 
 * Можно указывать несколько классов в этом файле.<br/>
 * Класс инициализируется не Spring'ом и воэтому инъектировать в него зависимости нельзя, но можно воспользоваться нашим
 * {@link SpringHelper#getBean(Class)}
 * </p>
 * @author aborisov
 */
public class HibernateIntegrator implements Integrator {

    @Override
    public void integrate(Metadata metadata,
                          SessionFactoryImplementor sessionFactory,
                          SessionFactoryServiceRegistry serviceRegistry) {

        final EventListenerRegistry eventListenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );

        // Здесь мы добавляем событие вконец цепочки событий, а можно ещё перетереть событие или добавить вначало цепочки
        AppHibernateEventListener listener = new AppHibernateEventListener();
        eventListenerRegistry.appendListeners( EventType.POST_INSERT, listener);
        eventListenerRegistry.appendListeners( EventType.POST_UPDATE, listener);
        eventListenerRegistry.appendListeners( EventType.POST_DELETE, listener);
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory,
                             SessionFactoryServiceRegistry serviceRegistry) {
        // Empty
    }
}