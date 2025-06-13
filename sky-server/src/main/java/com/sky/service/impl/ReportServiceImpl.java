package com.sky.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
  @Autowired
  private OrderMapper orderMapper;
  @Autowired
  private OrderDetailMapper orderDetailMapper;
  @Autowired
  private WorkspaceService workspaceService;

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

  /*
   * 订单统计
   * 
   * @param begin 开始日期
   * 
   * @param end 结束日期
   * 
   * @return
   */
  public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
    List<LocalDate> dateList = dateList(begin, end);
    List<Long> orderCountList = new ArrayList<>();
    List<Long> validOrderCountList = new ArrayList<>();
    Long totalOrderCount;
    Long validOrderCount;
    Double orderComplectionRate;
    totalOrderCount = orderMapper.getTotalOrderCount(null, null) == null ? 0L
        : orderMapper.getTotalOrderCount(null, null);
    validOrderCount = orderMapper.getTotalOrderCount(Orders.COMPLETED, null) == null ? 0L
        : orderMapper.getTotalOrderCount(Orders.COMPLETED, null);
    orderComplectionRate = totalOrderCount == 0 ? 0.0
        : (double) validOrderCount / totalOrderCount;
    for (LocalDate date : dateList) {
      Long orderCount = orderMapper.getTotalOrderCount(null, date) == null ? 0L
          : orderMapper.getTotalOrderCount(null, date);
      Long validOrderCountUnit = orderMapper.getTotalOrderCount(Orders.COMPLETED, date) == null ? 0L
          : orderMapper.getTotalOrderCount(Orders.COMPLETED, date);
      orderCountList.add(orderCount);
      validOrderCountList.add(validOrderCountUnit);
    }
    OrderReportVO orderReportVO = OrderReportVO.builder()
        .dateList(null == dateList ? null : StringUtils.join(dateList, ","))
        .orderCompletionRate(orderComplectionRate)
        .orderCountList(null == orderCountList ? null : StringUtils.join(orderCountList, ","))
        .totalOrderCount(Math.toIntExact(totalOrderCount))
        .validOrderCount(Math.toIntExact(validOrderCount))
        .validOrderCountList(null == validOrderCountList ? null : StringUtils.join(validOrderCountList, ","))
        .build();
    return orderReportVO;
  }

  /*
   * 销售额前十统计
   * 
   * @param begin 开始日期
   * 
   * @param end 结束日期
   * 
   * @return
   */
  public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
    List<String> nameList = new ArrayList<>();
    List<Long> numberList = new ArrayList<>();
    List<Orders> ordersList = orderMapper.getOrdersByStatusAndLTTime(Orders.COMPLETED, null);
    if (ordersList == null || ordersList.isEmpty()) {
      return SalesTop10ReportVO.builder().build();
    }
    nameList = orderDetailMapper.getNameByOrders(ordersList);
    for (String name : nameList) {
      Long number = orderDetailMapper.getNumberByName(name, ordersList) == null ? 0L
          : orderDetailMapper.getNumberByName(name, ordersList);
      numberList.add(number);
    }
    SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
        .nameList(null == nameList ? null : StringUtils.join(nameList, ","))
        .numberList(null == numberList ? null : StringUtils.join(numberList, ","))
        .build();
    return salesTop10ReportVO;
  }

  /**
   * 导出运营数据报表
   * 
   * @param response
   */
  public void exportBusinessData(HttpServletResponse response) {
    // 1. 查询数据库，获取营业数据---查询最近30天的运营数据
    LocalDate dateBegin = LocalDate.now().minusDays(30);
    LocalDate dateEnd = LocalDate.now().minusDays(1);

    // 查询概览数据
    BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN),
        LocalDateTime.of(dateEnd, LocalTime.MAX));

    // 2. 通过POI将数据写入到Excel文件中
    InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

    try {
      // 基于模板文件创建一个新的Excel文件
      XSSFWorkbook excel = new XSSFWorkbook(in);

      // 获取表格文件的Sheet页
      XSSFSheet sheet = excel.getSheet("Sheet1");

      // 填充数据--时间
      sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

      // 获得第4行
      XSSFRow row = sheet.getRow(3);
      row.getCell(2).setCellValue(businessDataVO.getTurnover());
      row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
      row.getCell(6).setCellValue(businessDataVO.getNewUsers());

      // 获得第5行
      row = sheet.getRow(4);
      row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
      row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

      // 填充明细数据
      for (int i = 0; i < 30; i++) {
        LocalDate date = dateBegin.plusDays(i);
        // 查询某一天的营业数据
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN),
            LocalDateTime.of(date, LocalTime.MAX));

        // 获得某一行
        row = sheet.getRow(7 + i);
        row.getCell(1).setCellValue(date.toString());
        row.getCell(2).setCellValue(businessData.getTurnover());
        row.getCell(3).setCellValue(businessData.getValidOrderCount());
        row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
        row.getCell(5).setCellValue(businessData.getUnitPrice());
        row.getCell(6).setCellValue(businessData.getNewUsers());
      }

      // 3. 通过输出流将Excel文件下载到客户端浏览器
      ServletOutputStream out = response.getOutputStream();
      excel.write(out);

      // 关闭资源
      out.close();
      excel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
