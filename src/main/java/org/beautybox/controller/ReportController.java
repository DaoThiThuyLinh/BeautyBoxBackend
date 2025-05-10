package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin-api")
@RequiredArgsConstructor
public class ReportController {

    final ReportService reportService;

    @GetMapping("/summary")
    public ApiResponse getSummary(){
        return ApiResponse.success("Tổng quan hệ thống", reportService.getSummary());
    }

    @Operation(summary = "Thông kê đặt hàng theo thời gian", parameters = {
            @Parameter(name = "groupTime", description = "1. Bước nhảy 1 ngày\n" +
                    "2. Bước nhảy 1 tuần\n" +
                    "3. Bước nhảy 1 tháng\n" +
                    "4. Bước nhảy 5 tháng\n" +
                    "5. Bước nhảy 1 năm")
    })
    @GetMapping("/order-by-time")
    public ApiResponse getOrderByTime(@RequestParam LocalDate fromDate,
                                      @RequestParam LocalDate toDate,
                                      @RequestParam(required = false, defaultValue = "1") int groupTime){
        return ApiResponse.success("Thống kê danh sách đặt hàng theo thời gian", reportService.getReportByTimeAndOrder(fromDate, toDate, groupTime));
    }

    @GetMapping("/revenue-by-time")
    public ApiResponse getRevenueByTime(@RequestParam LocalDate fromDate,
                                        @RequestParam LocalDate toDate,
                                        @RequestParam(required = false, defaultValue = "1") int groupTime){
        return ApiResponse.success("Thống kê doanh thu theo thời gian", reportService.getReportByTimeAndRevenue(fromDate, toDate, groupTime));
    }

    @GetMapping("/profit-by-time")
    public ApiResponse getProfit(@RequestParam LocalDate fromDate,
                         @RequestParam LocalDate toDate,
                         @RequestParam(required = false, defaultValue = "1") int groupTime){

        return ApiResponse.success("Thống kê lợi nhuận", reportService.getReportByTimeAndProfit(fromDate, toDate, groupTime));
    }
}
