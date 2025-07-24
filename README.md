 # Exploring Edirne's Cultural Heritage: A Digital Guide

_A Spring Boot application showcasing Edirne's rich culture through a user-friendly interface._

[![Last Commit](https://img.shields.io/github/last-commit/OWNER/repo-name?style=flat-square)](https://github.com/bkizilkayaa/cultural-edirne/commits)
[![Language](https://img.shields.io/github/languages/top/OWNER/repo-name?style=flat-square)](https://github.com/bkizilkayaa/cultural-edirne)
[![Languages](https://img.shields.io/github/languages/count/OWNER/repo-name?style=flat-square)](https://github.com/bkizilkayaa/cultural-edirne)
[![License](https://img.shields.io/github/license/OWNER/repo-name?style=flat-square)](https://github.com/bkizilkayaa/cultural-edirne/blob/main/LICENSE)
[![Build Status](https://img.shields.io/github/workflow/status/OWNER/repo-name/CI?style=flat-square)](https://github.com/bkizilkayaa/cultural-edirne/actions)


## Overview

This project aims to create a digital platform to explore and appreciate the cultural richness of Edirne, Turkey.  It addresses the need for readily accessible information about Edirne's historical sites, traditions, and local experiences. The target audience includes tourists, history enthusiasts, and residents of Edirne seeking to learn more about their city's unique heritage.  The application facilitates exploration and discovery, promoting cultural tourism and appreciation.

## Key Features

* **Interactive Map:**  A user-friendly map displaying key cultural sites, allowing for easy navigation and discovery.
* **Detailed Information Pages:** Comprehensive descriptions, images, and historical background for each listed site.
* **QR Code Integration:**  (Assumed based on project name) QR codes can be scanned at physical locations to access relevant information instantly.
* **Multilingual Support:** (Assumed future feature)  Support for multiple languages to cater to a wider audience.
* **Backend API:** A robust RESTful API powering the front-end application (based on the provided `pom.xml`).


## Getting Started

### Prerequisites

* Java 17 or later
* Maven
* MySQL database
* A suitable IDE (e.g., IntelliJ IDEA, Eclipse)

### Installation

1. Clone the repository: `git clone https://github.com/bkizilkayaa/cultural-edirne.git`
2. Navigate to the project directory: `cd repo-name`
3. Build the project: `mvn clean install`
4. Configure your MySQL database with the credentials defined in the application properties (e.g., `application.properties`).
5. Start the application: `mvn spring-boot:run`

### Usage

Once the application is running, you can access it through your web browser at `http://localhost:8080` (or the specified port).  Explore the map, click on locations for more information.  (Further instructions will be added as the application develops).

### Testing

The project will utilize JUnit tests to ensure code quality. Run the tests using `mvn test`.


## Technologies Used

* **Spring Boot:**  A powerful framework for building stand-alone, production-grade Spring-based applications. Chosen for its ease of development, auto-configuration, and robust ecosystem.
* **Spring Data JPA:** Simplifies database interactions, providing an abstraction layer over JPA. Selected for its efficiency and ease of use with relational databases.
* **MySQL:**  A widely used open-source relational database management system, chosen for its reliability and scalability.
* **Thymeleaf:**  A modern server-side Java template engine, providing cleaner and more maintainable templating compared to JSP. (Based on `pom.xml` dependencies)
* **Java 17:** Modern Java version offering performance improvements and enhanced features.


## Contribution Guidelines

Contributions are welcome! Please open an issue or submit a pull request following the standard GitHub workflow.  All contributions should adhere to the project's coding style and standards.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## Contact Information

Email: burakkizilkaya99@gmail.com


## Future Plans and Roadmap

* Implement multilingual support.
* Add user authentication and profiles.
* Integrate user reviews and ratings.
* Expand the database with more historical details and multimedia content.
* Develop a mobile-friendly version of the application.


## Bug Reporting

Please report any bugs or issues through the GitHub issue tracker. Provide detailed information about the issue, including steps to reproduce it and any relevant error messages.

