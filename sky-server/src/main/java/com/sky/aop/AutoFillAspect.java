package com.sky.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 自动填充切面类
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
  @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
  public void autoFillPointCut() {// 切入点表达式方法
  }

  /**
   * 自动填充
   * 
   * @param joinPoint
   */
  @Before("autoFillPointCut()")
  public void autoFill(JoinPoint joinPoint) {
    log.info("自动填充开始");
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取方法签名
    OperationType operationType = signature.getMethod().getAnnotation(AutoFill.class).value();// 获取注解参数
    Object args[] = joinPoint.getArgs();
    if (args == null || args.length == 0) {// 判断空指针
      return;
    }
    Object entity = args[0];// 约定用到autofill的方法 第一个参数放实体类 取实体类
    // 准备要填充的数据
    LocalDateTime now = LocalDateTime.now();
    Long id = BaseContext.getCurrentId();
    // 根据操作类型，填充数据
    if (operationType == OperationType.INSERT) {
      // 如果是INSERT操作
      // 获取公共字段的setter

      try {
        Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,
            LocalDateTime.class);
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,
            LocalDateTime.class);
        Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        // 设置公共字段
        setCreateTime.invoke(entity, now);
        setUpdateTime.invoke(entity, now);
        setCreateUser.invoke(entity, id);
        setUpdateUser.invoke(entity, id);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }

    } else if (operationType == OperationType.UPDATE) {
      try {
        Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,
            LocalDateTime.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

        // 如果是UPDATE操作，仅设置更新的属性
        setUpdateTime.invoke(entity, now);
        setUpdateUser.invoke(entity, id);
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    log.info("公共字段自动填充完成");
  }
}
