package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.constraint.DateType;
import org.beautybox.repository.*;
import org.beautybox.response.ReportTemplate;
import org.beautybox.service.ReportService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

     final OrderRepository orderRepository;
     final UserRepository userRepository;
     final ProductRepository productRepository;
     final OrderItemRepository orderItemRepository;
     final CategoryRepository categoryRepository;
     final BrandRepository brandRepository;

    @Override
    public Map<String, Object> getSummary() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalUser", userRepository.count());
        response.put("totalOrder", orderRepository.count());
        response.put("totalProduct", productRepository.count());
        response.put("totalBrand", brandRepository.count());
        response.put("category", categoryRepository.count());
        response.put("totalRevenue", orderItemRepository.sumRevenue());
        return response;
    }

    @Override
    public List<ReportTemplate> getReportByTimeAndOrder(LocalDate fromDate, LocalDate toDate, int groupTime) {
        List<ReportTemplate> reports = new ArrayList<>();
        while (fromDate.isBefore(this.nextTo(toDate, groupTime))) {
            LocalDateTime start= fromDate.atStartOfDay();
            if(start.isAfter(LocalDateTime.now())) {
                start = LocalDateTime.now();
            }
            LocalDateTime end= this.nextTo(fromDate, groupTime).atStartOfDay();
            if(end.isAfter(LocalDateTime.now()))
                end= LocalDateTime.now();
            int value= orderRepository.countByTime(start, end);
            reports.add(new ReportTemplate(start.format(DateTimeFormatter.ISO_DATE), value));
            fromDate = nextTo(fromDate, groupTime);
        }
        return reports;
    }

    private LocalDate nextTo(LocalDate from, int groupType){
        if(groupType == (DateType.DAY))
            return from.plusDays(1);
        else if(groupType == (DateType.WEEK)){
            return from.plusWeeks(1);
        }else if(groupType == (DateType.MONTH)){
            return from.plusMonths(1);
        }else if(groupType == (DateType.FIVE_MONTH)){
            return from.plusMonths(5);
        } else if(groupType == DateType.YEAR){
            return from.plusYears(1);
        }else{
            throw new RuntimeException("Bước nhảy không hợp lệ");
        }
    }
}
