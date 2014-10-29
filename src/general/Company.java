package general;

public enum Company {
	snp("SNP", "S&P 500"), blizzard("ATVI", "Activision Blizzard"), adobe("ADBE", "Adobe Systems Incorporated"), akamai(
			"AKAM", "Akamai Technologies, Inc"), amazon("AMZN", "Amazon.com, Inc."), apple("AAPL", "Apple Inc."), bidu(
			"BIDU", "Baidu.com, Inc."), broadcom("BRCM", "Broadcom Corporation"), cisco("CSCO", "Cisco Systems, Inc."), ebay(
			"EBAY", "eBay Inc."), expedia("EXPE", "Expedia, Inc."), facebook("FB", "Facebook, Inc."), google_a("GOOGL",
			"Google Inc. Class A"), google_c("GOOG", "Google Inc. Class C"), intel("INTC", "Intel Corporation"), microsoft(
			"MSFT", "Microsoft Corporation"), netflix("NFLX", "Netflix"), nvidia("NVDA", "NVIDIA Corporation"), qualcom(
			"QCOM", "QUALCOMM Incorporated"), tesla("TSLA", "Tesla Motors, Inc."), yahoo("YHOO", "Yahoo! Inc.");

	private String symbol;
	private String name;

	Company(String symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

}