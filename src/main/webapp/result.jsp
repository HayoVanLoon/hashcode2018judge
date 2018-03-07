<%--@elvariable id="problem" type="java.lang.String"--%>
<%--@elvariable id="submission" type="nl.hayovanloon.hashcode2018.models.Submission"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Unofficial 2018 Judge</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <style>
    nav {
      background-color: #7d6eee;
    }
    .btn {
      background-color: #2629a6;
    }
  </style>
</head>
<body>

<ul id="sets-dropdown" class="dropdown-content">
  <li><a href="#">Example Set</a></li>
  <li><a href="#">Should Be Easy Set</a></li>
  <li><a href="#">No Hurry Set</a></li>
  <li><a href="#">Metropolis Set</a></li>
  <li><a href="#">High Bonus Set</a></li>
</ul>
<nav>
  <div class="nav-wrapper">
    <a href="#" class="brand-logo">Unofficial 2018 <span class="hide-on-med-and-down">Hash Code</span> Judge</a>
    <ul id="nav-mobile" class="right hide-on-med-and-down">
      <li><a href="<c:url value="/static/downloads/online_qualification_round_2018.pdf"/>" target="_blank">Problem</a></li>
      <li class="hide"><a class="dropdown-button" href="#!" data-activates="sets-dropdown">Data Sets<i class="material-icons right">arrow_drop_down</i></a></li>
      <li><a href="<c:url value="/logout.jsp"/>"><i class="material-icons">exit_to_app</i></a></li>
    </ul>
  </div>
</nav>

<div class="container">

  <div class="row">
    <div class="col s12">
      <h4>Result</h4>
    </div>
  </div>

  <c:if test="${submission.message != null}">
  <div class="row">
    <div class="col s6 m3">Problem</div>
    <div class="col s6">${problem}</div>
  </div>

  <div class="row">
    <div class="col s6 m3">Message</div>
    <div class="col s6">${submission.message} <i class="material-icons error_outline red left"></i>
    </div>
  </div>
  </c:if>

  <div class="row">
    <div class="col s6 m3">Score</div>
    <div class="col s6">${submission.score}</div>
  </div>

  <div class="row">
    <div class="col s6 m3">Valid Rides</div>
    <div class="col s6">${submission.validRides}</div>
  </div>

  <div class="row">
    <div class="col s6 m3">Bonus Rides</div>
    <div class="col s6">${submission.bonusRides}</div>
  </div>

  <div class="row">
    <div class="col s6 m3">Late Rides</div>
    <div class="col s6">${submission.lateRides}</div>
  </div>

  <div class="row">
    <div class="col s6 m3">Missed Rides</div>
    <div class="col s6">${submission.missedRides}</div>
  </div>

  <div class="row">
    <div class="col s12"><a href="<c:url value="/index.jsp"/>" class="waves-effect waves-light btn">Back</a>
    </div>

  </div>

  <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>

  <script>(function($){$(document).ready(function(){
      $('select').material_select();
      $(".dropdown-button").dropdown();
  });})(jQuery);</script>

</body>
</html>
