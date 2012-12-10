package com.mipos.reader;

public class IIN {
	private String prefix;
	private String name;
	private CardType type;
	
	public IIN(String p, String n, String t){
		this.prefix = p;
		this.name = n;
		setTypeFromString(t);
	}
	
	private void setTypeFromString(String t){
		for (CardType ct : CardType.values()){
			if (ct.name().equalsIgnoreCase(t)){
				setType(ct);
				return;
			}
		}
		setType(CardType.UNKNOWN);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}

}
