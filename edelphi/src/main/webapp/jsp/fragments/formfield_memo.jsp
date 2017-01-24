<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="classes">formField formMemoField</c:set>
<c:choose>
  <c:when test="${!empty(param.classes)}">
    <c:set var="classes">${classes} ${param.classes}</c:set>
  </c:when>
</c:choose>

<div class="formFieldContainer formMemoFieldContainer">
  <c:choose>
    <c:when test="${param.labelLocale != null}">
       <label class="formFieldLabel" for="${param.id}"><fmt:message key="${param.labelLocale}"/></label>
    </c:when>
    <c:otherwise>
      <label class="formFieldLabel" for="${param.id}">${param.labelText}</label>
    </c:otherwise>
  </c:choose>

  <c:choose>
    <c:when test="${param.disabled eq 'true'}">
      <textarea id="${param.id}" name="${param.name}" class="${classes}" disabled="disabled" title="${param.title}">${param.value}</textarea>
    </c:when>
    <c:otherwise>
      <textarea id="${param.id}" name="${param.name}" class="${classes}" title="${param.title}">${param.value}</textarea>
    </c:otherwise>
  </c:choose>  
</div>