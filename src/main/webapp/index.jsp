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

  <form action="<c:url value="/score"/>" method="post">

    <div class="row">

      <div class="input-field col s12">
        <select id="problem" name="problem" required>
          <option value="a_example.in" selected>Example</option>
          <option value="b_should_be_easy.in">Should be easy</option>
          <option value="c_no_hurry.in">No hurry</option>
          <option value="d_metropolis.in">Metropolis</option>
          <option value="e_high_bonus.in">High bonus</option>
        </select>
        <label for="problem">Problem</label>
      </div>

    </div>

    <div class="row">
      <div class="col s12">
        <label for="data">Solution</label>
        <textarea id="data" name="data" style="height: 8em"></textarea>
      </div>
    </div>

    <div class="row">
      <div class="col s12">
        <input type="submit" class="btn">
      </div>
    </div>

  </form>

</div>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>

<script>(function($){$(document).ready(function(){
    $('select').material_select();
    $(".dropdown-button").dropdown();
});})(jQuery);</script>

</body>
</html>
