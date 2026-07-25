package Task.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import Task.Models.Auditresponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class AuditService {

    public Auditresponse auditUrl(String targetUrl) throws Exception {
        
        // 1. Basic URL Validation
        if (targetUrl == null || targetUrl.isBlank()) {
            throw new IllegalArgumentException("URL cannot be empty.");
        }
        
        // Ensure the URL has a protocol, otherwise the HttpClient will fail
        if (!targetUrl.startsWith("http://") && !targetUrl.startsWith("https://")) {
            targetUrl = "https://" + targetUrl;
        }

        // 2. Configure the HTTP Client with a strict timeout
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .GET()
                .build();

        // 3. Execute Request and Calculate Response Time
        long startTime = System.currentTimeMillis();
        
        // This will throw exceptions for timeouts or unresolvable hosts
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        long endTime = System.currentTimeMillis();
        long responseTimeMs = endTime - startTime;

        // 4. Handle Non-HTML Responses gracefully
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        if (!contentType.toLowerCase().contains("text/html")) {
            throw new UnsupportedOperationException("Unsupported media type. The tool only audits HTML pages. Received: " + contentType);
        }

        // 5. Parse the HTML body using JSoup
        Document doc = Jsoup.parse(response.body());

        // 6. Extract the required metrics
        int httpStatus = response.statusCode();
        String pageTitle = doc.title();
        
        // Safely extract meta description if it exists
        Element metaDescElement = doc.selectFirst("meta[name=description]");
        String metaDescription = (metaDescElement != null) ? metaDescElement.attr("content") : null;

        int h1Count = doc.select("h1").size();

        // Count images missing the 'alt' attribute or having an empty one
        int imagesMissingAlt = 0;
        Elements images = doc.select("img");
        for (Element img : images) {
            if (!img.hasAttr("alt") || img.attr("alt").trim().isEmpty()) {
                imagesMissingAlt++;
            }
        }

        // Extract visible text (JSoup automatically ignores <script> and <style> tags)
        String bodyText = doc.body() != null ? doc.body().text() : "";
        int wordCount = bodyText.isBlank() ? 0 : bodyText.split("\\s+").length;

        // 7. Construct and return the final report
        Auditresponse auditResponse = new Auditresponse();
        auditResponse.setHttpStatus(httpStatus);
        auditResponse.setResponseTime(responseTimeMs);
        auditResponse.setPageTitle(pageTitle);
        auditResponse.setMetaDescription(metaDescription);
        auditResponse.setH1Count(h1Count);
        auditResponse.setImagesMissingAltText(imagesMissingAlt);
        auditResponse.setWordCount(wordCount);

        return auditResponse;
    }
}