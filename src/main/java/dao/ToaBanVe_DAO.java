package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connectDB.Database;
import dto.Toa_DTO;
import entity.Toa;


public class ToaBanVe_DAO {
	public List<Toa_DTO> getAllToa(){
		List<Toa_DTO> list = new ArrayList<>();
		String sql = "SELECT viTriToa, loaiToa, soGhe FROM Toa";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				// Lấy dữ liệu từ DB dưới dạng String
	            String soGheStr = rs.getString("soGhe");
	            
	            // Chuyển đổi String sang int để phù hợp với Entity Toa
	            int soGheInt = 0;
	            if (soGheStr != null) {
	                soGheInt = Integer.parseInt(soGheStr);
	            }
				
	            Toa_DTO t = new Toa_DTO(
						rs.getString("viTriToa"),
						rs.getString("loaiToa"),
						soGheInt
				);
				list.add(t);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}			
		return list;
	}
}
