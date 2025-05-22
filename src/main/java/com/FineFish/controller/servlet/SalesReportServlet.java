package com.FineFish.controller.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.FineFish.controller.dao.SalesDAO;
import com.FineFish.model.Sale;
import com.FineFish.model.User;

/**
 * Servlet implementation class SalesReportServlet
 * Handles retrieving and displaying sales report data
 */
@WebServlet("/SalesReportServlet")
public class SalesReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public SalesReportServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"admin".equals(user.getRole())) {
            // Not an admin, redirect to login
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/pages/user/Login.jsp");
            } else {
                session.setAttribute("errorMessage", "Access denied. Admin privileges required.");
                response.sendRedirect(request.getContextPath() + "/pages/user/Dashboard.jsp");
            }
            return;
        }
        
        try {
            // Get date filter parameters if provided
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            Date startDate = null;
            Date endDate = null;
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            // Parse dates if provided
            if (startDateStr != null && !startDateStr.isEmpty()) {
                try {
                    startDate = sdf.parse(startDateStr);
                } catch (ParseException e) {
                    request.setAttribute("errorMessage", "Invalid start date format. Please use YYYY-MM-DD");
                }
            }
            
            if (endDateStr != null && !endDateStr.isEmpty()) {
                try {
                    endDate = sdf.parse(endDateStr);
                } catch (ParseException e) {
                    request.setAttribute("errorMessage", "Invalid end date format. Please use YYYY-MM-DD");
                }
            }
            
            // Create SalesDAO
            SalesDAO salesDAO = new SalesDAO();
            
            // Get sales data based on date filters
            List<Sale> salesData = salesDAO.getSalesReport(startDate, endDate);
            
            // Calculate total sales
            double totalSales = 0;
            int totalOrders = 0;
            
            if (salesData != null && !salesData.isEmpty()) {
                totalOrders = salesData.size();
                for (Sale sale : salesData) {
                    totalSales += sale.getAmount();
                }
            }
            
            // Set attributes for the JSP page
            request.setAttribute("salesData", salesData);
            request.setAttribute("totalSales", totalSales);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            
            // Forward to the sales report page
            request.getRequestDispatcher("/pages/admin/SalesReport.jsp").forward(request, response);
            
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving sales data: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("errorMessage", "A system error occurred. Please try again later.");
            request.getRequestDispatcher("/pages/admin/SalesReport.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Post requests handled same as get
        doGet(request, response);
    }
}