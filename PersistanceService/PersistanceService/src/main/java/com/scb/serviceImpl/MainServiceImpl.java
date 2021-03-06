package com.scb.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scb.model.PersistanceData;
import com.scb.model.compositekey.PersistanceDataCompositeKey;
import com.scb.repository.PersistanceDataRepository;
import com.scb.service.MainService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MainServiceImpl implements MainService {
	@Autowired
	private PersistanceDataRepository persistancedatarepositary;

	@Override
	public boolean saveTrancation(PersistanceData persistdata) {
		log.info("Transaction received: " + persistdata);
		PersistanceData persistDataVar = null;
		try {
			persistDataVar = (PersistanceData) persistancedatarepositary
					.findById(persistdata.getPersistanceDataCompositeKey()).get();
		} catch (NoSuchElementException ex) {
			log.info("Error in finding transaction" + ex.getMessage());
		}
		if (persistDataVar != null) {
			return false;
		} else {
			log.info("Transaction deatils being saved in db");
			int count=persistancedatarepositary.findAll().size();
			PersistanceDataCompositeKey pCompositeKey=persistdata.getPersistanceDataCompositeKey();
			pCompositeKey.setReferenceId(count);
			persistdata.setPersistanceDataCompositeKey(pCompositeKey);
			persistancedatarepositary.save(persistdata);
			log.info("transaction saved in db");
			return true;
		}
	}

	@Override
	public List<PersistanceData> getAllTransations() {
		List<PersistanceData> list = new ArrayList<>();
		persistancedatarepositary.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public PersistanceData getTransactionById(long transactionId) {
		PersistanceData obj = persistancedatarepositary.findById(transactionId).get(0);

		return obj;
	}

	@Override
	public List<PersistanceData> getTransactionByType(String transactionType) {
		List<PersistanceData> obj = persistancedatarepositary.findByType(transactionType);
		return obj;
	}

}
