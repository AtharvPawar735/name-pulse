# Page Pulse - Web Auditor
A lightweight full-stack web tool that audits any URL and returns key SEO and performance metrics. 
**Built for the Digital Heroes Internship Qualification Task.**
## 🚀 Live Demo
https://name-pulse.onrender.com
---
## 🛠️ Setup Instructions
### Prerequisites
* Java 21
* Maven 3.8+
### Running Locally
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/](https://github.com/)[YOUR_USERNAME]/page-pulse.git
   cd page-pulse
Build and run the Spring Boot application:
Bash
./mvnw spring-boot:run
(Alternatively, run mvn clean install and execute the generated JAR in the /target folder).

Access the Application:
Open your browser and navigate to http://localhost:8080. The vanilla HTML/JS frontend is served directly from the Spring Boot static directory.

📡 API Contract
POST /api/audit
Audits a given URL and returns a JSON report containing page metrics.
Request Body
JSON
{
  "url": "[https://example.com](https://example.com)"
}
Success Response (200 OK)
JSON
{
  "httpStatus": 200,
  "responseTimeMs": 145,
  "pageTitle": "Example Domain",
  "metaDescription": null,
  "h1Count": 1,
  "imagesMissingAltText": 0,
  "wordCount": 23
}
Error Responses
The API is designed to never crash. Expected failures return clean JSON error messages mapped to sensible HTTP status codes.

400 Bad Request (Malformed URLs or Unresolvable Hosts)
JSON
{
  "error": "Could not resolve host. Please check if the URL is correct.",
  "status": "400"
}

415 Unsupported Media Type (Non-HTML responses like PDFs or Images)
JSON
{
  "error": "Unsupported media type. The tool only audits HTML pages.",
  "status": "415"
}

504 Gateway Timeout (Website took too long to respond)
JSON
{
  "error": "The request timed out. The website might be down or too slow.",
  "status": "504"
}

🧠 Design Decisions & Reasoning
1. Java Spring Boot for Backend & Static File Serving
I chose Spring Boot for the backend because it allows for rapid development of robust REST APIs. Instead of splitting the frontend and backend into two separate deployed services, I placed the vanilla HTML/CSS/JS frontend inside Spring's src/main/resources/static folder.

Reasoning: This monolithic approach eliminates CORS configuration headaches, simplifies local testing, and allows the entire full-stack application to be deployed effortlessly as a single Dockerized container on Render's free tier.

2. JSoup for HTML Parsing
I utilized the JSoup library for fetching and parsing the DOM.

Reasoning: JSoup is exceptionally forgiving with malformed HTML, which is crucial when scraping unpredictable websites. It provides intuitive, jQuery-like CSS selectors making it easy to identify <meta> tags and missing alt attributes. Furthermore, its .body().text() method automatically strips out <script> and <style> tags, allowing for a highly accurate approximation of the visible word count without writing complex RegEx.

3. Controller-Level Exception Handling
To satisfy the strict requirement of "sensible errors, never a crash," I implemented dedicated error handling within the Controller layer using try-catch blocks.

Reasoning: Rather than allowing the native Java HttpClient to throw raw stack traces (like HttpTimeoutException or UnknownHostException) directly to the client, the Controller intercepts these specific exceptions and maps them to appropriate HTTP status codes (504, 400). This ensures the frontend always receives a predictable JSON payload and can gracefully display a user-friendly error message on the UI.
