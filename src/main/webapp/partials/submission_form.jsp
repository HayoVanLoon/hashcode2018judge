<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<span>Simply copy-paste your solution here</span>

<form action="<c:url value="/score"/>" method="post">

    <input type="hidden" name="problem" value="${param.problem}">

    <div class="row">
        <div class="col s12">
            <label for="${param.divid}-data">Solution</label>
            <textarea id="${param.divid}-data" name="data" style="height: 8em"></textarea>
        </div>
    </div>

    <div class="row">
        <div class="col s12">
            <input type="submit" class="btn">
        </div>
    </div>

</form>
