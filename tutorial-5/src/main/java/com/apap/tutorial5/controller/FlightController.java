package com.apap.tutorial5.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.apap.tutorial5.model.FlightModel;
import com.apap.tutorial5.model.PilotModel;
import com.apap.tutorial5.service.FlightService;
import com.apap.tutorial5.service.PilotService;


@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
		
	@RequestMapping (value ="flight/delete", method = RequestMethod.POST)
	private String deleteFlight(@ModelAttribute PilotModel pilot, Model model) {
		for (FlightModel flight : pilot.getPilotFlight()) {
			flightService.deleteFlight(flight.getId());
		}
		return "deleteFlight";
	}
	
	@RequestMapping (value = "/flight/delete/{id}")
	private String deleteFlight (@PathVariable (value = "id") long id) {
		flightService.deleteFlight(id);
		return "deleteFlight";
	}
	
	@RequestMapping (value = "/flight/update/{id}", method = RequestMethod.GET)
	private String updateFlight (@PathVariable (value = "id") long id, Model model) {
		FlightModel currentFlight = flightService.getFlight(id);
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(currentFlight.getPilot().getLicenseNumber());
		currentFlight.setPilot(pilot);
		model.addAttribute("currentFlight", currentFlight);
		return "updateFlight";
	}
	
	@RequestMapping (value = "/flight/update", method = RequestMethod.POST)
	private String updateFlightSubmit (@ModelAttribute FlightModel currentflight) {		
		flightService.updateFlight(currentflight, currentflight.getId());
		return "suksesUpdateFlight";	
	}
	
	@RequestMapping (value = "/flight/view")
	private String viewFlight (@RequestParam ("flightNumber") String flightNumber, Model model) {		
		List <FlightModel> Flights = new ArrayList();
		List <FlightModel> allFlights = flightService.selectAll();
		
		for (FlightModel fli: allFlights) {
			if (fli.getFlightNumber().equals(flightNumber)) {
				Flights.add(fli);
			}
		}
		if (Flights.size() == 0){
			return "error";
		}
		model.addAttribute("flightNumber", flightNumber);
		model.addAttribute("flights", Flights);
		return "viewFlight";		
	}
	
	@RequestMapping (value = "/flight/view/{flightNumber}", method = RequestMethod.GET)
	private String viewFlights (@PathVariable (value = "flightNumber") String flightNumber, Model model) {
		List <FlightModel> Flights = new ArrayList();
		List <FlightModel> allFlights = flightService.selectAll();
		
		for (FlightModel fli: allFlights) {
			if (fli.getFlightNumber().equals(flightNumber)) {
				Flights.add(fli);
			}
		}
		if (Flights.size() == 0){
			return "error";
		}
		model.addAttribute("flightNumber", flightNumber);
		model.addAttribute("flights", Flights);
		return "viewFlight";		
	}
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add (@PathVariable (value = "licenseNumber") String licenseNumber, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		ArrayList <FlightModel> listFlight = new ArrayList<FlightModel>();
		listFlight.add(new FlightModel());
		pilot.setPilotFlight(listFlight);
		pilot.setLicenseNumber(licenseNumber);
		model.addAttribute("pilot", pilot);
		return "addFlight";
	}
	
	@RequestMapping (value = "/flight/add", method = RequestMethod.POST)
	private String addFlightSubmit (@ModelAttribute FlightModel flight) {
		flightService.addFlight(flight);
		return "add";
	}
	
	@RequestMapping(value="/flight/add/{pilot.licenseNumber}", params={"addRow"}, method = RequestMethod.POST)
	public String addRow(PilotModel pilot, BindingResult bindingResult, Model model) {
		if (pilot.getPilotFlight()==null) {
			ArrayList <FlightModel> listFlight = new ArrayList<FlightModel>();
			pilot.setPilotFlight(listFlight);
		}
		pilot.getPilotFlight().add(new FlightModel());
		
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}
	
	@RequestMapping(value="/flight/add/{pilot.licenseNumber}", params= {"removeRow"}, method = RequestMethod.POST)
	public String removeRow (PilotModel pilot, BindingResult bindingResult, Model model, HttpServletRequest req) {
		Integer indexRow = Integer.valueOf(req.getParameter("removeRow"));
		pilot.getPilotFlight().remove(indexRow.intValue());

		model.addAttribute("pilot", pilot);
		return "addFlight";
	}

	@RequestMapping(value="/flight/add/{pilot.licenseNumber}", params= {"save"}, method = RequestMethod.POST)
	private String saveAllFlight (@ModelAttribute PilotModel pilot) {
		
		//PilotModel realPilot = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
		System.out.println(pilot.getLicenseNumber() + "license" + pilot.getPilotFlight().size() + pilot.getPilotFlight().get(0).getFlightNumber());
		PilotModel realPilot = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
		for (FlightModel flight : pilot.getPilotFlight()) {		
			flight.setPilot(realPilot);
			
			flightService.addFlight(flight);
		}
		return "add";
	}
}