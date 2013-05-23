package com.axelor.apps.base.service.formula.call;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.account.db.PricingListVersion;
import com.axelor.apps.base.service.administration.GeneralService;
import com.axelor.apps.base.service.formula.Formula3Lvl;
import com.axelor.apps.base.service.formula.loader.Loader;
import com.axelor.exception.AxelorException;

/**
 * Singleton d'accès aux formules des assiettes.
 * 
 * @author guerrier
 * @version 1.0
 *
 */
@Singleton
public final class CalculationRuleTaxCall {
	
	private static final Logger LOG = LoggerFactory.getLogger(CalculationRuleTaxCall.class);
	
	private static volatile CalculationRuleTaxCall instance = null;
	
	private Class<Formula3Lvl<BigDecimal, PricingListVersion, InvoiceLine, BigDecimal>> klass;
	
	/**
	 * Constructeur.
	 * Initialise l'instance pour les formules d'assiettes.
	 */
	@SuppressWarnings("unchecked")
	@Inject
	private CalculationRuleTaxCall(){
		
		LOG.info("NEW CALCULATION RULE TAX");
		
		try {
			klass = (Class<Formula3Lvl<BigDecimal, PricingListVersion, InvoiceLine, BigDecimal>>) Loader.loaderFormula(GeneralService.getFormulaGenerator().getCalculationRuleTax());
		} 
		catch (Exception e) {
			throw new RuntimeException("Impossible de charger les formules d'assiettes de taxes", e);
		}
		
	}
	
	/**
	 * Méthode d'accès au singleton
	 * @return CalculationRuleTaxCall
	 */
	private static CalculationRuleTaxCall get(){
		
		if(instance == null){
			synchronized(CalculationRuleTaxCall.class){
				
				if(instance == null) { instance = new CalculationRuleTaxCall(); }
				
			}
		}
		
		return instance;
		
	}
	
	/**
	 * Récupérer l'instance des formules d'assiettes.
	 * 
	 * @return
	 */
	public static Formula3Lvl<BigDecimal, PricingListVersion, InvoiceLine, BigDecimal> newInstance(){
		
		try { return get().klass.newInstance(); } 
		catch (Exception e) { throw new RuntimeException("Impossible d'instancier les formules d'assiettes de taxes", e); } 
	}
	
	/**
	 * Réinitialiser l'instance des formules d'assiettes. 
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws AxelorException 
	 */
	@SuppressWarnings("unchecked")
	public static void reset() throws InstantiationException, IllegalAccessException, AxelorException {
		LOG.info("RESET CALCULATION RULE TAX");

		Loader.loader().removeClassCache(get().klass);
		get().klass = (Class<Formula3Lvl<BigDecimal, PricingListVersion, InvoiceLine, BigDecimal>>)  Loader.loaderFormula(GeneralService.getFormulaGenerator().getCalculationRuleTax());
		
	}
	
}
