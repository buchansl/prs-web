package com.prs.db;



//import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.LineItem;
//import com.prs.business.Request;
import com.prs.business.Request;

public interface LineitemRepository extends CrudRepository<LineItem, Integer> {
	
	
}
