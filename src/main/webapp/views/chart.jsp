

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>
<title>Classrooms</title>
</head>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/jquery-3.3.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/statistique.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<section id="container">
    <header style="background-color: #245269" class="header black-bg">
        <div class="sidebar-toggle-box">
            <div class="fa fa-bars tooltips" data-placement="right" data-original-title="Collapse"></div>
        </div>
        <a href="/" class="logo"><b>Emploi du temps</b></a>
        <div class="top-menu">
            <ul class="nav pull-right top-menu">
                <li><a class="logout" href="/logout">Deconnexion</a></li>
            </ul>
        </div>
    </header>

    <aside>
        <div id="sidebar" class="nav-collapse ">
            <ul class="sidebar-menu" id="nav-accordion">

                <p class="centered"><img src="${pageContext.request.contextPath}/resources/assets/img/admin.png" class="img-circle" width="60"></p>
                <h5 class="centered">ADMIN</h5>
                <li class="li_location">
                    <a href="/">
                        <i class="fa fa-arrow-left"></i>
                        <span>Back</span>
                    </a>
                </li>

                <li class="sub-menu">

                     <a href="javascript:;">
                         <i class="fa fa-tasks"></i>
                         <span>Timetable creation</span>
                     </a>

                    <!--
                   <ul class="sub">
                       <li class="active"><a href="/timetable">Lessons</a></li>
                   </ul>-->
                    <ul class="sub">
                        <li><a href="/timetable/exam">Exams</a></li>
                    </ul>
                    <!--  <ul class="sub">
                          <li><a href="/timetable/curriculum">Curriculums</a></li>
                      </ul> -->
                </li>
                <li class="sub-menu">
                    <a class="active" href="javascript:;">
                        <i class="fa fa-th"></i>
                        <span>Data management</span>
                    </a>
                    <ul class="sub">
                        <li><a href="/timetable/groups">Groupes</a></li>
                        <li><a href="/timetable/teachers">Professeurs</a></li>
                        <li><a href="/timetable/subjects">Modules</a></li>
                        <li class="active"><a href="/timetable/classrooms">Classes</a></li>
                    </ul>
                </li>

            </ul>
        </div>
    </aside>

    <section id="main-content">
        <section class="wrapper">

            <button class="position btn btn-success btn-xs" data-toggle="modal" data-target="#classroomAddModal"><i class="fa fa-plus"></i></button>




                            <div align="center">
                                <div>Le nombre des examens en fonction des professeurs </div>
                                <div id="jackpot-container" class="col-sm-12">

                                    <div id="jackpot-item-container">
                                        <div class="row">

                                            <div class="col-sm-6 mb-2">

                                                <canvas id="myChart" width="600" height="600" class="centered"></canvas>

                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>



        </section>
    </section>

    <footer style="background-color: #245269" class="site-footer">
        <div style="background-color: #245269" class="text-center">
            <p>&copy; Ensaj 2022</p>
        </div>
    </footer>
</section>

<script src="${pageContext.request.contextPath}/resources/assets/js/jquery.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/bootstrap.min.js"></script>
<script class="include" type="text/javascript" src="${pageContext.request.contextPath}/resources/assets/js/jquery.dcjqaccordion.2.7.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/jquery.scrollTo.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/jquery.nicescroll.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/common-scripts.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/jquery-ui-1.9.2.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/bootstrap-switch.js"></script>

<style>body, html { overflow:hidden; }

#jackpot-container {
    border-style: solid;
    border-width: 1px;
    height: 700px;
    padding: 0;
    margin: 0 auto;
}
#jackpot-item-container {
    border-style: solid;
    border-width: 1px;
    width: 700px;
    text-align: center;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate3d(-50%, -50%, 0);
}</style>
</body>
</html>

