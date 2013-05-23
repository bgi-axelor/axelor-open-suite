package com.axelor.apps.base.service;

import com.axelor.apps.base.db.BankDetails;
import com.axelor.apps.base.db.Bic;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.tool.StringTool;

public class BankDetailsService {
	
	/**
	 * Méthode qui permet d'extraire les informations de l'iban
	 * Met à jour les champs suivants :
	 * 		<ul>
     *      	<li>BankCode</li>
     *      	<li>SortCode</li>
     *      	<li>AccountNbr</li>
     *      	<li>BbanKey</li>
     *      	<li>CountryCode</li>
     *      	<li>Bic</li>
	 * 		</ul>
	 * 
	 * @param bankDetails
	 * @return BankDetails
	 */
	public BankDetails detailsIban(BankDetails bankDetails){
		
		if(bankDetails.getIban()!=null){
			
			bankDetails.setBankCode(StringTool.extractStringFromRight(bankDetails.getIban(),23,5));
			bankDetails.setSortCode(StringTool.extractStringFromRight(bankDetails.getIban(),18,5));
			bankDetails.setAccountNbr(StringTool.extractStringFromRight(bankDetails.getIban(),13,11));
			bankDetails.setBbanKey(StringTool.extractStringFromRight(bankDetails.getIban(),2,2));
			bankDetails.setCountryCode(StringTool.extractStringFromRight(bankDetails.getIban(),27,2));
			Bic bic = Bic.all().filter(
					"self.countryCode = ?1 " +
					"AND self.sortCode = ?2 " +
					"AND self.bankCode = ?3", bankDetails.getCountryCode(), bankDetails.getSortCode(), bankDetails.getBankCode()).fetchOne();
			if(bic != null){
				bankDetails.setBic(bic.getCode());
			}
			else{
				bankDetails.setBic(null);
			}
			
		}
		return bankDetails;
	}
	
	
	/**
	 * Méthode permettant de créer un RIB
	 * 
	 * @param accountNbr
	 * @param bankAddress
	 * @param bankCode
	 * @param bbanKey
	 * @param bic
	 * @param countryCode
	 * @param ownerName
	 * @param payerPartner
	 * @param sortCode
	 * 
	 * @return
	 */
	public BankDetails createBankDetails(String accountNbr, String bankAddress, String bankCode, String bbanKey, String bic, 
			String countryCode, String ownerName, Partner partner, String sortCode)  {
		BankDetails bankDetails = new BankDetails();
		bankDetails.setAccountNbr(accountNbr);
		bankDetails.setBankAddress(bankAddress);
		bankDetails.setBankCode(bankCode);
		bankDetails.setBbanKey(bbanKey);
		bankDetails.setBic(bic);
		bankDetails.setCountryCode(countryCode);
		
		bankDetails.setOwnerName(ownerName);
		bankDetails.setPartner(partner);
		
		bankDetails.setSortCode(sortCode);
		
		return bankDetails;
	}

}
