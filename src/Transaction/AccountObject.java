/*
 * Copyright (c) 2014 Joseph Su
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Transaction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * @author Joseph Su
 * 
 * Transactional objects, Account
 * 
 */

public class AccountObject {
	
	String accountNum = null;
	String showName = null;
	String checkbookID = null;
	String outputFileName = null;
	String inputFileName = null;
	String bankName = null;
	
	

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public String getFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String fileName) {
		this.outputFileName = fileName;
	}

	Map <String, Double> map = new LinkedHashMap<String, Double>();
	ArrayList<String> POList = new ArrayList<String>(); 

	
	/* 
	 * Copy constructor
	 */
	public AccountObject(AccountObject another) {
		this.accountNum = another.getAccountNumber();
		this.showName = another.getShowName();
		this.outputFileName = another.getFileName();
		this.checkbookID = another.getCheckbookID();
		this.inputFileName = another.getInputFileName();
		this.bankName = another.getBankName();
		
		Map<String, Double> map = another.getMap();
		for (Map.Entry<String, Double> mapEntry : map.entrySet()) {			
			this.insert(mapEntry.getKey(), mapEntry.getValue());
		}
		
	    ArrayList<String> list = another.getPOList();
		for (String entry : list) {
			this.po_insert(entry);
		}
 
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getOutputFileName() {
		return outputFileName;
	}

	public String getCheckbookID() {
		return checkbookID;
	}

	public AccountObject() {
		
	}
	
	public void setAccountNumber(String num) {
		this.accountNum = num;
	}
	
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getAccountNumber() {
		return accountNum;
	}
	
	public void insert(String key, Double value) {
		map.put(key, value);
	}
	
	public Map<String, Double> getMap() {
		return map;
	}

	public void po_insert(String data) {
		POList.add(data);
	}
	
	public void po_clear() {
		POList.clear();
	}
	
	
	
	public ArrayList<String> getPOList() {
		return POList;
	}
	
	
	public void setCheckbookID(String value) {		
		this.checkbookID = value;
	}
			
}
