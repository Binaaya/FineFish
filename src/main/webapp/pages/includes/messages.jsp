<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Success Message -->
<c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success">
        <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
        <c:remove var="successMessage" scope="session" />
    </div>
</c:if>

<!-- Error Message -->
<c:if test="${not empty sessionScope.errorMessage}">
    <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
        <c:remove var="errorMessage" scope="session" />
    </div>
</c:if>

<!-- Error Message (Request Scope) -->
<c:if test="${not empty requestScope.errorMessage}">
    <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i> ${requestScope.errorMessage}
    </div>
</c:if>
<style>
/* Alert Messages */
.alert {
    padding: 15px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    border-radius: 8px;
    font-size: 16px;
    position: relative;
    animation: fadeIn 0.5s;
}

.alert i {
    margin-right: 8px;
}

.alert-success {
    color: #155724;
    background-color: #d4edda;
    border-color: #c3e6cb;
}

.alert-error {
    color: #721c24;
    background-color: #f8d7da;
    border-color: #f5c6cb;
}

/* For mobile devices */
@media (max-width: 768px) {
    .alert {
        padding: 12px;
        font-size: 14px;
    }
}
</style>