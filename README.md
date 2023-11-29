# Crypto Recommendation Service Demo
Returns descending sorted list of all the cryptos,comparing the normalized range\
 http://localhost:8080/sort_norm_crypto \
Returns crypto data for a desired crypto\
 http://localhost:8080/specific_crypto_data?cryptoCurrency=DOGE \
Returns min price for a desired crypto\
 http://localhost:8080/specific_min_crypto_data?cryptoCurrency=XRP \
Returns max price for a desired crypto\
 http://localhost:8080/specific_max_crypto_data?cryptoCurrency=DOGE \
Returns oldest crypto\
 http://localhost:8080/specific_oldest_crypto_data?cryptoCurrency=BTC \
Returns newest crypto\
 http://localhost:8080/specific_newest_crypto_data?cryptoCurrency=XRP \
Returns old new min max in this order\
 http://localhost:8080/oldnewmimax_crypto_data?cryptoCurrency=BTC \
Return the crypto with the highest normalized range for a specific day\
 http://localhost:8080/get_highest_crypto_for_day?date=2022/01/01 \
Return the crypto with the highest normalized range for a specified date range\
 http://localhost:8080/get_highest_crypto_for_date_range?startDate=2022/01/01&endDate=2022/01/04 \
