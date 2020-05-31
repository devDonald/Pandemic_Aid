package com.gnorizon.solutions.pandemic_aid.helpers;

public class ResponseModel {
    private String name, report, message;

    public ResponseModel() {
    }

    public ResponseModel(String name, String report, String message) {
        this.name = name;
        this.report = report;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
