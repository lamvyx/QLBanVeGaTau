package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.Database;
import dto.ChuyenTau_DTO;
import entity.ChuyenTau;

public class ChuyenTauBanVe_Dao {
	public List<ChuyenTau_DTO> getAllMaChuyenTau() {
	    List<ChuyenTau_DTO> list = new ArrayList<>();

	
	    try {
	        Connection con = Database.getConnection();

	        String sql = """
	        		SELECT 
					    ct.maCT,
					    t.tenTau,
					    tt.maGaDi,
					    tt.maGaDen,
					    ct.gioKhoiHanh,
					    ct.ngayKhoiHanh
					FROM ChuyenTau ct
					JOIN TuyenTau tt ON ct.maTuyenTau = tt.maTT
					JOIN Tau t ON ct.maTau = t.maTau
	        		""";

	        PreparedStatement ps = con.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	        	ChuyenTau_DTO ct = new ChuyenTau_DTO(
	                rs.getString("maCT"),
	                rs.getString("tenTau"),
	                rs.getString("maGaDi"),
	                rs.getString("maGaDen"),
	                rs.getDate("ngayKhoiHanh").toLocalDate(),
	                rs.getTime("gioKhoiHanh").toLocalTime()
	            );
	            list.add(ct);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
}
}
