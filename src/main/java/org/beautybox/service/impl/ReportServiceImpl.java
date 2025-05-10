package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import org.beautybox.constraint.DateType;
import org.beautybox.entity.OrderItem;
import org.beautybox.entity.OrderProduct;
import org.beautybox.repository.*;
import org.beautybox.response.ReportTemplate;
import org.beautybox.service.ReportService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
     final WarehouseRepository warehouseRepository;
     final int ORDER = 1;
     final int REVENUE = 2;
     final int PROFIT = 3;

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
        return this.getData(fromDate, toDate, groupTime, ORDER);
    }

    @Override
    public List<ReportTemplate> getReportByTimeAndRevenue(LocalDate fromDate, LocalDate toDate, int groupTime) {
        return this.getData(fromDate, toDate, groupTime, REVENUE);
    }

    @Override
    public List<ReportTemplate> getReportByTimeAndProfit(LocalDate fromDate, LocalDate toDate, int groupTime) {
        return this.getData(fromDate, toDate, groupTime, PROFIT);
    }

    private List<ReportTemplate> getData(LocalDate fromDate, LocalDate toDate, int groupTime, int type) {
        List<ReportTemplate> reports = new ArrayList<>();
        reports.add(new ReportTemplate(fromDate.toString(), 0));
        boolean isReturn = false;
        while (fromDate.isBefore(toDate)) {
            LocalDateTime start = fromDate.atStartOfDay();
            LocalDateTime end = this.nextTo(fromDate, groupTime).atStartOfDay();
            if(end.isAfter(LocalDateTime.now())){
                end = LocalDateTime.now();
                isReturn = true;
            }
            long value = 0;
            if(type == ORDER){
                value = orderItemRepository.sumRevenueByTime(start, end);
            }
            if(type == REVENUE){
                value = orderItemRepository.sumRevenueByTime(start, end);
            }
            if(type == PROFIT){
                double avgPurchasePrice = warehouseRepository.getAvgPriceByProductDetailId("");
                double avgSalesPrice = orderItemRepository.getAvgByProductDetailId("");
                List<OrderItem> orderItems = orderItemRepository.getByTime("", start, end, Pageable.unpaged()).getContent();
                long profit = 0;
                for(OrderItem orderItem : orderItems) {
                    profit = (long) (profit  + (avgPurchasePrice -  avgSalesPrice) * orderItem.getQuantity());
                }
                value = profit;
            }
            reports.add(new ReportTemplate(end.toLocalDate().toString(), value));
            if(isReturn){
                break;
            }
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
