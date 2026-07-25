package Task.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Task.Models.Auditrequest;
import Task.Models.Auditresponse;
import Task.Service.AuditService;

import java.net.UnknownHostException;
import java.net.http.HttpTimeoutException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allows the frontend to call this API without CORS errors
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping("/audit")
    public ResponseEntity<?> auditWebsite(@RequestBody Auditrequest request) {
        try {
            // Call the service layer to perform the audit
            Auditresponse response = auditService.auditUrl(request.getUrl());
            
            // Return the successful JSON report with a 200 OK status
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Handles empty URLs or malformed URI syntax
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid URL provided: " + e.getMessage());

        } catch (UnknownHostException e) {
            // Handles URLs that do not exist or cannot be resolved via DNS
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Could not resolve host. Please check if the URL is correct.");

        } catch (HttpTimeoutException e) {
            // Handles websites that take too long to respond
            return buildErrorResponse(HttpStatus.GATEWAY_TIMEOUT, "The request timed out. The website might be down or too slow.");

        } catch (UnsupportedOperationException e) {
            // Handles non-HTML content (like PDFs or Images) detected in the Service layer
            return buildErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());

        } catch (Exception e) {
            // Catch-all for any other unexpected errors to prevent a server crash
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during the audit: " + e.getMessage());
        }
    }

    /**
     * Helper method to standardize error JSON responses.
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", message);
        errorBody.put("status", String.valueOf(status.value()));
        return ResponseEntity.status(status).body(errorBody);
    }
}