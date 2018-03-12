<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul id="sets-dropdown" class="dropdown-content">
    <li><a href="<c:url value="/static/downloads/a_example.in"/>">Example Set</a></li>
    <li><a href="<c:url value="/static/downloads/b_should_be_easy.in"/>">Should Be Easy Set</a></li>
    <li><a href="<c:url value="/static/downloads/c_no_hurry.in"/>">No Hurry Set</a></li>
    <li><a href="<c:url value="/static/downloads/d_metropolis.in"/>">Metropolis Set</a></li>
    <li><a href="<c:url value="/static/downloads/e_high_bonus.in"/>">High Bonus Set</a></li>
</ul>
<nav>
    <div class="nav-wrapper">
        <a href="#" class="brand-logo">Unofficial 2018 <span class="hide-on-med-and-down">Hash Code</span> Judge</a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li><a href="<c:url value="/static/downloads/online_qualification_round_2018.pdf"/>" target="_blank">Problem Statement</a></li>
            <li><a class="dropdown-button" href="#!" data-activates="sets-dropdown">Data Sets<i class="material-icons right">arrow_drop_down</i></a></li>
            <li><a href="<c:url value="/logout.jsp"/>"><i class="material-icons">exit_to_app</i></a></li>
        </ul>
    </div>
</nav>
