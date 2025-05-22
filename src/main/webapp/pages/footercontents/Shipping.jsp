<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipping Information - FineFish</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            max-width: 800px;
            margin: 0 auto;
            color: #333;
        }
        h1 {
            text-align: center;
            color: #0066cc;
            margin-bottom: 20px;
        }
        h2 {
            color: #0066cc;
            border-bottom: 1px solid #eee;
            padding-bottom: 5px;
            margin-top: 30px;
        }
        p {
            margin-bottom: 15px;
        }
        .section {
            margin-bottom: 25px;
        }
        ul {
            padding-left: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .note {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 5px;
            border-left: 4px solid #0066cc;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <h1>Shipping Information</h1>

    <div class="section">
        <h2>Delivery Areas</h2>
        <p>We currently deliver to the following areas in Nepal:</p>
        <ul>
            <li>Kathmandu Valley (including Kathmandu, Lalitpur, and Bhaktapur)</li>
            <li>Pokhara</li>
            <li>Chitwan</li>
            <li>Butwal</li>
        </ul>
        <p>We're constantly expanding our delivery network. If your area is not listed, please check back soon!</p>
    </div>

    <div class="section">
        <h2>Delivery Schedule</h2>
        <p>We offer delivery services Monday through Saturday, from 7:00 AM to 6:00 PM.</p>
        <p>Orders placed before 3:00 PM are eligible for same-day dispatch (where available).</p>
        <p>During checkout, you can select your preferred delivery time slot.</p>
        
        <div class="note">
            <strong>Note:</strong> Delivery times may vary during holidays, extreme weather conditions, or other unforeseen circumstances. We will notify you in case of any delays.
        </div>
    </div>

    <div class="section">
        <h2>Delivery Charges</h2>
        <p>Delivery charges vary based on your location:</p>
        <table>
            <thead>
                <tr>
                    <th>Location</th>
                    <th>Delivery Fee</th>
                    <th>Free Delivery Threshold</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Kathmandu (Central Areas)</td>
                    <td>NPR 50</td>
                    <td>Orders above NPR 1,500</td>
                </tr>
                <tr>
                    <td>Kathmandu (Outer Areas)</td>
                    <td>NPR 80</td>
                    <td>Orders above NPR 2,000</td>
                </tr>
                <tr>
                    <td>Lalitpur & Bhaktapur</td>
                    <td>NPR 100</td>
                    <td>Orders above NPR 2,500</td>
                </tr>
                <tr>
                    <td>Pokhara</td>
                    <td>NPR 120</td>
                    <td>Orders above NPR 3,000</td>
                </tr>
                <tr>
                    <td>Chitwan</td>
                    <td>NPR 80</td>
                    <td>Orders above NPR 2,000</td>
                </tr>
                <tr>
                    <td>Butwal</td>
                    <td>NPR 150</td>
                    <td>Orders above NPR 3,500</td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="section">
        <h2>Packaging</h2>
        <p>We take great care in packaging our seafood products to ensure they reach you in optimal condition:</p>
        <ul>
            <li>All seafood is packed in insulated boxes with ice packs to maintain temperature</li>
            <li>Products are vacuum-sealed for freshness</li>
            <li>Our packaging is designed to maintain cold-chain integrity for up to 4 hours after dispatch</li>
            <li>We use eco-friendly packaging materials wherever possible</li>
        </ul>
    </div>

    <div class="section">
        <h2>Order Tracking</h2>
        <p>Once your order is dispatched, you'll receive a confirmation email with tracking information.</p>
        <p>You can also track your order by logging into your account and viewing your order history.</p>
        <p>For any delivery-related queries, please contact our customer support team at support@finefish.com or +977-9801234567.</p>
    </div>
</body>
</html>