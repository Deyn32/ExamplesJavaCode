package ru.nvacenter.bis.appDocFz.client.view;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import ru.nvacenter.bis.appDocFz.client.resources.Resources;
import ru.nvacenter.bis.core.client.event.*;
import ru.nvacenter.bis.core.client.place.FramePlace;
import ru.nvacenter.bis.core.client.place.UserProfilePlace;
import ru.nvacenter.bis.core.client.resources.CommonResources;
import ru.nvacenter.bis.core.client.services.SessionService;
import ru.nvacenter.bis.core.client.utils.VersionHelper;
import ru.nvacenter.bis.core.client.widget.MenuBarExt;
import ru.nvacenter.bis.core.client.widget.MenuItemExt;
import ru.nvacenter.bis.core.client.widget.userinfo.UserInfoPopover;
import ru.nvacenter.bis.core.shared.AppAuthority;
import ru.nvacenter.bis.core.shared.AppSecurity;
import ru.nvacenter.bis.core.shared.proxies.entity.InstituteProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.RoleProxy;
import ru.nvacenter.bis.core.shared.proxies.entity.UserProxy;
import ru.nvacenter.bis.guidecom.client.place.GuideInstitutePlace;
import ru.nvacenter.bis.guidecom.client.place.GuidePositionPlace;
import ru.nvacenter.bis.guidecom.client.place.GuideRolesPlace;
import ru.nvacenter.bis.guidecom.client.place.GuideSignerPlace;
import ru.nvacenter.bis.service.client.place.*;
import ru.nvacenter.platform.gwt.utils.shared.AppendlessRenderer;
import ru.nvacenter.platform.gwt.widget.datebox.DateBoxResources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainViewImpl extends ResizeComposite implements MainView {

    @Inject
    private SessionService sessionService;

    interface MainViewImplUiBinder extends UiBinder<Widget, MainViewImpl> {}
    private static MainViewImplUiBinder uiBinder = GWT.create(MainViewImplUiBinder.class);


    private static final Logger logger = Logger.getLogger(MainViewImpl.class.getName());

    @Inject
    private EventBus eventBus;
    @Inject
    private PlaceHistoryMapper placeHistoryMapper;
    /** Объект для осущетвления запросов на сервером */
    @Inject
    private PlaceController placeController;

    @UiField
    CommonResources resourcesCore;
    @UiField
    Resources resourcesApp;

    PopupPanel popupPanel; //Шитов М.

    /**
     * Финаносвый год, с коротого мы начинаем заполнять список финансовых годов,
     * 	предполагается что они не будут вводить данные за прошлые года
     */
    private static final int BEGIN_YEAR = 2013;

    /**
     * div на странице index.jsp который содержит информацию о версии ПО на сервере.
     */
    DivElement versionDiv = (DivElement)Document.get().getElementById(VersionHelper.VERSION_DIV_ID);


    @UiField
    SpanElement verisonSpan;

    @UiField SpanElement userInstituteSpan;

    @UiField DockLayoutPanel dockLayoutPanel;

    @UiField HTML breadCrumb;
    @UiField Hyperlink logedInProfileHyperlink;
    @UiField Label userFullName;

    @UiField ValueListBox<Integer> yearListBox;
//    @UiField ScrollPanelExt westPanel;

    /** ==============  ОСНОВНОЕ горизонтальное меню ============== */
    @UiField MenuBarExt headerMenuBar;
    @UiField MenuItemExt lboMenuItem;           // Бюджетная роспись
    @UiField MenuItemExt fileFromFKMenuItem;    // Загрузка файлов из ФК

    /** Пункт меню "Внутренний контроль" */
    @UiField MenuItemExt finControlMenuItem;    // Внутренний контроль
        @UiField MenuBarExt menuBarFinControl;
        @UiField MenuItemExt finControlDepartmentMenuItem;      // -> Структурные подразделения
            @UiField MenuBarExt menuBarFinControlDepartment;
            @UiField MenuItemExt ListOfDocsForMapOfInnerControlMenuItem;    // -> Перечень операций
            @UiField MenuItemExt ListOfDocsForMapOfInnerControlMenuItem_RoleFounderDep;    // -> Перечень операций
            @UiField MenuItemExt mapOfInnerControlMenuItem;                 // -> Карта ВФК
            @UiField MenuItemExt mapOfInnerControlMenuItem_RoleFounderDep;                 // -> Карта ВФК
            @UiField MenuItemExt journalOfInnerControlMenuItem;             // -> Журнал ВФК
            @UiField MenuItemExt journalOfInnerControlMenuItem_RoleFounderDep;             // -> Журнал ВФК
        @UiField MenuItemExt finControlInstituteMenuItem;       // -> Подведомственные учреждения
            @UiField MenuBarExt menuBarFinControlInstitute;
            @UiField MenuItemExt reportOfInnerControlMenuItem;              // -> Отчёт о проведении мероприятий ВФК
            @UiField MenuItemExt reportOfResultInnerControlMenuItem;         // -> Отчёт о результатах ВФК
            @UiField MenuItemExt reportOfInnerControlMenuItem_RoleInstitute; // -> Отчёт о проведении мероприятий ВФК

    /** Пункт меню "Внутренний аудит" */
    @UiField MenuItemExt auditMenuItem;         // Внутренний аудит
        @UiField MenuBarExt menuBarAudit;
        @UiField MenuItemExt auditDepartmentMenuItem;       // -> Структурные подразделения
            @UiField MenuBarExt menuBarAuditDepartment;
            @UiField MenuItemExt auditPlannedDepartmentMenuItem;        // -> Плановая проверка
            @UiField MenuItemExt auditUnplannedDepartmentMenuItem;      // -> Внеплановая проверка
        @UiField MenuItemExt auditInstituteMenuItem;        // -> Подведомственные учреждения
            @UiField MenuBarExt menuBarAuditInstitute;
            @UiField MenuItemExt auditPlannedDependentMenuItem;         // -> Плановая проверка
            @UiField MenuItemExt auditPlannedDependentMenuItem_RoleInstitute;         // -> Плановая проверка
            @UiField MenuItemExt auditUnplannedDependentMenuItem;       // -> Внеплановая проверка
            @UiField MenuItemExt auditUnplannedDependentMenuItem_RoleInstitute;       // -> Внеплановая проверка
            @UiField MenuItemExt externalControlReportMenuItem;         // -> Отчёт финансового контроля
            @UiField MenuItemExt externalControlReportMenuItem_RoleInstitute;         // -> Отчёт финансового контроля
        @UiField MenuItemExt auditSubsidyMenuItem;          // -> Межбюджетные трансферты
            @UiField MenuBarExt menuBarAuditSubsidy;
            @UiField MenuItemExt auditPlannedNkoMenuItem;               // -> Плановая проверка
            @UiField MenuItemExt auditUnplannedNkoMenuItem;             // -> Внеплановая проверка

    /** Пункт меню "Нарушения" */
    @UiField MenuItemExt violationMenuItem;
        @UiField MenuBarExt menuBarViolation;
        @UiField MenuItemExt NVA_SPR_FKAU_VidNarMenuItem;       // -> Виды нарушений
        @UiField MenuItemExt NVA_SPR_FKAU_ViolationMenuItem;    // -> Формулировки нарушений
        @UiField MenuItemExt Test_violationMenuItem;

    /** Пункт меню "Справочники" */
    @UiField MenuItemExt guidesMenuItem;
        @UiField MenuBarExt menuBarGuides;
        @UiField MenuItemExt guidesInstituteMenuItem;           // -> Учреждения, структурные подразделения, сотрудники
            @UiField MenuBarExt menuBarGuidesInstitutes;
            @UiField MenuItemExt institute;                             // -> Учреждения
            @UiField MenuItemExt NVA_SPR_DepartmentsMenuItem;           // -> Департаменты
            @UiField MenuItemExt NVA_SPR_OTDELMenuItem;                 // -> Отделы
            @UiField MenuItemExt NVA_SPR_User_DepartmentsMenuItem;      // -> Связь пользователей с департаментами
            @UiField MenuItemExt position;                              // -> Должности
            @UiField MenuItemExt signer;                                // -> Подписывающие лица
            @UiField MenuItemExt NVA_SPR_MFWorkersMenuItem;             // -> Сотрудники Министерства
            @UiField MenuItemExt NVA_SPR_PositionsMenuItem;             // -> Должности
        @UiField MenuItemExt guidesBudgetProcedureMenuItem;     // -> Бюджетные процедуры
            @UiField MenuBarExt menuBarGuidesBudgetProcedure;
            @UiField MenuItemExt NVA_SPR_FKAU_Proc_ProcessMenuItem;     // -> Процедуры/процессы
            @UiField MenuItemExt NVA_SPR_FKAU_OperMenuItem;             // -> Операции
            @UiField MenuItemExt NVA_SPR_NPAMenuItem;                   // -> Нормативные правовые акты
        @UiField MenuItemExt guidesFinControlMenuItem;          // -> Контроль
            @UiField MenuBarExt menuBarGuidesFinControl;
            @UiField MenuItemExt NVA_SPR_FK_CONTROL_OBJECTMenuItem;     // -> Объекты контроля
            @UiField MenuItemExt NVA_SPR_FK_MethodMenuItem;             // -> Методы контроля
            @UiField MenuItemExt NVA_SPR_FK_SpKontrMenuItem;            // -> Способы контроля
            @UiField MenuItemExt NVA_SPR_FK_VidKontrMenuItem;           // -> Виды контроля
            @UiField MenuItemExt NVA_SPR_FK_KDMenuItem;                 // -> Контрольные действия
            @UiField MenuItemExt NVA_SPR_FK_LevelRiskMenuItem;          // -> Уровни рисков
            @UiField MenuItemExt NVA_SPR_FKAU_REASONMenuItem;           // -> Основания для проведения внутреннего контроля
            @UiField MenuItemExt NVA_SPR_FK_KD_PeriodMenuItem;          // -> Периодичность выполнения контрольного действия
            @UiField MenuItemExt NVA_SPR_FK_KD_ProdMenuItem;            // -> Продолжительность выполнения контрольного действия
            @UiField MenuItemExt NVA_SPR_FK_TypeDocMenuItem;            // -> Типы документов проверки
            @UiField MenuItemExt NVA_SPR_FK_TypeDoc_ProcNaprMenuItem;   // -> Связь видов документов с финансовой процедурой контроля
        @UiField MenuItemExt guidesAuditMenuItem;               // -> Аудит
            @UiField MenuBarExt menuBarGuidesAudit;
            @UiField MenuItemExt NVA_SPR_FKAU_TipProvMenuItem;          // -> Типы проверок
            @UiField MenuItemExt NVA_SPR_FAU_VidPrMenuItem;             // -> Виды аудиторской проверки
            @UiField MenuItemExt NVA_SPR_FAU_METODMenuItem;             // -> Методы аудита
            @UiField MenuItemExt NVA_SPR_FAU_CelMenuItem;               // -> Цели аудиторской проверки
            @UiField MenuItemExt NVA_SPR_FAU_WORK_AUDITMenuItem;        // -> Задачи аудиторской проверки
            @UiField MenuItemExt NVA_SPR_FAU_SUB_AUDIT_CONTROLMenuItem; // -> Темы аудиторских проверок
            @UiField MenuItemExt NVA_SPR_FAU_QUEST_WAYMenuItem;         // -> Вопросы по направлениям деятельности
            @UiField MenuItemExt NVA_SPR_FKAU_ORGMenuItem;              // -> Проверяющие организации
            @UiField MenuItemExt NVA_SPR_FAU_USER_CONTROLMenuItem;      // -> Cотрудники, включаемые в аудиторские проверки
            @UiField MenuItemExt NVA_SPR_FKAU_OsnovMenuItem;            // -> Основания для продления или приостановления проверок
            @UiField MenuItemExt NVA_SPR_FKAU_VidOtvMenuItem;           // -> Виды ответственности
        @UiField MenuItemExt guidesGeneralMenuItem;             // -> Общие
            @UiField MenuBarExt menuBarGuidesGeneral;
            @UiField MenuItemExt NVA_SPR_FAU_REPORTMenuItem;            // -> Отчеты
            @UiField MenuItemExt NVA_SPR_FAU_DATE_REPORTMenuItem;       // -> Регламентные даты для отчётов


    /** Пункт меню "Администрирование" */
    @UiField MenuItemExt usersMenuItem;
        @UiField MenuBarExt menuBarAdmin;
        @UiField MenuItemExt users;                             // -> Карточки пользователей
        @UiField MenuItemExt usersRoles;                        // -> Назначение ролей, доступных пользователю
        @UiField MenuItemExt guideRoles;                        // -> Справочник ролей
        @UiField MenuItemExt journalNoticeMenuItem;             // -> Уведомления пользователей
        @UiField MenuItemExt auditNpaMenuItem;                  // -> Копирование из БД ДОКа
        @UiField MenuItemExt auditViolationNpa;                 // -> Анализ формулировок
        @UiField MenuItemExt journalsMenuItem;                  // -> Журналы
            @UiField MenuBarExt menuBarJournals;
            @UiField MenuItemExt journalOperationMenuItem;              // -> Журнал событий
            @UiField MenuItemExt usersSessionsMenuItem;                 // -> Журнал сеансов

    /** Пункт меню "Форум" */
    @UiField MenuItemExt forumMenuItem;

    /**Пункт меню Аудит НПА*/
    //@UiField MenuItemExt auditNpaMenuItem;

    /* ============================================================ */

    //@UiField MenuItemExt structuralAuditMenuItem;
    //@UiField MenuItemExt dependentAuditMenuItem;
    //@UiField MenuItemExt nsiMenuItem;
//    @UiField MenuItemExt externalControlReportMenuItem;
    //@UiField MenuItemExt settingMenuItem;


    /** Панель содержащая меню финконтроля */
//    @UiField MenuBarExt menuBarFinControl;
//    @UiField MenuItemExt ListOfDocsForMapOfInnerControlMenuItem;
//    @UiField MenuItemExt mapOfInnerControlMenuItem;
//    @UiField MenuItemExt journalOfInnerControlMenuItem;
//    @UiField MenuItemExt reportOfInnerControlMenuItem;
//    @UiField MenuItemExt reportOfResultInnerControlMenuItem;

    /** Панель содержащая меню внутреннего аудита */
//    @UiField MenuBarExt menuBarStructuralAudit;
//    @UiField MenuItemExt auditPlannedStructuralMenuItem;
//    @UiField MenuItemExt auditUnplannedStructuralMenuItem;

    /** Панель содержащая меню ведомсвенного аудита */
//    @UiField MenuBarExt menuBarDependentAudit;
//    @UiField MenuItemExt auditPlannedDependentMenuItem;
//    @UiField MenuItemExt auditUnplannedDependentMenuItem;
//    @UiField MenuItemExt auditPlannedNkoMenuItem;

    //Боковая менюшка к согласованию НСИ
    //@UiField MenuBarExt menuBarNSI;
    //@UiField MenuItemExt NVA_SPR_FKAU_OBJMenuItem;

    //Боковая менюшка к настройкам
    //@UiField MenuBarExt menuBarSetting;
    //@UiField MenuItemExt settingForumMenuItem;
//    @UiField MenuItemExt settingOtherMenuItem;


    List<Integer> years;
    /**
     * [12:22:51] Надежда Потапова: текущий год
     */
    private int currentYear;
    /**
     * [12:16:38] Надежда Потапова: мне она нужна чтобы если пользователь выбирает другую вкладку
     * 	а до этого выбрал отличный от текущего года год, чтобы этот год не слетал
     */
    private int useYear;

    private boolean isDefaultYearRunOnce = false;


    @Inject
    private UserInfoPopover userInfoPopover;

    /**
     * инициализация главного меню
     *  инициализируются ссылки по умолчанию (не изменяемые в процессе работы (смены пользователя и т.п.))
     *  выставляются разделители между пунктами
     */
    @Inject // PostConstruct
    private void initMenuHeader()
    {
        // блок инициализации ссылок по умолчанию

        // горизонтальное меню

        // ----------------------- Внутренний контроль

        // Отчёт о результатах внутреннего финансового контроля
        //TODO: Добавить ссылку на соответствующий модуль (разработкой данного режима занимается отдел Полторацкой)
        reportOfResultInnerControlMenuItem.setHyperLink(placeHistoryMapper.getToken(
                new FramePlace("app.jsp#/resultHeaders", "Отчёт о результатах внутреннего финансового контроля")));
        //reportControlMenuItem2.setHyperLink(placeHistoryMapper.getToken(new FramePlace("", "Отчёт о результатах внутреннего финансового контроля")));
        // Отчёт о проведении мероприятий внутреннего контроля
        reportOfInnerControlMenuItem.setHyperLink(placeHistoryMapper.getToken(
                new FramePlace("app.jsp#/activityHeaders", "Отчёт о проведении мероприятий внутреннего контроля")));
        reportOfInnerControlMenuItem_RoleInstitute.setHyperLink(placeHistoryMapper.getToken(
                new FramePlace("app.jsp#/activityHeaders", "Отчёт о проведении мероприятий внутреннего контроля")));

        finControlMenuItem.getMenuBarExtList().add(menuBarFinControl);
        finControlDepartmentMenuItem.getMenuBarExtList().add(menuBarFinControlDepartment);
        finControlInstituteMenuItem.getMenuBarExtList().add(menuBarFinControlInstitute);

        // ----------------------- Внутренний аудит




        // Формулировки нарушений
        NVA_SPR_FKAU_ViolationMenuItem.setHyperLinkW(placeHistoryMapper.getToken(
                new FramePlace("app.jsp#/violationHeaders", "НСИ/Формулировки нарушений")));

        Test_violationMenuItem.setHyperLinkW(placeHistoryMapper.getToken(
                new FramePlace("audit-violation.html", "НСИ/Тест - Фиксация нарушений")));

        // ----------------------- СПРАВОЧНИКИ
        // --- I. Учреждения, структурные подразделения, сотрудники
        // учреждения
        institute.setHyperLink(placeHistoryMapper.getToken(new GuideInstitutePlace()));
        // департаменты

        // должности
        position.setHyperLink(placeHistoryMapper.getToken(new GuidePositionPlace()));
        // подписывающие лица
        signer.setHyperLink(placeHistoryMapper.getToken(new GuideSignerPlace(null)));
        // Сотрудники Министерства
        //NVA_SPR_MFWorkersMenuItem.setHyperLinkW(placeHistoryMapper.getToken(new NVA_SPR_MFWorkersPlace()));
        // Должности
        //NVA_SPR_PositionsMenuItem.setHyperLinkW(placeHistoryMapper.getToken(new NVA_SPR_PositionsPlace()));


        // ----------------------- Администрирование
        // Карточки пользователей
        users.setHyperLink(placeHistoryMapper.getToken(new UsersPlace()));
        // Роли пользователей
        usersRoles.setHyperLink(placeHistoryMapper.getToken(new UsersRolesPlace()));
        // Справочик ролей
        guideRoles.setHyperLink(placeHistoryMapper.getToken(new GuideRolesPlace()));

        // --- ЖУРНАЛЫ
        // Журнал операций
        journalOperationMenuItem.setHyperLink(placeHistoryMapper.getToken(new JournalOperationPlace()));
        // Журнал сессий
        usersSessionsMenuItem.setHyperLink(placeHistoryMapper.getToken(new UsersSessionsPlace()));


        //------------------------ Аудит НПА
        auditNpaMenuItem.setHyperLink(placeHistoryMapper.getToken(
                new FramePlace("audit-npa.html#/activityHeaders", "НСИ/Администрирование/Автоматизированная правовая система"
                )));

        auditViolationNpa.setHyperLink(placeHistoryMapper.getToken(
                new FramePlace("audit-violation-npa.html#/activityHeaders", "НСИ/Администрирование/Анализ формулировок нарушений"
                )));
        // согласование НСИ
        // nsiMenuItem.setHyperLink(placeHistoryMapper.getToken(new NSI2Place("")));
//        nsiMenuItem.setHyperLink(placeHistoryMapper.getToken(new NVA_SPR_FKAU_Proc_ProcessPlace()));
//        nsiMenuItem.getMenuBarExtList().add(menuBarNSI);
        /*
//        NVA_SPR_FKAU_OBJMenuItem.setHyperLinkW(placeHistoryMapper.getToken(new NSI2Place("FKAU_OBJ")));
        // справочники
        guidesMenuItem.setHyperLink(placeHistoryMapper.getToken(new GuideInstitutePlace()));
        // привязка бокового меню к элементу
        guidesMenuItem.getMenuBarExtList().add(menuBarGuidesInstitutes);
        institute.setHyperLink(placeHistoryMapper.getToken(new GuideInstitutePlace()));
        position.setHyperLink(placeHistoryMapper.getToken(new GuidePositionPlace()));
        signer.setHyperLink(placeHistoryMapper.getToken(new GuideSignerPlace(null)));
*/
        // Настройки
//        settingMenuItem.setHyperLink(placeHistoryMapper.getToken(new SettingForumPlace()));
//        settingMenuItem.getMenuBarExtList().add(menuBarSetting);
//        settingForumMenuItem.setHyperLinkW(placeHistoryMapper.getToken(new SettingForumPlace()));
        //settingOtherMenuItem.setHyperLinkW(placeHistoryMapper.getToken(new SettingForumPlace()));

        // блок формирования разделителей между меню
//        for(MenuItemExt menuItem : headerMenuBar.getItemsExt())
//            menuItem.setMenuItemSeparator(headerMenuBar.insertSeparator(headerMenuBar.getItemIndex(menuItem)));
//        headerMenuBar.addSeparator();

    }

    /**
     * активация пункта меню головного меню
     * @param menuHeaderItem
     * @param isWestMenuShown показать боковое меню
     */
//    private void activateMenu(MenuItemExt menuHeaderItem, boolean isWestMenuShown)
//    {
//
//        for (MenuItemExt menuItem : headerMenuBar.getItemsExt())
//        {
//            menuItem.setStyleDependentName("active", menuItem.equals(menuHeaderItem));
//            for (MenuBarExt ext: menuItem.getMenuBarExtList()) {
//                ext.setStyleDependentName("vertical-left", true);
//                ext.setVisible(menuItem.equals(menuHeaderItem));
//            }
//        }
//        setWestMenuShown(isWestMenuShown);
//    }

    /**
     * активация меню
     * @param menuHeader элемент меню
     */
//    private void activateMenu(MenuItemExt menuHeader)
//    {
//        this.activateMenu(menuHeader, false);
//    }


    @SuppressWarnings("deprecation")
    public MainViewImpl() {

        logger.info("	перекрываем стиль по-умолчанию для всех gwt-DateBox");
        DateBoxResources dateBoxResources = GWT.create(DateBoxResources.class);
        dateBoxResources.dateBoxCss().ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));

        // считываем версию ПО
        verisonSpan.setInnerHTML(versionDiv.getInnerHTML());

        // вставляем на главную страницу css стили
        resourcesCore.mainViewStyle().ensureInjected();
        //resourcesCore.menuBarCss().ensureInjected();
        resourcesCore.stackLayoutPanelCss().ensureInjected();
        resourcesApp.menuBarCss().ensureInjected();

        // несмотря на то что в JAVA метод getYear deprecated, в javascript это вполне допустимо,
        // 	и никакого отношения к deprecated в JAVA не имеет
        // 	getYear() возвращает the year minus 1900
        currentYear = 1900 + new Date().getYear();
        years = new ArrayList<>();
        for(int i = BEGIN_YEAR; i < currentYear + 2; i++){
            years.add(i);
        }
        // если не назначить значение, а затем вызвать setAcceptableValues то в список попадает Null
        //	чтобы этого избежать назначаем значение, но запрещаем выстреливать событие
        yearListBox.setValue(BEGIN_YEAR, false);
        yearListBox.setAcceptableValues(years);
        yearListBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {

            @Override
            public void onValueChange(ValueChangeEvent<Integer> event) {
                Integer newValue = event.getValue();
                if (newValue != null && newValue != 0) {
                    useYear = newValue;
                    eventBus.fireEvent(new YearChangedEvent(newValue));
                }
            }
        });
        //activateMenu(dependentAuditMenuItem);

        userFullName.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                Widget source = (Widget) event.getSource();
                userInfoPopover.showAt(source);
            }
        });

        userFullName.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                userInfoPopover.hide();
            }
        });
    }



    @UiFactory
    public ValueListBox<Integer> makeValueListBox() {
        return new ValueListBox<>(new AppendlessRenderer<Integer>() {

            /**
             * Описывает как показывать выбранное значение в текстовом поле
             * @param object
             * @return
             */
            @Override
            public String render(Integer object) {
                if(object != null)
                    return object.toString();

                return null;
            }
        });
    }
    @Override
    public void setDisplay(AcceptsOneWidget display) {
        dockLayoutPanel.add((Widget) display);
    }


    @Override
    public void setBreadCrumb(SafeHtml safeHtml) {
        breadCrumb.setHTML(safeHtml);
    }

    @Inject
    @Override
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;

        eventBus.addHandler(UserChangedEvent.TYPE, new UserChangedEventHandler() {
            @Override
            public void onUserChange(UserChangedEvent event) {
                doOnUserChange(event);
            }
        });
        eventBus.addHandler(LetterGetEvent.TYPE, new LetterGetEventHandler() {
            @Override
            public void onLetterGet(LetterGetEvent event) {
                doOnGetLetter(event);
            }
        });
    }

    @Override
    public void setPlaceHistoryMapper(PlaceHistoryMapper placeHistoryMapper) {
        this.placeHistoryMapper = placeHistoryMapper;
    }

    @Override
    public void setLogedInUser(UserProxy currentUser) {

        logger.log(Level.INFO, DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()) + " Вы вошли как \"" + currentUser.getUsername() + "\"");

        logedInProfileHyperlink.setText(currentUser.getUsername());
        logedInProfileHyperlink.setTargetHistoryToken(placeHistoryMapper.getToken(new UserProfilePlace(currentUser.getUsername(), currentUser.getId())));


        String innerText = Strings.isNullOrEmpty(currentUser.getFullname()) ?
                "(нет данных)" : "(" + currentUser.getFullname() + ")";
        userFullName.setText(innerText);
        userInfoPopover.setUserInfo(currentUser);


        InstituteProxy institute = currentUser.getInstitute();
        if(institute != null){
            userInstituteSpan.setInnerHTML(institute.getCode() + " " + institute.getNameShort());
        }
    }

    /**
     * назначим первый раз значение по умолчанию (текущий год)
     * инициируем цепочку событий связанных с установкой финансоового года
     */
    @Override
    public void setDefaultYear() {
        if(!isDefaultYearRunOnce) {
            isDefaultYearRunOnce = true;

            //хардкод чтобы срабатывал метод смены года, для списка лет из 1 года
            yearListBox.setValue(null, true);
            yearListBox.setValue(useYear != 0 ? useYear : currentYear, true);
            yearListBox.setAcceptableValues(years);
        }
    }

    @Override
    public void initHyperlinks(UserProxy currentUser) {
        // initMenuHeader();

    }

//    @Override
//    public void setDisplayMenuBar() {
//        // Метод History.getToken() возвращал разные результаты в зависимости от того
//        // перешёл ли пользователь по прямой ссылке или был вызван PlaceConntroller.goTo().
//        // Sheduler решает эту проблему
//        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//            @Override
//            public void execute() {
//                String currentToken = History.getToken();
//                if(currentToken.contains(NvaSprOtdelPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FKAU_Proc_ProcessPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FK_CONTROL_OBJECTPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FKAU_REASONPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FK_TypeDoc_ProcNaprPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FAU_DATE_REPORTPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FAU_USER_CONTROLPlace.PREFIX)
//                        || currentToken.contains(NvaSprDepartmentsPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_User_DepartmentsPlace.PREFIX)
//                        || currentToken.contains(NVA_SPR_FKAU_OperPlace.PREFIX)){
////                    activateMenu(nsiMenuItem, true);
//                } else if (currentToken.contains("violationHeaders")){
////                    activateMenu(nsiMenuItem, true);
//                }
//                else if (currentToken.contains("guide")
//                        || currentToken.contains("Guide")
//                        || currentToken.contains("Spr")) {
//                    // настраиваем меню справочников
////                    activateMenu(guidesMenuItem, true);
//                } else if (currentToken.contains("Users")
//                        || currentToken.contains("Roles")
//                        || currentToken.contains("Notice")
//                        || currentToken.contains("Logs")
//                        || currentToken.contains("ServerMaintenance")) {
//                    // настраиваем меню админки
////                    activateMenu(usersMenuItem, true);
//
//                    // настраиваем меню в режиме Финансовый контроль
//                } else if (currentToken.contains("Operations")
//                        || currentToken.contains("Session")) {
//                    // настраиваем меню в режиме Журналов
////                    activateMenu(journalsMenuItem, true);
//
//                }  else if (currentToken.contains(InnerControlPlace.PREFIX)
//                        || currentToken.contains(MapInnerControlPlace.PREFIX)
//                        || currentToken.contains(DocForMapControlPlace.PREFIX)) {
////                    activateMenu(finControlMenuItem,true);
//                }  else if (currentToken.contains(DocForMapControlEditPlace.PREFIX) || currentToken.contains(InnerControlEditPlace.PREFIX)) {
////                    activateMenu(finControlMenuItem);
//                } else if (currentToken.contains("activityHeaders")){
////                    activateMenu(finControlMenuItem,true);
//                } else if (currentToken.contains(AuditOrderPlace.PREFIX)) {
//                    setWestMenuShown(false);
//                } else if (currentToken.contains(AuditGroupPlace.PREFIX)) {
//                    setWestMenuShown(false);
//                } else if (currentToken.contains(AuditProgramPlace.PREFIX)) {
//                    setWestMenuShown(false);
//                } else if (currentToken.contains(AuditGroupPlace.PREFIX)) {
//                    setWestMenuShown(false);
//                } else if (currentToken.contains(AuditPlanPlace.PREFIX)
//                        || currentToken.contains(AuditRowPlace.PREFIX)) {
//                    setWestMenuShown(false);
//                } else if (currentToken.contains(AuditMainPlace.PREFIX)) {
//                    AuditMainPlace p = new AuditMainPlace(currentToken);
//                    String kindId = p.getKeys().get(AuditMainPlace.KIND_KEY);
////                    if (kindId.equals(AuditMainPlace.PLANNED_DEPENDENT_ID)) {
////                        activateMenu(dependentAuditMenuItem, true);
////                    } else if (kindId.equals(AuditMainPlace.PLANNED_STRUCTURAL_ID)) {
////                        activateMenu(structuralAuditMenuItem, true);
////                    } else if (kindId.equals(AuditMainPlace.UNPLANNED_STRUCTURAL_ID)) {
////                        activateMenu(structuralAuditMenuItem, true);
////                    } else if (kindId.equals(AuditMainPlace.UNPLANNED_DEPENDENT_ID)) {
////                        activateMenu(dependentAuditMenuItem, true);
////                    } else if (kindId.equals(AuditMainPlace.UNPLANNED_NKO_KIND_ID)) {
////                        activateMenu(dependentAuditMenuItem, true);
////                    } else {
////                        activateMenu(dependentAuditMenuItem, false);
////                        activateMenu(structuralAuditMenuItem, false);
////                    }
//                }  else if (currentToken.contains(NSI2Place.PREFIX)){
//                    //Cогласование НСИ
////                    activateMenu(nsiMenuItem,true);
//
//                }
//                //TODO: Открыть для тестового показа
//                else if (currentToken.contains(FileFromFKPlace.PREFIX)){
//                    activateMenu(fileFromFKMenuItem);
//                }
//                else if (currentToken.contains(ForumPlace.PREFIX) ||
//                        currentToken.contains(LetterPlace.PREFIX) ||
//                        currentToken.contains(CorrespondencePlace.PREFIX)){
//                    activateMenu(forumMenuItem);
//                }
//                else if (currentToken.contains(LetterPlace.PREFIX)){
//                    activateMenu(forumMenuItem);
//                }
//                else if (currentToken.contains(CorrespondencePlace.PREFIX)) {
//                    activateMenu(forumMenuItem);
//                }
//                //////////////////////////////////////////////////////////////
//                else if (currentToken.contains(SettingForumPlace.PREFIX)){
////                    activateMenu(settingMenuItem,true);
//                } else if (currentToken.contains(ReportPlace.PREFIX)  ||
//                        currentToken.contains(GroupPlace.PREFIX)  ||
//                        currentToken.contains(extReportListInstitutePlace.PREFIX)  ||
//                        currentToken.contains(JournalPlace.PREFIX))
//                {
//                    activateMenu(externalControlReportMenuItem);
//                }
//                //TODO: Открыть для тестового показа
//                else if (currentToken.contains("LboList")){
//                    activateMenu(lboMenuItem,false);
//                }
//                ////////////////////////////////////////////////////////////////
//                else {
//                    // настраиваем меню по умолчанию
////                    activateMenu(dependentAuditMenuItem,true);
//                }
//
//            }
//        });
//    }


//    @Override
//    public void setWestMenuShown(boolean isShow) {
//        westPanel.setHideVerticalScrollBar(true);
//        dockLayoutPanel.setWidgetSize(westPanel, isShow ? 20 : 0);
//    }

    /**
     * Обработка события о смене текущего пользователя
     * @param event
     */
    private void doOnUserChange(UserChangedEvent event) {
        UserProxy user = event.getUser();
        this.setLogedInUser(user);
        this.initHyperlinks(user);
        this.setDefaultYear();

        // пункты которые зависят от пользователей
        // уведомление пользователей
        journalNoticeMenuItem.setHyperLink(placeHistoryMapper.getToken(new JournalNoticePlace(sessionService.getCurrentUser().getId())));

        //Если пользователь имеет роль ROLE_FOUNDER_DEP он видит не один отчет, а Журнал отчетов
        Set<RoleProxy> items = sessionService.getCurrentUser().getRoles();
        for (RoleProxy item: items) {
            if (item.getAuthority().equals(AppAuthority.ROLE_FOUNDER_DEP)) {
                reportOfResultInnerControlMenuItem.setText("Журнал отчётов о результатах внутреннего финансового контроля");
                reportOfResultInnerControlMenuItem.setHyperLink(placeHistoryMapper.getToken(
                        new FramePlace("audit.html#/resultHeaders", "Журнал отчётов о результатах внутреннего финансового контроля")));
                break;
            }
        }


        // финансовый контроль в зависимости от прав
//        if (AppAuthority.hasRole(user, AppAuthority.ROLE_INSTITUTE)) {
//            finControlMenuItem.setHyperLink(placeHistoryMapper.getToken(new FramePlace("app.jsp#/activityHeaders", "Отчет о проведении мероприятий внутреннего контроля")));
//        }
//        else {
//            finControlMenuItem.setHyperLink(placeHistoryMapper.getToken(new DocForMapControlPlace()));
//        }


        // видимость меню
        generateMapMenuItemByRoles(user);
        visibleUpdateMenuItemByRoles(headerMenuBar,user);

        // скрываем повторяющиеся пункты
        if (menuBarFinControl.getItemsExt().contains(finControlInstituteMenuItem)) {
            reportOfInnerControlMenuItem_RoleInstitute.setVisible(false);
        }
        if (menuBarFinControl.getItemsExt().contains(finControlDepartmentMenuItem)) {
            ListOfDocsForMapOfInnerControlMenuItem_RoleFounderDep.setVisible(false);
            mapOfInnerControlMenuItem_RoleFounderDep.setVisible(false);
            journalOfInnerControlMenuItem_RoleFounderDep.setVisible(false);
        }
        if (menuBarAudit.getItemsExt().contains(auditInstituteMenuItem)) {
            auditPlannedDependentMenuItem_RoleInstitute.setVisible(false);
            auditUnplannedDependentMenuItem_RoleInstitute.setVisible(false);
            externalControlReportMenuItem_RoleInstitute.setVisible(false);
        }
    }

    /**
     * Обработка события о наличии новых сообщений у пользователя
     * @param event
     */
    private void doOnGetLetter(LetterGetEvent event) {
        Integer count = event.getCountLetters();
        if(count != null && count > 0) {
            int left = this.yearListBox./*forumMenuItem.*/getAbsoluteLeft() - 350;
            int top = this.yearListBox./*forumMenuItem.*/getAbsoluteTop() + 50;
            makePopupPanel(count);
            popupPanel.setPopupPosition(left, top);
            // Show the popup
            popupPanel.show();
        }
    }

    /**
     * Всплывающее окно для информирования наличия новых сообщений в форуме
     * @param count     количество непрочитанных сообщений
     * @return
     */
    public PopupPanel makePopupPanel(Integer count)
    {
        popupPanel = new PopupPanel(true);
        popupPanel.setWidth("320px");
        //popupPanel.setHeight("150px");

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        String strFont = "<font size=\"3\" style=\"padding: 5px;border-radius: 10px/10px; box-shadow: 5px 5px 3px 1px; width: 200px;text-align:center;";
        strFont +=  "background: #E0E6F8;\">"; // #E3F6CE

        sb.appendHtmlConstant(strFont);
        sb.appendEscaped("У Вас есть непрочитанные сообщения: ");// + count.toString());
        sb.appendHtmlConstant("<b>");
        sb.appendEscaped(count.toString());
        sb.appendHtmlConstant("</b>");
        sb.appendHtmlConstant("</font>");
        sb.appendHtmlConstant("<br/>");
        sb.appendHtmlConstant("<br/>");
        sb.appendHtmlConstant("<font size=\"2\">");
        sb.appendHtmlConstant("<em>");
        sb.appendEscaped("Для просмотра перейдите в пункт меню \"Форум\"");
        sb.appendHtmlConstant("</em>");
        sb.appendHtmlConstant("</font>");
        HTMLPanel dialogContents = new HTMLPanel(sb.toSafeHtml());
        popupPanel.setWidget(dialogContents.asWidget());
        return popupPanel;
    }

    /**
     * показывает или не показывает пункты меню в зависимости от ролей.
     * @param menuBarExt
     * @param user
     */
    private void visibleUpdateMenuItemByRoles(MenuBarExt menuBarExt, UserProxy user)
    {
        // по горизонтальному меню
        for (MenuItemExt menuItemExt:menuBarExt.getItemsExt())
        {
            menuItemExt.setVisible(false); // все пункты отключаем
            if (menuItemExt.getAuthorities().equals("")) {
                 menuItemExt.setVisible(true);
            }
            else
                for (String role: menuItemExt.getAuthoritiesList())
                    if  (AppSecurity.hasRole(user, role)) {
                        menuItemExt.setVisible(true);
                        break;
                    }
            // по вложенным спискам
            for(MenuBarExt menuBar:  menuItemExt.getMenuBarExtList())
                    visibleUpdateMenuItemByRoles(menuBar,user);

        }
    }

    /**
     * построение карты пунктов меню в зависимости от ролей
     * @param user
     *
     * add 09.02.2017 by nburaeva
     */
    private void generateMapMenuItemByRoles(UserProxy user){

        if (AppSecurity.hasRole(user, AppAuthority.ROLE_FOUNDER)) {
            // --- Вложенные пункты меню, которые должны отображаться
            // панель "Подведомственные учреждения" пункта меню "Внутренний контроль"
            if (!menuBarFinControl.getItemsExt().contains(finControlInstituteMenuItem)){
                menuBarFinControl.getItemsExt().add(finControlInstituteMenuItem);
            }
            if (AppSecurity.hasRole(user, AppAuthority.ROLE_FOUNDER_DEP) || AppSecurity.hasRole(user, AppAuthority.ROLE_FOUNDER_EXEC)) {
                // панель "Структурные подразделения" пункта меню "Внутренний контроль"
                if (!menuBarFinControl.getItemsExt().contains(finControlDepartmentMenuItem)) {
                    menuBarFinControl.getItemsExt().add(finControlDepartmentMenuItem);
                }
            }


            // Внутренний аудит -> Структурные подразделения
            if (!menuBarAudit.getItemsExt().contains(auditDepartmentMenuItem)) {
                menuBarAudit.getItemsExt().add(auditDepartmentMenuItem);
            }
            // Внутренний аудит -> Подведомственные учреждения
            if (!menuBarAudit.getItemsExt().contains(auditInstituteMenuItem)) {
                menuBarAudit.getItemsExt().add(auditInstituteMenuItem);
            }
            // Внутренний аудит -> Межбюджетные трансферты
            if (!menuBarAudit.getItemsExt().contains(auditSubsidyMenuItem)) {
                menuBarAudit.getItemsExt().add(auditSubsidyMenuItem);
            }
        } else {
            // --- Вложенные пункты меню, которые надо скрыть
            // панель "Подведомственные учреждения" пункта меню "Внутренний контроль"
            if (menuBarFinControl.getItemsExt().contains(finControlInstituteMenuItem)){
                menuBarFinControl.removeItem(finControlInstituteMenuItem);
            }
            // панель "Структурные подразделения" пункта меню "Внутренний контроль"
            if (menuBarFinControl.getItemsExt().contains(finControlDepartmentMenuItem)){
                menuBarFinControl.removeItem(finControlDepartmentMenuItem);
            }

            // Внутренний аудит -> Структурные подразделения
            if (menuBarAudit.getItemsExt().contains(auditDepartmentMenuItem)){
                menuBarAudit.removeItem(auditDepartmentMenuItem);
            }
            // Внутренний аудит -> Подведомственные учреждения
            if (menuBarAudit.getItemsExt().contains(auditInstituteMenuItem)){
                menuBarAudit.removeItem(auditInstituteMenuItem);
            }
            // Внутренний аудит -> Межбюджетные трансферты
            if (menuBarAudit.getItemsExt().contains(auditSubsidyMenuItem)){
                menuBarAudit.removeItem(auditSubsidyMenuItem);
            }
        }

    }

}
