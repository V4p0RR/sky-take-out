package com.sky.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
  @Autowired
  private SetmealMapper setmealMapper;
}
