package com.billy.exchangeratechart.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRate {

	/* 幣別 */
	private String currency;
	/* 日期 */
	private LocalDate date;
	/* 買入匯率 */
	private BigDecimal buyRate;
	/* 賣出匯率 */
	private BigDecimal sellRate;
}