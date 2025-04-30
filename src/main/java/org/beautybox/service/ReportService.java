package org.beautybox.service;

import org.beautybox.response.ReportTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface ReportService {
    Map<String, Object> getSummary();
    List<ReportTemplate> getReportByTimeAndOrder(LocalDate fromDate, LocalDate toDate, int groupTime);
}
