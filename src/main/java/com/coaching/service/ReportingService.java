package com.coaching.service;

import com.coaching.dao.ReportingDAO;

import java.util.Map;

public class ReportingService {
    private final ReportingDAO dao = new ReportingDAO();

    public Map<String, String> summary() {
        return dao.summary();
    }
}
