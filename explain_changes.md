# connectDB(giữ nguyên)

# dao:
**Thêm file:**
1. Class KhachHangBanVe_Dao
2. Class ToaBanVe_DAO
3. Class ChuyenTauBanVe_DAO

# entity (giữ nguyên)

# dto(package mới):
**Thêm file:**
1. Class KhachHang_DTO
2. Class Toa_DTO
3. Class ChuyenTauDTO

# view
**Cập nhật**
1. Lớp BanVePage:

	private JComboBox<KhachHang_DTO> createCustomerCombo() {
		cboKhachHang = new JComboBox<>();
		
		KhachHangBanVe_Dao khDao = new KhachHangBanVe_Dao();
		
		for(KhachHang_DTO kh : khDao.getAllKhachHang()) {
			cboKhachHang.addItem(kh);
		}
		
		cboKhachHang.setSelectedIndex(0);
		cboKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboKhachHang.setPreferredSize(new Dimension(220, 30));
		cboKhachHang.addActionListener(e -> {
			KhachHang_DTO kh = (KhachHang_DTO) cboKhachHang.getSelectedItem();
			if (kh != null) {
				// Có thể xóa selectedKhachHang vì đã chuyển sang dùng object thay vì String
				String selectedKhachHang = 
						kh.getMaKH() + " - " +		   
						kh.getTenKH() + " - " +		   
						kh.getSdt();	
				refreshSummary();
			}
		});
		return cboKhachHang;
	}
	
	private JComboBox<ChuyenTau_DTO> createChuyenCombo() {
	    cboChuyenTau = new JComboBox<>();

	    ChuyenTauBanVe_Dao ctDao = new ChuyenTauBanVe_Dao();

	    for (ChuyenTau_DTO ct : ctDao.getAllMaChuyenTau()) {
	        cboChuyenTau.addItem(ct); 
	    }

	    cboChuyenTau.setSelectedIndex(0);
	    cboChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    cboChuyenTau.setPreferredSize(new Dimension(300, 30));

	    cboChuyenTau.addActionListener(e -> {
	        ChuyenTau_DTO ct = (ChuyenTau_DTO) cboChuyenTau.getSelectedItem();

	        if (ct != null) {
	        	// Có thể xóa selectedChuyen vì đã chuyển sang dùng object thay vì String
	            String selectedChuyen =
	                    ct.getTenTau() + " - " +
	                    ct.getGaDi() + " → " +
	                    ct.getGaDen() + " (" +
	                    ct.getNgayKhoiHanh() + " - " +
	                    ct.getGioKhoiHanh() + ")";
	            
	            refreshSummary();
	        }
	    });

	    return cboChuyenTau;
	}

	private JComboBox<Toa_DTO> createToaCombo() {
		cboToaTau = new JComboBox<>();
		
		ToaBanVe_DAO toaDao = new ToaBanVe_DAO();
		
		for(Toa_DTO t : toaDao.getAllToa()) {
			cboToaTau.addItem(t);
		}
		
		cboToaTau.setSelectedIndex(0);
		cboToaTau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboToaTau.setPreferredSize(new Dimension(220, 30));
		cboToaTau.addActionListener(e -> {
			
			Toa_DTO t = (Toa_DTO) cboToaTau.getSelectedItem();
			if (t != null) {
				// Có thể xóa selectedToa vì đã chuyển sang dùng object thay vì String
				String selectedToa = 
						t.getViTriToa() + " - " +		   
						t.getLoaiToa() + " - " +		   
						t.getSoGhe() + " ghế";	
				refreshSummary();
			}
		});
		return cboToaTau;
	}