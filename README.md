# Assumptions: 
- The number of countries in the world does not exceed 400 in the near future because the API only gets data for at most 400 countries
- Countries with no data is considered to have a GDP of 0.0 in World Bank's calculations


------------------------------------------------------------
This program prints the country and its GDP as a percentage of world GDP (using the indicator code: `NY.GDP.MKTP.CD`) sorted from highest to lowest based on last year in the range provided; countries with the highest last year GDP at the top.

The program will make HTTP requests to the World Bank API documented here:

https://datahelpdesk.worldbank.org/knowledgebase/topics/125589-developer-information.

No third-party dataset library is used.

The program will be executed in the command line terminal with the following interface:

```
gdp --from-year 2020 --to-year 2022
```

- `--from-year` inclusive
- `--to-year` inclusive

The program will print to the standard output in comma-separated values (CSV) format with columns added for every year in the range ordered by last year's percentage, higher percentages at the top. Percentages are rounded to 2 decimal places (eg: 4.45)
```
Country,2020,2021,2022
"United States",24.71,24.07,25.32
"China",17.24,18.39,17.86
"Japan",5.92,5.17,4.21
...
...
"Yemen, Rep.",0.0,0.0,0.0
```
*check output.csv file for full output


## Note
The World Bank's [terms of use](https://www.worldbank.org/en/about/legal/terms-and-conditions) and API usage guidelines are strongly respected. 
