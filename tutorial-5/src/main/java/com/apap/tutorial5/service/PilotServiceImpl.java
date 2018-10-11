package com.apap.tutorial5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tutorial5.model.PilotModel;
import com.apap.tutorial5.repository.PilotDb;

@Service
@Transactional
public class PilotServiceImpl implements PilotService{
	@Autowired
	private PilotDb pilotDb;
	
	@Override
	public PilotModel getPilotDetailByLicenseNumber(String licenseNumber) {
		return pilotDb.findByLicenseNumber(licenseNumber);
	}

	@Override
	public void addPilot(PilotModel pilot) {
		pilotDb.save(pilot);
	}

	@Override
	public void deletePilot(long id) {
		pilotDb.deleteById(id);
	}

	@Override
	public void updatePilot(PilotModel pilot, String licenseNumber) {
		PilotModel updatedPilot = pilotDb.findByLicenseNumber(licenseNumber);
		updatedPilot.setName(pilot.getName());
		updatedPilot.setFlyHour(pilot.getFlyHour());
		pilotDb.save(updatedPilot);
	}
}
