package com.sky.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
  @Autowired
  private ReportService reportService;

  /**
   * 指定时间内营业额统计
   * 
   * @param begin 开始日期
   * @param end   结束日期
   * @return 营业额统计结果
   */
  @GetMapping("/turnoverStatistics")
  public Result<TurnoverReportVO> turnoverStatistics( // 指定格式
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    log.info("营业额统计:{}至{}", begin, end);
    TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(begin, end);
    return Result.success(turnoverReportVO);
  }

  /**
   * 用户统计
   * 
   * @param begin
   * @param end
   * @return
   */
  @GetMapping("/userStatistics")
  public Result<UserReportVO> userStatistics(
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    log.info("用户统计:{}至{}", begin, end);
    UserReportVO userReportVO = reportService.userStatistics(begin, end);
    return Result.success(userReportVO);
  }

  /**
   * 订单统计
   * 
   * @param begin
   * @param end
   * @return
   */
  @GetMapping("/ordersStatistics")
  public Result<OrderReportVO> ordersStatistics(
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    log.info("订单统计:{}至{}", begin, end);
    OrderReportVO orderReportVO = reportService.ordersStatistics(begin, end);
    return Result.success(orderReportVO);
  }

  /**
   * 销量排名
   * 
   * @param begin
   * @param end
   * @return
   */
  @GetMapping("/top10")
  public Result<SalesTop10ReportVO> top10(
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    log.info("查询销量排名:{}至{}", begin, end);
    SalesTop10ReportVO salesTop10ReportVO = reportService.top10(begin, end);
    return Result.success(salesTop10ReportVO);
  }

}
