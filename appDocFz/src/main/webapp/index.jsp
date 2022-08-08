<!--
    Этот файл нужен для GWT DevMode в IDEA,
    потому что при запуске она ищёт в директории файл index.{html,html,jsp},
    а без них не может автоматически открыть вкладку в браузере.
    По url /index.html слушает ControllerServlet.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:forward page="/index.html" />