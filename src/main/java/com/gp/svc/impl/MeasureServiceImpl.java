package com.gp.svc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gp.dao.MeasureDAO;
import com.gp.dao.PseudoDAO;
import com.gp.svc.MeasureService;

@Service("measureService")
public class MeasureServiceImpl implements MeasureService{
	
	static Logger LOGGER = LoggerFactory.getLogger(MeasureServiceImpl.class);
	
	@Autowired
	MeasureDAO measuredao;
	
	@Autowired 
	PseudoDAO pseudodao;
	
	
}
