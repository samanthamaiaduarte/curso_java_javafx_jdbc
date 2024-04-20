package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate").toLocalDate());
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(department);

		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("DepartmentId"));
		department.setName(rs.getString("DepartmentName"));

		return department;
	}

	@Override
	public void insert(Seller seller) {
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement (
					"INSERT INTO seller " 
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, Date.valueOf(seller.getBirthDate()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());

			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				
				ResultSet rs = st.getGeneratedKeys();
				
				if(rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				
				DB.closeResultSet(rs);
				
			} else {
				throw new DbException ("Unexpected error! No rows affected.");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller seller) {
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement (
					"UPDATE seller "
					+ "SET Name = ?, "
					+ "Email = ?, "
					+ "BirthDate = ?, "
					+ "BaseSalary = ?, "
					+ "DepartmentId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, Date.valueOf(seller.getBirthDate()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			st.setInt(6, seller.getId());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement (
					"DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepartmentName " + "FROM seller "
					+ "INNER JOIN department ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();
			if (rs.next()) {

				Department department = instantiateDepartment(rs);

				Seller seller = instantiateSeller(rs, department);

				return seller;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		return null;

	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT seller.*, department.Name as DepartmentName " + "FROM seller "
					+ "INNER JOIN department ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			rs = st.executeQuery();
			while (rs.next()) {

				Department department = map.get(rs.getInt("DepartmentId"));
				if (department == null) {
					department = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), department);
				}

				Seller seller = instantiateSeller(rs, department);
				sellers.add(seller);

			}

			return sellers;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department dep) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT seller.*, department.Name as DepartmentName " + "FROM seller "
					+ "INNER JOIN department ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? "
					+ "ORDER BY Name");

			st.setInt(1, dep.getId());

			List<Seller> sellers = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			rs = st.executeQuery();
			while (rs.next()) {

				Department department = map.get(rs.getInt("DepartmentId"));
				if (department == null) {
					department = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), department);
				}

				Seller seller = instantiateSeller(rs, department);
				sellers.add(seller);

			}

			return sellers;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

}
