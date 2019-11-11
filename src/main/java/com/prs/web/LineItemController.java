package com.prs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineitemRepository;

import com.prs.db.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/line-items")
public class LineItemController {
	@Autowired
	private LineitemRepository lineItemRepo;
	@Autowired
	private RequestRepository requestRepo;

// return all LineItems
	@GetMapping("/")
	public JsonResponse listLineItem() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

//return one LineItem for given id
	@GetMapping("/{id}")
	public JsonResponse getLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findById(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

// add a new LineItem

	@PostMapping("/")
	public JsonResponse addLineItem(@RequestBody LineItem l) {
		// add a new LineItem
		JsonResponse jr = null;

		try {
			jr = JsonResponse.getInstance(lineItemRepo.save(l));
			recalculateTotal(l.getRequest());
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}
/// Update a LineItem

	@PutMapping("/")
	public JsonResponse updateLineItem(@RequestBody LineItem l) {
		// update a LineItem
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(l.getId())) {
				jr = JsonResponse.getInstance(lineItemRepo.save(l));
				recalculateTotal(l.getRequest());

			} else {
				// doesn't exist
				jr = JsonResponse.getInstance("Error updating LineItem. id :" + l.getId() + "doesn't exist!");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}
/// delete a LineItem

	@DeleteMapping("/{id}")
	public JsonResponse deleteLineItem(@PathVariable int id) {
		// delete a LineItem
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(id)) {
				Request lineItem = lineItemRepo.findById(id).get().getRequest();
						lineItemRepo.deleteById(id);
				
				jr = JsonResponse.getInstance("Delete successful");
				recalculateTotal(lineItem);
			} else {
				// doesn't exist
				jr = JsonResponse.getInstance("Error Deleting LineItem. id :" + id + "doesn't exist!");
			}

		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
		}

		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;

	}

	private void recalculateTotal(Request id) {
		List<Request> rlist = requestRepo.findAllById(id);

		double total = 0.0; 
		for (Request r : rlist) {
			total += r.getTotal();
		}
		try {
			id.setTotal(total);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
