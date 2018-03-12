<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<jsp:include page="/partials/head.jsp" />

<body>

<jsp:include page="/partials/nav.jsp" />

<div class="container">

  <ul class="collapsible" data-collapsible="accordion">
    <li>
      <div class="collapsible-header"><i class="material-icons">assignment</i>Example</div>
      <div class="collapsible-body">
        <jsp:include page="/partials/submission_form.jsp">
          <jsp:param name="divid" value="example" />
          <jsp:param name="problem" value="a_example.in" />
        </jsp:include>
      </div>
    </li>
    <li>
      <div class="collapsible-header"><i class="material-icons">lightbulb_outline</i>Should Be Easy</div>
      <div class="collapsible-body">
        <jsp:include page="/partials/submission_form.jsp">
          <jsp:param name="divid" value="shouldbeeasy" />
          <jsp:param name="problem" value="b_should_be_easy.in" />
        </jsp:include>
      </div>
    </li>
    <li>
      <div class="collapsible-header"><i class="material-icons">slow_motion_video</i>No Hurry</div>
      <div class="collapsible-body">
        <jsp:include page="/partials/submission_form.jsp">
          <jsp:param name="divid" value="nohurry" />
          <jsp:param name="problem" value="c_no_hurry.in" />
        </jsp:include>
      </div>
    </li>
    <li>
      <div class="collapsible-header"><i class="material-icons">location_city</i>Metropolis</div>
      <div class="collapsible-body">
        <jsp:include page="/partials/submission_form.jsp">
          <jsp:param name="divid" value="metropolis" />
          <jsp:param name="problem" value="d_metropolis.in" />
        </jsp:include>
      </div>
    </li>
    <li>
      <div class="collapsible-header"><i class="material-icons">card_giftcard</i>High Bonus</div>
      <div class="collapsible-body">
        <jsp:include page="/partials/submission_form.jsp">
          <jsp:param name="divid" value="highbonus" />
          <jsp:param name="problem" value="e_high_bonus.in" />
        </jsp:include>
      </div>
    </li>
  </ul>

</div>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>

<script>(function($){$(document).ready(function(){
    $('select').material_select();
    $(".dropdown-button").dropdown();
});})(jQuery);</script>

</body>
</html>
