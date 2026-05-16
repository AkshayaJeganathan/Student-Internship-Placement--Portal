# PlaceSync - Student Internship & Placement Portal

PlaceSync is a full-stack web application built to streamline the campus placement and internship process. It provides a seamless interface for students to track eligibility, apply for active job drives, and manage their profiles, while allowing companies and admins to oversee application statuses and recruitment metrics.

---

## 🚀 Key Features

* **Student Dashboard:** View matching job drives, check individual eligibility constraints based on CGPA/backlogs, upload resumes safely, and track active application pipelines in real-time.
* **Company Panel:** Post active job drives (with explicit CTC, type, and eligibility criteria) and track inbound applicant streams.
* **Administrative Controls:** Oversee the entire ecosystem, evaluate student data, manage application statuses (Applied, Shortlisted, Selected, Rejected), and input round-by-round remarks.
* **Automated Validation & Constraints:** Backend constraint handling guarding against invalid profile parameters and duplicate applications.
* **Secure Infrastructure:** Configured password protection, robust exception handling, and dynamic database schema generation via Hibernate.

---

## 🛠️ Technology Stack

* **Backend:** Java 17, Spring Boot 4.0.x, Spring Data JPA, Spring Security
* **Frontend:** HTML5, Tailwind CSS, JavaScript (Fetch API)
* **Database:** MySQL 8.0
* **Build & Dependency Management:** Maven
* **Utilities:** Project Lombok

---

## 📂 Project Structure

```text
StudentPlacementPortal/
├── src/
│   ├── main/
│   │   ├── java/com/placement/portal/
│   │   │   ├── config/          # SecurityConfig, GlobalExceptionHandler
│   │   │   ├── controller/      # Student, Company, and Admin REST APIs
│   │   │   ├── dto/             # ApiResponse, Dashboard/Eligibility DTOs
│   │   │   ├── entity/          # Student, Company, JobDrive, Application models
│   │   │   ├── repository/      # JPA Database Access Layer Interfaces
│   │   │   └── service/         # Business & Eligibility Logic Layers
│   │   └── resources/
│   │       ├── static/          # UI pages (login.html, student-dashboard.html...)
│   │       └── application.properties   # Environment configurations
├── uploads/                     # Local storage directory for student resumes
└── pom.xml                      # Maven dependencies
