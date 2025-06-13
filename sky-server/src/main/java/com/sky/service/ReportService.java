package com.sky.service;

import java.time.LocalDate;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

public interface ReportService {

  /**
   * 指定时间内营业额统计
   * 
   * @param begin 开始日期
   * @param end   结束日期
   * @return 营业额统计结果
   */
  TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

  /**
   * 用户统计
   * 
   * @param begin
   * @param end
   * @return
   */
  UserReportVO userStatistics(LocalDate begin, LocalDate end);

  /**
   * 订单统计
   * 
   * @param begin
   * @param end
   * @return
   */
  OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

  /**
   * 销售额前十统计
   * 
   * @param begin
   * @param end
   * @return
   */
  SalesTop10ReportVO top10(LocalDate begin, LocalDate end);

}
