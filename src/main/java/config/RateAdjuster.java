package config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//匯率調整值工具
public class RateAdjuster {

	private static final Map<String, BigDecimal[]> adjustmentMap = new HashMap<>();

	static {
		/* 美金 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_USD,
				new BigDecimal[] { BigDecimal.valueOf(-0.2), BigDecimal.valueOf(0.25) });
		/* 人民幣 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_CNY,
				new BigDecimal[] { BigDecimal.valueOf(-0.036), BigDecimal.valueOf(0.074) });
		/* 港幣 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_HKD,
				new BigDecimal[] { BigDecimal.valueOf(-0.03), BigDecimal.valueOf(0.07) });
		/* 日幣 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_JPY,
				new BigDecimal[] { BigDecimal.valueOf(-0.002), BigDecimal.valueOf(0.001) });
		/* 歐元 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_EUR,
				new BigDecimal[] { BigDecimal.valueOf(-0.3), BigDecimal.valueOf(0.3) });
		/* 澳幣 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_AUD,
				new BigDecimal[] { BigDecimal.valueOf(-0.3), BigDecimal.valueOf(0.3) });
		/* 加拿大幣 */	
		adjustmentMap.put(CurrencyConstants.CURRENCY_CAD,
				new BigDecimal[] { BigDecimal.valueOf(-0.31), BigDecimal.valueOf(0.31) });
		/* 英鎊 */
		adjustmentMap.put(CurrencyConstants.CURRENCY_GBP,
				new BigDecimal[] { BigDecimal.valueOf(-0.3), BigDecimal.valueOf(0.3) });
	}

	public static BigDecimal adjustSellRate(String currency, BigDecimal rate) {
		BigDecimal adjustment = adjustmentMap.get(currency)[0];
		return rate.add(adjustment);
	}

	public static BigDecimal adjustBuyRate(String currency, BigDecimal rate) {
		BigDecimal adjustment = adjustmentMap.get(currency)[1];
		return rate.add(adjustment);
	}
}
