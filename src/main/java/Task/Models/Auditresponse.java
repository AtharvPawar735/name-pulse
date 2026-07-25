package Task.Models;

public class Auditresponse {
    private int httpStatus;
    private double responseTime;
    private String pageTitle;
    private String metaDescription;
    private int h1Count;
    private int imagesMissingAltText;
    private int wordCount;
    
    public int getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
    public double getResponseTime() {
        return responseTime;
    }
    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }
    public String getPageTitle() {
        return pageTitle;
    }
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public String getMetaDescription() {
        return metaDescription;
    }
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }
    public int getH1Count() {
        return h1Count;
    }
    public void setH1Count(int h1Count) {
        this.h1Count = h1Count;
    }
    public int getImagesMissingAltText() {
        return imagesMissingAltText;
    }
    public void setImagesMissingAltText(int imagesMissingAltText) {
        this.imagesMissingAltText = imagesMissingAltText;
    }
    public int getWordCount() {
        return wordCount;
    }
    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

}
