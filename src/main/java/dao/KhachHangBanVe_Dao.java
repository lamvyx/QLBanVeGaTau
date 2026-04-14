package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connectDB.Database;
import dto.KhachHang_DTO;
import entity.KhachHang;

public class KhachHangBanVe_Dao {
	public List<KhachHang_DTO> getAllKhachHang(){
		List<KhachHang_DTO> list = new ArrayList<>();
		String sql = "SELECT maKH, tenKH, sdt FROM KhachHang";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				KhachHang_DTO kh = new KhachHang_DTO(
						rs.getString("maKH"),
						rs.getString("tenKH"),
						rs.getString("sdt")
				);
				list.add(kh);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}			
		return list;
	}
}
