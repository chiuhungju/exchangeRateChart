package com.billy.exchangeratechart.service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.billy.exchangeratechart.model.ExchangeRate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import config.CurrencyConstants;
import config.RateAdjuster;

@Service
public class ExchangeRateChartService {

	// 透過API取得相關幣值的匯率資料
	public List<ExchangeRate> fetchExchangeRates(String url) {

		List<ExchangeRate> exchangeRates = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();
		// 以今天為準
		LocalDate endDate = LocalDate.now();
		// 往前推一年
		LocalDate startDate = endDate.minusYears(1);
		String[] currencies = { CurrencyConstants.CURRENCY_HKD, CurrencyConstants.CURRENCY_USD,
				CurrencyConstants.CURRENCY_CNY, CurrencyConstants.CURRENCY_JPY, CurrencyConstants.CURRENCY_EUR,
				CurrencyConstants.CURRENCY_AUD, CurrencyConstants.CURRENCY_CAD, CurrencyConstants.CURRENCY_GBP };

		for (String currency : currencies) {
			try {
				String requestBody = String.format("""
						{
						    "Currency": "%s",
						    "StartDate": "%s",
						    "EndDate": "%s",
						    "ItemId": "FF9F6986FD574497B5C6CC93B9838C6D"
						}
						""", currency, startDate, endDate);

				HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
						.header("Content-Type", "application/json; charset=utf-8")
						.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				// 處理返回的匯率並添加到匯率列表中
				List<ExchangeRate> newRates = parseAndAddRates(response.body(), currency);
				exchangeRates.addAll(newRates);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return exchangeRates;
	}

	// 將取得的匯率json資料轉成對應的值
	private static List<ExchangeRate> parseAndAddRates(String json, String currency) throws Exception {

		List<ExchangeRate> newRates = new ArrayList<>();
		JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
		JsonArray dataArray = jsonObj.getAsJsonArray("Data");
		JsonArray dateArray = jsonObj.getAsJsonArray("Date");

		for (int i = 0; i < dateArray.size(); i++) {

			long epochMilliSell = dataArray.get(0).getAsJsonArray().get(i).getAsJsonArray().get(0).getAsLong();
			BigDecimal rateSell = BigDecimal
					.valueOf(dataArray.get(0).getAsJsonArray().get(i).getAsJsonArray().get(1).getAsDouble());
			// 根據不同幣值的調整值調整匯率
			rateSell = RateAdjuster.adjustSellRate(currency, rateSell);
			
			BigDecimal rateBuy = BigDecimal
					.valueOf(dataArray.get(1).getAsJsonArray().get(i).getAsJsonArray().get(1).getAsDouble());
			
			rateBuy = RateAdjuster.adjustBuyRate(currency, rateBuy);
			
			LocalDate date = Instant.ofEpochMilli(epochMilliSell).atZone(ZoneId.systemDefault()).toLocalDate();
			
			ExchangeRate rate = new ExchangeRate();
			rate.setCurrency(currency);
			rate.setDate(date);
			rate.setSellRate(rateSell);
			rate.setBuyRate(rateBuy);
			newRates.add(rate);
		}
		return newRates;
	}
}
