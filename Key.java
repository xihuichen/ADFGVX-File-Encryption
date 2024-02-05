package ie.atu.sw;



public class Key {
	//This class is to set key (a String) for ADFGVX encryption and decryption.
	//Not packing the key sorting function in this class for two reasons: 1. Single objective principle 2.Complexity of cross referencing this class
	private String key = "LUCKY";

	public String getKey() {
		String upper=this.key.toUpperCase();
		
		
		return upper;
	}

	public void setKey(String key) {
		this.key = key;
	}}
