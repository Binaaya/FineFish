<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - FineFish</title>
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
        .contact-details {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
        }
        .contact-form {
            margin-top: 30px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input, textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        textarea {
            height: 150px;
        }
        button {
            background-color: #0066cc;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0055aa;
        }
    </style>
</head>
<body>
    <h1>Contact Us</h1>

    <div class="section">
        <h2>Get in Touch</h2>
        <p>Have questions, feedback, or need assistance with your order? We're here to help! Our customer service team is ready to assist you with any inquiries you may have.</p>
        
        <div class="contact-details">
            <h2>Contact Information</h2>
            <p><strong>Customer Service:</strong> +977-9801234567<br>
            <strong>Email:</strong> support@finefish.com<br>
            <strong>Operating Hours:</strong> Monday to Saturday, 7:00 AM - 6:00 PM</p>
            
            <h2>Office Address</h2>
            <p>FineFish Pvt. Ltd.<br>
            Fishery Complex, Ward No. 10<br>
            Bharatpur, Chitwan, Nepal</p>
        </div>
    </div>

    <div class="contact-form">
        <h2>Send Us a Message</h2>
        <form action="${pageContext.request.contextPath}/contact" method="post">
            <div class="form-group">
                <label for="name">Your Name</label>
                <input type="text" id="name" name="name" required>
            </div>
            
            <div class="form-group">
                <label for="email">Your Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="phone">Phone Number</label>
                <input type="tel" id="phone" name="phone">
            </div>
            
            <div class="form-group">
                <label for="subject">Subject</label>
                <input type="text" id="subject" name="subject" required>
            </div>
            
            <div class="form-group">
                <label for="message">Your Message</label>
                <textarea id="message" name="message" required></textarea>
            </div>
            
            <button type="submit">Send Message</button>
        </form>
    </div>
</body>
</html>