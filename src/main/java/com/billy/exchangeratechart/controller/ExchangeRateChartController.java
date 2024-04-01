package com.billy.exchangeratechart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.billy.exchangeratechart.model.ExchangeRate;
import com.billy.exchangeratechart.service.ExchangeRateChartService;
import config.CurrencyConstants;

@Controller
public class ExchangeRateChartController {

	@Autowired
	ExchangeRateChartService exchangeRateChartService;

	@GetMapping("/exchangerates")
	@ResponseBody
	public List<ExchangeRate> getExchangeRates() {
		String htmlContent = CurrencyConstants.EXCHANGE_RATE_URL;
		List<ExchangeRate> exchangeRates = exchangeRateChartService.fetchExchangeRates(htmlContent);
		return exchangeRates;
	}

	// 導向前端頁面
	@GetMapping("/exchangeratechart")
	public String showExchangeRateChart() {		
		return "exchangeRateChart";
	}

}
