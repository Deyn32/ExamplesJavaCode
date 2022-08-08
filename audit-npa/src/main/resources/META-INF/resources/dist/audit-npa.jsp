





<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <!--Data from Spring-Security CSRF Filter-->
    <script>document.write('<base href="' + document.location + '" />');</script>

    
    
    <meta name="_csrf" content="${_csrf.token}">
    
    <meta name="_csrf_header" content="${_csrf.headerName}">
    
    <meta name="_csrf_parameter" content="${_csrf.parameterName}">
    
    
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta http-equiv="Cache-Control" content="no-cache" />
    <title>Базовая страница</title>

   
       
       <link href="./js/favicon.ico" rel="icon" type="image/x-icon">
       

       
       <link href="styles.css" rel="stylesheet">
       
       <link href="cstyles.css" rel="stylesheet">
       

    <script type="text/javascript">
            (function(window) {
                window.configContextPath = '${pageContext.request.contextPath}' || '';
            })(window);
    </script>

</head>
<body class="layout-column">
<div id="versionDiv" class="hide">Версия ${version} (от ${lastModified})</div>
<route-app>
    <div class="loading-bar-box">
        Идет загрузка
        <img src="./images/loading.gif" width="168" height="40" border="0"/>
    </div>
<route-app>
</body>

    
    <script src="./modules/audit-npa/js/vendor.js" type="text/javascript"></script>
    
    <script src="./modules/audit-npa/js/polyfills.js" type="text/javascript"></script>
    
    <script src="./modules/audit-npa/js/styles.js" type="text/javascript"></script>
    
    <script src="./modules/audit-npa/js/main.js" type="text/javascript"></script>
    

</html>