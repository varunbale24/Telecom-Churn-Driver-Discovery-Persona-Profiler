# TeleChurn AI

TeleChurn AI is a single Java Spring Boot application for telecom churn driver discovery, customer persona profiling, churn prediction, and retention reporting.

This is a single Java Spring Boot application. The frontend uses Thymeleaf. The backend uses Spring Boot. Machine learning uses Weka. PostgreSQL is used for persistence. No Python or separate frontend application is required.

## Project Description

TeleChurn AI helps telecom teams identify why customers cancel subscriptions, segment subscribers into behavioral personas, and prioritize retention actions.

## Problem Statement

Telecom providers need to know why customers are canceling their subscriptions so they can fix underlying service issues.

## Objectives

- Discover churn drivers with an interpretable decision tree
- Profile subscribers with K-Means clustering
- Predict churn risk for an individual customer
- Support business reporting and retention planning

## Features

- Thymeleaf-based UI
- Spring Security login and registration
- Customer explorer and detail pages
- Churn analytics dashboard
- Driver insights and decision-tree rules
- Persona cards and cluster summaries
- Churn prediction workflow
- CSV report export

## Architecture

Controller -> Service -> Repository -> PostgreSQL

ML flow:
Dataset -> Java preprocessing -> Weka J48 / SimpleKMeans -> predictions and personas

## Technology Stack

- Java 21
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Spring Security
- PostgreSQL
- Weka
- Thymeleaf
- Bootstrap 5
- Chart.js
- JUnit 5
- Mockito

## Database Setup

This workspace is configured to boot with embedded H2 by default so the website starts without a local database server.

If you want to use PostgreSQL in your own environment, create a PostgreSQL database named `telechurn_db` and restore the PostgreSQL datasource settings.

Set environment variables if needed for PostgreSQL mode:

- `DB_USERNAME`
- `DB_PASSWORD`

## How to Run

```bash
cd telechurn-ai
mvn clean install
mvn spring-boot:run
```

Then open:

```text
http://localhost:5050
```

## Default Login

Development account:

- Email: `admin@telechurn.com`
- Password: `Admin@123`

Change this password before deployment.

## ML Approach

- Decision tree: Weka `J48`
- Clustering: Weka `SimpleKMeans`
- Customer features are preprocessed in Java before training

## Screenshots

Add dashboard and analytics screenshots here.

## Controller Overview

- `/login`, `/register`
- `/dashboard`
- `/customers`
- `/churn-analysis`
- `/churn-drivers`
- `/personas`
- `/prediction`
- `/model-performance`
- `/reports`
- `/profile`

## Testing

Run:

```bash
mvn test
```

## Future Enhancements

- Full dataset import pipeline for the entire Telco Customer Churn dataset
- More detailed retention action engine
- PDF report export
- Editable customer records in the UI
