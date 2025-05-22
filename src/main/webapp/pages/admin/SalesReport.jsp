<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'admin'}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sales Report - FineFish Admin</title>
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/salesreport.css">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    
    <!-- Custom styles for the sales report page -->
    <style>
        /* Sales Report specific styles */
        .report-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            flex-wrap: wrap;
            gap: 1rem;
        }
        
        .report-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #333333;
        }
        
        .date-filter-form {
            display: flex;
            align-items: center;
            gap: 1rem;
            flex-wrap: wrap;
        }
        
        .form-group-inline {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .form-group-inline label {
            font-weight: 500;
            color: #333333;
            white-space: nowrap;
        }
        
        .date-input {
            padding: 0.5rem;
            border: 1px solid #e1e1e1;
            border-radius: 0.5rem;
            font-family: inherit;
            font-size: 0.9rem;
        }
        
        .filter-btn {
            background-color: #3a86ff;
            color: white;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 0.5rem;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .filter-btn:hover {
            background-color: #2667cc;
        }
        
        .reset-btn {
            background-color: #6c757d;
            color: white;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 0.5rem;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .reset-btn:hover {
            background-color: #5a6268;
        }
        
        .summary-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .summary-card {
            flex: 1 0 200px;
            background-color: #ffffff;
            border-radius: 0.75rem;
            padding: 1.5rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border-left: 4px solid #3a86ff;
        }
        
        .summary-card:nth-child(2) {
            border-left-color: #ff9e1b;
        }
        
        .summary-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
        
        .summary-card-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 1rem;
        }
        
        .summary-card-title {
            font-size: 0.875rem;
            color: #666666;
            margin-bottom: 0.5rem;
        }
        
        .summary-card-value {
            font-size: 1.5rem;
            font-weight: 700;
            color: #333333;
        }
        
        .summary-card-icon {
            width: 40px;
            height: 40px;
            background-color: #f5f5f5;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.25rem;
        }
        
        .summary-card:nth-child(1) .summary-card-icon {
            color: #3a86ff;
        }
        
        .summary-card:nth-child(2) .summary-card-icon {
            color: #ff9e1b;
        }
        
        .report-table-container {
            background-color: #ffffff;
            border-radius: 0.75rem;
            padding: 1.5rem;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        
        .report-table-header {
            margin-bottom: 1.5rem;
        }
        
        .report-table-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #333333;
        }
        
        .report-table {
            width: 100%;
            border-collapse: collapse;
        }
        
        .report-table th {
            background-color: #f5f5f5;
            padding: 1rem 0.75rem;
            text-align: left;
            font-weight: 600;
            color: #333333;
            border-bottom: 1px solid #e1e1e1;
        }
        
        .report-table td {
            padding: 1rem 0.75rem;
            border-bottom: 1px solid #e1e1e1;
        }
        
        .report-table tr:last-child td {
            border-bottom: none;
        }
        
        .report-table tr:hover td {
            background-color: #f5f5f5;
        }
        
        .date-column {
            white-space: nowrap;
        }
        
        .username-column {
            max-width: 150px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        
        .amount-column {
            font-weight: 600;
            text-align: right;
        }
        
        .empty-state {
            text-align: center;
            padding: 3rem 0;
        }
        
        .empty-state i {
            font-size: 3rem;
            color: #d1d5db;
            margin-bottom: 1rem;
        }
        
        .empty-state h3 {
            font-size: 1.5rem;
            margin-bottom: 0.5rem;
            color: #333333;
        }
        
        .empty-state p {
            color: #666666;
            margin-bottom: 0;
            max-width: 500px;
            margin-left: auto;
            margin-right: auto;
        }
        
        /* Responsive adjustments */
        @media (max-width: 768px) {
            .report-header {
                flex-direction: column;
                align-items: flex-start;
            }
            
            .date-filter-form {
                flex-direction: column;
                align-items: flex-start;
                width: 100%;
            }
            
            .form-group-inline {
                width: 100%;
            }
            
            .date-input {
                width: 100%;
            }
            
            .filter-actions {
                display: flex;
                gap: 0.5rem;
                width: 100%;
                margin-top: 0.5rem;
            }
            
            .filter-btn, .reset-btn {
                flex: 1;
            }
            
            .report-table {
                display: block;
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>

<!-- Include Admin Navigation -->
<jsp:include page="/pages/admin/adminnav.jsp">
    <jsp:param name="currentPage" value="salesreport" />
</jsp:include>

<main class="main-content">
    <jsp:include page="/pages/includes/messages.jsp" />

    <div class="admin-welcome">
        <h2>Sales Report</h2>
        <p>View and analyze your sales data with customizable date filters.</p>
    </div>

    <!-- Report Header with Date Filters -->
    <div class="report-header">
        <div class="report-title">
            <c:choose>
                <c:when test="${not empty startDate && not empty endDate}">
                    Sales from ${startDate} to ${endDate}
                </c:when>
                <c:when test="${not empty startDate}">
                    Sales from ${startDate} onwards
                </c:when>
                <c:when test="${not empty endDate}">
                    Sales until ${endDate}
                </c:when>
                <c:otherwise>
                    All Sales
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Date Filter Form -->
        <form action="${pageContext.request.contextPath}/SalesReportServlet" method="get" class="date-filter-form">
            <div class="form-group-inline">
                <label for="startDate">From:</label>
                <input type="date" id="startDate" name="startDate" class="date-input" value="${startDate}">
            </div>
            
            <div class="form-group-inline">
                <label for="endDate">To:</label>
                <input type="date" id="endDate" name="endDate" class="date-input" value="${endDate}">
            </div>
            
            <div class="filter-actions">
                <button type="submit" class="filter-btn">Apply Filter</button>
                <a href="${pageContext.request.contextPath}/SalesReportServlet" class="reset-btn">Reset</a>
            </div>
        </form>
    </div>

    <!-- Summary Cards -->
    <div class="summary-cards">
        <div class="summary-card">
            <div class="summary-card-header">
                <div>
                    <p class="summary-card-title">Total Sales</p>
                    <h3 class="summary-card-value">$<fmt:formatNumber value="${totalSales}" pattern="#,##0.00" /></h3>
                </div>
                <div class="summary-card-icon">
                    <i class="fas fa-dollar-sign"></i>
                </div>
            </div>
        </div>
        
        <div class="summary-card">
            <div class="summary-card-header">
                <div>
                    <p class="summary-card-title">Total Orders</p>
                    <h3 class="summary-card-value">${totalOrders}</h3>
                </div>
                <div class="summary-card-icon">
                    <i class="fas fa-shopping-cart"></i>
                </div>
            </div>
        </div>
    </div>

    <!-- Sales Data Table -->
    <div class="report-table-container">
        <div class="report-table-header">
            <h3 class="report-table-title">Sales Transactions</h3>
        </div>
        
        <c:choose>
            <c:when test="${empty salesData}">
                <!-- Empty state when no sales data found -->
                <div class="empty-state">
                    <i class="fas fa-chart-bar"></i>
                    <h3>No sales data found</h3>
                    <p>
                        There are no sales matching your filter criteria. Try adjusting your date filters or check back later.
                    </p>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Sales data table -->
                <div class="table-container">
                    <table class="report-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>Customer</th>
                                <th>Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="sale" items="${salesData}">
                                <tr>
                                    <td>#${sale.orderId}</td>
                                    <td class="date-column">
                                        <fmt:formatDate value="${sale.orderDate}" pattern="MMM dd, yyyy" />
                                    </td>
                                    <td class="username-column">${sale.username}</td>
                                    <td class="amount-column">$<fmt:formatNumber value="${sale.amount}" pattern="#,##0.00" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Back to Dashboard Link -->
    <div class="admin-back-link">
        <a href="${pageContext.request.contextPath}/AdminDashboardServlet">
            <i class="fas fa-arrow-left"></i> Back to Dashboard
        </a>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
<script>
    // Ensure end date is not before start date
    document.getElementById('startDate').addEventListener('change', function() {
        const startDate = this.value;
        const endDateInput = document.getElementById('endDate');
        
        if (endDateInput.value && endDateInput.value < startDate) {
            endDateInput.value = startDate;
        }
        
        endDateInput.min = startDate;
    });
    
    // Set min date for end date based on start date
    if (document.getElementById('startDate').value) {
        document.getElementById('endDate').min = document.getElementById('startDate').value;
    }
</script>