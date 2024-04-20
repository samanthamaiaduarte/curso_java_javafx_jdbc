package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller seller) {
		if(seller.getId() == null) {
			dao.insert(seller);
		} else
			dao.update(seller);
	}
	
	public void remove(Seller seller) {
		dao.deleteById(seller.getId());
	}
}
