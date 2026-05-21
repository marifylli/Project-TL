package com.unipath.model;
import java.time.LocalDateTime;
import java.util.Map;

// official report generater for statistical analysis secretary

public class Report {
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private FilterCriteria appliedFilters;    // applied filtera
    private Map<String, Object> statistics;   // apotelesmata analysis
    private byte[] documentContent;           // arxeio eksagogis os bytes
    private String format;                    // "PDF" | "DOCX"
    private String generatedBy;              // username grammateias


    public Report() {
        this.createdAt = LocalDateTime.now();
    }

    public Report(String title, FilterCriteria appliedFilters,
                  Map<String, Object> statistics, String format,
                  String generatedBy) {
        this.title          = title;
        this.appliedFilters = appliedFilters;
        this.statistics     = statistics;
        this.format         = format;
        this.generatedBy    = generatedBy;
        this.createdAt      = LocalDateTime.now();
    }


    public int getId()
    { return id; }
    public void setId(int id)
    { this.id = id; }

    public String getTitle()
    { return title; }
    public void setTitle(String title)
    { this.title = title; }

    public LocalDateTime getCreatedAt()
    { return createdAt; }
    public void setCreatedAt(LocalDateTime dt)
    { this.createdAt = dt; }

    public FilterCriteria getAppliedFilters()
    { return appliedFilters; }
    public void setAppliedFilters(FilterCriteria f)
    { this.appliedFilters = f; }

    public Map<String, Object> getStatistics()
    { return statistics; }
    public void setStatistics(Map<String, Object> s)
    { this.statistics = s; }

    public byte[] getDocumentContent()
    { return documentContent; }
    public void setDocumentContent(byte[] content)
    { this.documentContent = content; }

    public String getFormat()
    { return format; }
    public void setFormat(String format)
    { this.format = format; }

    public String getGeneratedBy()
    { return generatedBy; }
    public void setGeneratedBy(String user)
    { this.generatedBy = user; }

    @Override
    public String toString() {
        return "Report{id=" + id + ", title='" + title
                + "', format=" + format
                + ", createdAt=" + createdAt
                + ", by=" + generatedBy + "}";
    }

}
