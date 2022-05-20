<%--
  Created by IntelliJ IDEA.
  User: Taras
  Date: 02.04.2017
  Time: 14:17
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>
    <title>Login</title>
</head>
<body>
    <div id="login-page">
        <div class="container">
            <form style="box-shadow: 2px 5px 6px 2px rgba(0,0,0, 0.5);" class="form-login" action="/login" method="post">
                <h2 style="background-color: #245269" class="form-login-heading">Admin login</h2>
                <div class="login-wrap">
                    <input type="text" class="form-control" name="username" placeholder="Login" required autofocus>
                    <br>
                    <input type="password" class="form-control" name="password" placeholder="Password" required>
                    <br>

                    <br>
                    <button style="background-color: #245269" class="btn btn-theme btn-block" href="index.html" type="submit"><i class="fa fa-lock"></i> Login</button>

                </div>
            </form>
        </div>
        <div class="container">
            <form style="box-shadow: 2px 5px 6px 2px rgba(0,0,0, 0.5);" class="form-login"  >
                <h2 style="background-color: #245269" class="form-login-heading">session professeur</h2>
                <div class="login-wrap">
                    <input type="text" id="qta_field" value="${item.value}"/>
                    <a href='' onclick="this.href='timetable/exam/teacher/'+document.getElementById('qta_field').value">update</a>


                </div>
            </form>
        </div>

    </div>
    <footer style="background-color: #245269" class="site-footer ff">
        <div style="background-color: #245269" class="text-center">
            <p>&copy; Ensaj 2022</p>
        </div>
    </footer>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery.backstretch.min.js"></script>
<script>
    $.backstretch("${pageContext.request.contextPath}/resources/assets/img/aya.png", {speed: 500});
</script>

<%@include file="footer.jsp"%>