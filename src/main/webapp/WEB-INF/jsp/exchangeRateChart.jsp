<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns"></script>
</head>
<body>
	<div class="container mt-5">
		<div class="row">
			<div class="col-md-2">
				<select class="form-control" id="currencySelector">
					<option value="USD">美元 (USD)</option>
					<option value="HKD">港幣 (HKD)</option>
					<option value="CNY">人民幣 (CNY)</option>
					<option value="JPY">日圓 (JPY)</option>
					<option value="EUR">歐元 (EUR)</option>
					<option value="AUD">澳幣 (AUD)</option>
					<option value="CAD">加拿大幣 (CAD)</option>
					<option value="GBP">英鎊 (GBP)</option>
				</select>
			</div>
		</div>
		<canvas id="exchangeRateChart" width="600" height="125"></canvas>
	</div>
</body>

<script>
document.addEventListener('DOMContentLoaded', function() {
    var ctx = document.getElementById('exchangeRateChart').getContext('2d');
    var chart; //定義chart變數，以更新走勢圖

    function fetchData(selectedCurrency) {
        // 用fetch獲取相對應的api
        fetch('/exchangerates')
            .then(response => response.json())
            .then(data => {
                // 根據selectedCurrency篩選出對應的匯率數據
                const filteredData = data.filter(item => item.currency === selectedCurrency);
                const dates = filteredData.map(item => item.date); // 日期
                const buyRates = filteredData.map(item => item.buyRate); // 買進匯率
                const sellRates = filteredData.map(item => item.sellRate); // 賣出匯率
        
                if(chart) chart.destroy(); // 如果圖表已存在，先刪除
                chart = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: dates,
                        datasets: [{
                            label: '銀行買入',
                            data: buyRates,
                            borderColor: 'rgb(255, 159, 64)',
                            borderWidth: 2,
                            pointRadius: 0,
                            tension: 0.1
                            
                        }, {
                            label: '銀行賣出',
                            data: sellRates,
                            borderColor: 'rgb(54, 162, 235)',
                            borderWidth: 2,
                            pointRadius: 0,
                            tension: 0.1
                        }]
                    },
                    options: {
                        scales: {
                            x: {
                                type: 'time',
                                time: {
                                    unit: 'month', // 指定時間軸的單位為月
                                    tooltipFormat: 'EEEE, MMM d, yyyy', // 指定游標停止時顯示的日期格式
                                    displayFormats: {
                                        month: 'MMM’yy' // 指定橫軸標籤的顯示格式
                                    }
                                }
                            }
                        }
                    }
                });
            });
    }
    
    //初始化圖表，預設美元（USD）的匯率
    fetchData('USD');

    // 當切換選項時，更新圖表
    document.getElementById('currencySelector').addEventListener('change', function() {
        const selectedCurrency = this.value;
        fetchData(selectedCurrency);
    });
});
</script>
</html>