<%--@elvariable id="problem" type="java.lang.String"--%>
<%--@elvariable id="oldScore" type="java.lang.Long"--%>
<%--@elvariable id="submission" type="nl.hayovanloon.hashcode2018.models.Submission"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<jsp:include page="/partials/head.jsp" />

<body>

<jsp:include page="/partials/nav.jsp" />

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

  <c:if test="${submission.score - oldScore > 0}">
  <div class="row">
    <div class="col s12">
      <i class="material-icons left orange">priority_high</i><span>You beat your previous score of ${oldScore} by ${submission.score - oldScore} points!</span><i class="material-icons right orange">priority_high</i>
    </div>
  </div>
  </c:if>

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
