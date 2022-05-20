

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<title>Search for timetable</title>
</head>
<body>
<div class="top-menu">
    <ul class="nav pull-right top-menu">
        <li><a style="background-color: #245269" class="logout" href="/login">login</a></li>
    </ul>
</div>
<sec:authorize access="hasAnyRole('ROLE_USER','ROLE_ADMIN',permitAll())">

<div align="center" class="container">
    <div class="container">
        <div  style="box-shadow: 2px 5px 6px 2px rgba(0,0,0, 0.5); padding: 0" class="login-wrap form-login no-padding">


                <h2 style="background-color: #245269" class="form-login-heading">Planning des examens</h2>

                <br>


            <form:form class="formTeacher" method="post" modelAttribute="teacherSearchForm" action="" role="form" id="myForm">
                <form:select required="true" class="selectpicker btn-padding" data-live-search="true" path="teacher" data-width="100%">
                    <form:option disabled="true" value="">Choose a teacher</form:option>
                    <form:options items="${teachers}" itemValue="id"/>
                </form:select>
                <div class="btn-padding2">

                    <p><input onclick="func(this)" id="examTeacher" type="submit" name="forTeacher" value="Exams" class="btn btn-info subBtn btn-block"></p>
                </div>




                    <br>
                    <div class="btn-padding">
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <p><a class="btn btn-md btn-success btn-block" href="/timetable/exam" role="button">Espace administrateur</a></p>
                    <p><a class="btn btn-md btn-success btn-block" href="/anas/chart" role="button">Statistiques</a></p>
                </sec:authorize>

                        <p><a class="btn btn-md btn-danger btn-block" href="/logout" role="button">Deconnexion </a></p>
                    </div>


            </form:form>
            </sec:authorize>
        </div>

    </div>

</div>

<footer style="background-color: #245269" class="site-footer ff">
    <div style="background-color: #245269" class="text-center">
        <p>&copy; Ensaj 2022</p>
    </div>
</footer>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/resources/assets/js/jquery.backstretch.min.js"></script>
<script>
    $.backstretch("${pageContext.request.contextPath}/resources/assets/img/aya.png", {speed: 80});
    function func(input) {
        var id = 0;
        id = input.id;
        if (id == "group") $("#formGroup").attr('action', 'for/group');
        if (id == "groupExam") $("#formGroup").attr('action', 'exam/for/group');
        if (id == "teacher") $(".formTeacher").attr('action', 'for/teacher');
        if (id == "examTeacher") $(".formTeacher").attr('action', 'exam/for/teacher');
    }
</script>
<%@include file="footer.jsp" %>

