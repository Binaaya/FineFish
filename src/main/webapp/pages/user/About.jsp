<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>

<!-- base css -->
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/about.css"
/>

<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
  <jsp:param name="title" value="About Us" />
  <jsp:param name="css" value="about" />
  <jsp:param name="active" value="about" />
</jsp:include>

<!-- Page Header -->
<header class="page-header">
  <div class="container">
    <h1 class="page-title">About FineFish</h1>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <span>/</span>
      <span>About Us</span>
    </div>
  </div>
</header>

<!-- Main Content -->
<main>
  <jsp:include page="/pages/includes/messages.jsp" />
  <!-- Our Story Section -->
  <section class="story-section">
    <div class="container">
      <div class="story-content">
        <div class="story-text">
          <h2 class="section-title">Our Story</h2>
          <p class="section-subtitle">From Ocean to Table with Care</p>
          <p>
            FineFish was founded in 2015 by brothers Mark and David Fischer, who
            grew up in a small fishing village on the coast of Maine. Coming
            from a long line of fishermen, they witnessed firsthand the
            challenges of getting fresh seafood to inland communities while
            ensuring fair compensation for those who caught it.
          </p>
          <p>
            What began as a small operation delivering to local restaurants has
            grown into a nationwide service, but our mission remains the same:
            to connect consumers directly with the source of their seafood,
            cutting out middlemen and ensuring only the freshest catch reaches
            your table.
          </p>
          <p>
            Today, we work with over 50 family-owned fishing operations across
            the country, all committed to sustainable practices that protect our
            oceans for future generations. Every piece of seafood we deliver
            comes with our guarantee of freshness and our promise of responsible
            sourcing.
          </p>
        </div>
        <div class="story-image">
          <img
            src="${pageContext.request.contextPath}/uploads/sadie.jpg"
            alt="Fishermen at work"
          />
        </div>
      </div>
    </div>
  </section>

  <!-- Our Values Section -->
  <section class="values-section" id="sustainability">
    <div class="container">
      <h2 class="section-title text-center">Our Values</h2>
      <p class="section-subtitle text-center">What Drives Us Every Day</p>

      <div class="values-grid">
        <div class="value-card">
          <div class="value-icon">
            <i class="fas fa-fish"></i>
          </div>
          <h3>Freshness Guaranteed</h3>
          <p>
            We deliver seafood within 24-48 hours of catch, guaranteeing peak
            freshness. If you're not 100% satisfied, we'll refund your purchase.
          </p>
        </div>

        <div class="value-card">
          <div class="value-icon">
            <i class="fas fa-leaf"></i>
          </div>
          <h3>Sustainable Sourcing</h3>
          <p>
            We partner only with fishermen who use sustainable fishing methods
            that protect marine ecosystems and prevent overfishing.
          </p>
        </div>

        <div class="value-card">
          <div class="value-icon">
            <i class="fas fa-handshake"></i>
          </div>
          <h3>Fair Trade Practices</h3>
          <p>
            We pay our fishermen partners fair prices, ensuring they can
            continue their traditional way of life while providing for their
            families.
          </p>
        </div>

        <div class="value-card">
          <div class="value-icon">
            <i class="fas fa-recycle"></i>
          </div>
          <h3>Eco-Friendly Packaging</h3>
          <p>
            Our packaging is made from recycled materials and is either
            recyclable or biodegradable, minimizing our environmental footprint.
          </p>
        </div>
      </div>
    </div>
  </section>

  <!-- Team Section -->
  <section class="team-section" id="partners">
    <div class="container">
      <h2 class="section-title text-center">Meet Our Team</h2>
      <p class="section-subtitle text-center">The People Behind FineFish</p>

      <div class="team-grid">
        <div class="team-member">
          <div class="member-image">
            <img
              src="${pageContext.request.contextPath}/uploads/sadikshya.jpg"
              alt="Sadikshya"
            />
          </div>
          <h3>Sadikshya Budhathoki</h3>
          <p class="member-title">Co-Founder and CEO</p>
          <p class="member-bio">
            With 20+ years of experience in the fishing industry, Mark manages
            our partnerships with fishing operations across the country.
          </p>
        </div>

        <div class="team-member">
          <div class="member-image">
            <img
              src="${pageContext.request.contextPath}/uploads/tilu.jpg"
              alt="David Fischer"
            />
          </div>
          <h3>Tilasmi Subedi</h3>
          <p class="member-title">Co-Founder and COO</p>
          <p class="member-bio">
            David oversees our logistics and delivery operations, ensuring your
            seafood arrives fresh and on time.
          </p>
        </div>

        <div class="team-member">
          <div class="member-image">
            <img
              src="https://randomuser.me/api/portraits/women/44.jpg"
              alt="Sarah Chen"
            />
          </div>
          <h3>Sarah Chen</h3>
          <p class="member-title">Head of Sustainability</p>
          <p class="member-bio">
            As a marine biologist, Sarah ensures all our sourcing practices meet
            the highest standards of sustainability.
          </p>
        </div>

        <div class="team-member">
          <div class="member-image">
            <img
              src="https://randomuser.me/api/portraits/men/55.jpg"
              alt="Robert Martinez"
            />
          </div>
          <h3>Robert Martinez</h3>
          <p class="member-title">Executive Chef</p>
          <p class="member-bio">
            Chef Robert creates our recipes and guides, helping customers make
            the most of their premium seafood deliveries.
          </p>
        </div>

        <div class="team-member">
          <div class="member-image">
            <img
              src="${pageContext.request.contextPath}/uploads/binu.jpg"
              alt="Jennifer Lopez"
            />
          </div>
          <h3>Binaya Bastola</h3>
          <p class="member-title">Customer Experience Manager</p>
          <p class="member-bio">
            Jennifer ensures that every customer has an exceptional experience,
            from ordering to delivery, and manages our customer support team.
          </p>
        </div>
      </div>
    </div>
  </section>
</main>

<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp">
  <jsp:param name="js" value="about" />
</jsp:include>
