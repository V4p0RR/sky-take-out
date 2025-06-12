package com.sky.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
  @Autowired
  private OrderMapper orderMapper;

  /**
   * 生成指定时间段内的日期列表
   * 
   * @param begin
   * @param end
   * @return
   */
  private List<LocalDate> dateList(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = new ArrayList<>();
    for (int i = 0; !begin.plusDays(i).isAfter(end); i++) {
      dateList.add(begin.plusDays(i));
    }
    return dateList;
  }

  /*
   * 指定时间内营业额统计
   * 
   * @param begin 开始日期
   * 
   * @param end 结束日期
   */
  public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = dateList(begin, end);
    List<BigDecimal> turnoverList = new ArrayList<>();
    for (LocalDate date : dateList) {
      BigDecimal turnover = orderMapper.turnoverStatistics(date);
      if (turnover == null) {
        turnover = BigDecimal.ZERO;
      }
      turnoverList.add(turnover);
    }
    TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
        .dateList(StringUtils.join(dateList, ","))
        .turnoverList(null == turnoverList ? null : StringUtils.join(turnoverList, ","))
        .build();
    return turnoverReportVO;
  }

  /*
   * 用户统计
   * 
   * @param begin 开始日期
   * 
   * @param end 结束日期
   * 
   * @return
   */
  public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = dateList(begin, end);
    List<Long> userCountList = new ArrayList<>();
    List<Long> userCountBeforeDateList = new ArrayList<>();
    for (LocalDate date : dateList) {
      Long userCount = orderMapper.userStatistics(date);
      Long userCountBeforeDate = orderMapper.userStatisticsBeforeDate(date);
      if (userCount == null) {
        userCount = 0L;
      }
      if (userCountBeforeDate == null) {
        userCountBeforeDate = 0L;
      }
      userCountList.add(userCount);
      userCountBeforeDateList.add(userCountBeforeDate);
    }
    UserReportVO userReportVO = UserReportVO.builder()
        .dateList(StringUtils.join(dateList, ","))
        .newUserList(null == userCountList ? null : StringUtils.join(userCountList, ","))
        .totalUserList(null == userCountBeforeDateList ? null : StringUtils.join(userCountBeforeDateList, ","))
        .build();
    return userReportVO;
  }
}
