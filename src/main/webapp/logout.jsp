<%--@elvariable id="logoutUrl" type="java.lang.String"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<jsp:include page="/partials/head.jsp" />

<body>

<nav>
  <div class="nav-wrapper">
    <a href="#" class="brand-logo">Unofficial 2018 <span class="hide-on-med-and-down">Hash Code</span> Judge</a>
  </div>
</nav>

<div class="container">

  <div class="row">
    <div class="col s12">
      <h4>Logout</h4>
    </div>
  </div>

  <div class="row">
    <div class="col s12">
      <span>This account has insufficient rights. Log out and use a different account.</span>
    </div>
    <div class="col s6">
      <a href="${logoutUrl}" class="btn">Log Out</a>
    </div>
  </div>

</div>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>

</body>
</html>
