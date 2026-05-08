package view;

import connectDB.Database;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Màn hình Lập hóa đơn: chọn vé CHO_THANH_TOAN, áp khuyến mãi/thuế, ghi DB.
 */
public class LapHoaDonPage extends JPanel {
	private static final long serialVersionUID = 1L;

	// ── UI fields ──────────────────────────────────────────────────────────
	private final JTextField txtMaHD  = new JTextField();
	private final JTextField txtMaNV  = new JTextField();
	private final JTextField txtMaKH  = new JTextField();
	private final JTextField txtThue  = new JTextField("10%  (VAT)");
	private final JTextField txtTongTruoc = new JTextField("0");
	private final JTextField txtTongSau  = new JTextField("0");
	private final JComboBox<String> cboMaKM;
	private final JComboBox<String> cboPhuongThuc = new JComboBox<>(
			new String[]{"TIEN_MAT", "CHUYEN_KHOAN", "VI_DIEN_TU"});
	private final JTable tblVe;
	private final DefaultTableModel model;
	private final TaiKhoan user;

	// ── Dữ liệu ────────────────────────────────────────────────────────────
	private final Map<String, BigDecimal> khuyenMaiMap = new HashMap<>();
	private String selectedMaVe = null;
	private String selectedMaKH = null;

	public LapHoaDonPage() {
		this(null);
	}

	public LapHoaDonPage(TaiKhoan user) {
		this.user = user;
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		if (user != null) {
			txtMaNV.setText(user.getTenDangNhap()); // Giả sử username là mã nhân viên hoặc map từ DB
		} else {
			txtMaNV.setText("NV001");
		}

		// Khởi tạo bảng vé
		String[] cols = {"Mã vé", "Khách hàng", "Chuyến", "Toa / Ghế", "Giá vé (đ)", "Trạng thái"};
		model = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};
		tblVe = new JTable(model);
		AppTheme.styleTable(tblVe);
		tblVe.setRowHeight(32);
		tblVe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblVe.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) onVeSelected();
		});

		// Combo khuyến mãi
		cboMaKM = new JComboBox<>();
		cboMaKM.addItem("-- Không áp dụng --");
		cboMaKM.setFont(AppTheme.font(Font.PLAIN, 13));
		cboMaKM.addActionListener(e -> tinhTongTien());

		// Nạp dữ liệu
		nạpKhuyenMai();
		nạpVeChoThanhToan();
		tạoMaHDMoi();

		add(taoHeader(), BorderLayout.NORTH);
		add(taoBody(), BorderLayout.CENTER);
	}

	// ── Header ─────────────────────────────────────────────────────────────
	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout(0, 6));
		header.setOpaque(false);
		JLabel title = new JLabel("Lập hóa đơn");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(AppTheme.PRIMARY);
		JLabel sub = new JLabel("Chốt thanh toán cho vé CHO_THANH_TOAN và ghi hóa đơn vào hệ thống");
		sub.setFont(AppTheme.font(Font.PLAIN, 12));
		sub.setForeground(AppTheme.TEXT_MUTED);
		header.add(title, BorderLayout.NORTH);
		header.add(sub,   BorderLayout.CENTER);
		return header;
	}

	// ── Body ────────────────────────────────────────────────────────────────
	private JPanel taoBody() {
		JPanel body = new JPanel(new BorderLayout(12, 0));
		body.setOpaque(false);

		// Bảng bên trái
		JPanel left = new JPanel(new BorderLayout(0, 8));
		left.setBackground(AppTheme.CARD_BG);
		left.setBorder(AppTheme.cardBorder());
		JLabel lblLeft = new JLabel("Vé chờ lập hóa đơn  (click để chọn)");
		lblLeft.setFont(AppTheme.font(Font.BOLD, 14));
		lblLeft.setForeground(AppTheme.TEXT_PRIMARY);
		left.add(lblLeft, BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(tblVe);
		scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		left.add(scroll, BorderLayout.CENTER);

		JButton btnLamMoi = new JButton("Làm mới danh sách");
		AppTheme.styleSecondaryButton(btnLamMoi);
		btnLamMoi.addActionListener(e -> { nạpVeChoThanhToan(); tạoMaHDMoi(); });
		left.add(btnLamMoi, BorderLayout.SOUTH);

		// Form bên phải
		JPanel right = new JPanel(new BorderLayout(0, 10));
		right.setBackground(AppTheme.CARD_BG);
		right.setBorder(AppTheme.cardBorder());
		right.setPreferredSize(new Dimension(440, 10));
		JLabel lblRight = new JLabel("Thông tin hóa đơn");
		lblRight.setFont(AppTheme.font(Font.BOLD, 14));
		lblRight.setForeground(AppTheme.TEXT_PRIMARY);
		right.add(lblRight, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 0, 8, 8);
		gbc.fill  = GridBagConstraints.HORIZONTAL;

		txtMaHD.setEditable(false);
		txtMaNV.setEditable(false);
		txtMaKH.setEditable(false);
		txtThue.setEditable(false);
		txtTongTruoc.setEditable(false);
		txtTongSau.setEditable(false);
		styleInput(txtMaHD); styleInput(txtMaNV); styleInput(txtMaKH);
		styleInput(txtThue); styleInput(txtTongTruoc); styleInput(txtTongSau);
		cboMaKM.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));

		themDong(form, gbc, 0, "Mã hóa đơn",     txtMaHD);
		themDong(form, gbc, 1, "Nhân viên",        txtMaNV);
		themDong(form, gbc, 2, "Khách hàng",       txtMaKH);
		themDong(form, gbc, 3, "Thuế (VAT)",       txtThue);
		themDong(form, gbc, 4, "Khuyến mãi",       cboMaKM);
		themDong(form, gbc, 5, "Phương thức TT",   cboPhuongThuc);
		themDong(form, gbc, 6, "Tổng trước giảm",  txtTongTruoc);
		themDong(form, gbc, 7, "Tổng thanh toán",  txtTongSau);

		right.add(form, BorderLayout.CENTER);

		JPanel actions = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);
		JButton btnTinh = new JButton("Tính tiền");
		AppTheme.styleSecondaryButton(btnTinh);
		btnTinh.addActionListener(e -> tinhTongTien());
		JButton btnLap = new JButton("✔  Lập & Xác nhận hóa đơn");
		AppTheme.stylePrimaryButton(btnLap);
		btnLap.addActionListener(e -> lapHoaDon());
		actions.add(btnTinh);
		actions.add(btnLap);
		right.add(actions, BorderLayout.SOUTH);

		body.add(left,  BorderLayout.CENTER);
		body.add(right, BorderLayout.EAST);
		return body;
	}

	// ── Logic ───────────────────────────────────────────────────────────────

	/** Nạp danh sách vé CHO_THANH_TOAN từ DB */
	private void nạpVeChoThanhToan() {
		model.setRowCount(0);
		String sql = "SELECT v.maVeTau, kh.tenKH, ct.maCT, " +
				"toa.maToa, ctv.viTriGhe, " +
				"v.giaVe, v.trangThai, v.maKH " +
				"FROM VeTau v " +
				"JOIN KhachHang kh  ON kh.maKH   = v.maKH " +
				"JOIN ChuyenTau ct  ON ct.maCT    = v.maCT " +
				"JOIN Toa toa       ON toa.maToa  = v.maToa " +
				"LEFT JOIN ChiTietVeTau ctv ON ctv.maVeTau = v.maVeTau " +
				"WHERE v.trangThai = 'CHO_THANH_TOAN' " +
				"ORDER BY v.maVeTau";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				String toaGhe = rs.getString("maToa") + " / " +
						(rs.getString("viTriGhe") == null ? "-" : rs.getString("viTriGhe"));
				model.addRow(new Object[]{
						rs.getString("maVeTau"),
						rs.getString("tenKH"),
						rs.getString("maCT"),
						toaGhe,
						String.format("%,.0f", rs.getDouble("giaVe")),
						rs.getString("trangThai")
				});
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi tải danh sách vé: " + ex.getMessage());
		}
	}

	/** Nạp khuyến mãi còn hiệu lực */
	private void nạpKhuyenMai() {
		String sql = "SELECT maKM, tenKM, tyLeKM FROM KhuyenMai WHERE CAST(GETDATE() AS DATE) BETWEEN ngayBD AND ngayKT";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				String ma   = rs.getString("maKM");
				String ten  = rs.getString("tenKM");
				double tyle = rs.getDouble("tyLeKM");
				khuyenMaiMap.put(ma, BigDecimal.valueOf(tyle));
				cboMaKM.addItem(ma + " – " + ten + " (" + tyle + "%)");
			}
		} catch (SQLException ignored) {
		}
	}

	private void onVeSelected() {
		int row = tblVe.getSelectedRow();
		if (row < 0) return;
		selectedMaVe = model.getValueAt(row, 0).toString();
		// Lấy thêm maKH từ DB
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(
					 "SELECT maKH, giaVe FROM VeTau WHERE maVeTau = ?")) {
			ps.setString(1, selectedMaVe);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					selectedMaKH = rs.getString("maKH");
					txtMaKH.setText(selectedMaKH + "  (" + model.getValueAt(row, 1) + ")");
					txtTongTruoc.setText(String.format("%,.0f", rs.getDouble("giaVe")));
				}
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
		}
		tinhTongTien();
	}

	private void tinhTongTien() {
		if (selectedMaVe == null) return;
		try {
			String raw = txtTongTruoc.getText().replace(",", "").replace(" ", "");
			BigDecimal base = new BigDecimal(raw);

			// Thuế VAT 10%
			BigDecimal vat = base.multiply(new BigDecimal("0.10"));
			BigDecimal sau = base.add(vat);

			// Khuyến mãi
			String kmItem = cboMaKM.getSelectedItem() == null ? "" : cboMaKM.getSelectedItem().toString();
			if (!kmItem.startsWith("--")) {
				String maKM = kmItem.split(" – ")[0].trim();
				BigDecimal tyle = khuyenMaiMap.getOrDefault(maKM, BigDecimal.ZERO);
				BigDecimal giam = sau.multiply(tyle.divide(BigDecimal.valueOf(100)));
				sau = sau.subtract(giam);
			}
			sau = sau.setScale(0, RoundingMode.HALF_UP);
			txtTongSau.setText(String.format("%,.0f", sau));
		} catch (NumberFormatException ignored) {
		}
	}

	/** Ghi hóa đơn và cập nhật trạng thái vé → DA_THANH_TOAN */
	private void lapHoaDon() {
		if (selectedMaVe == null || selectedMaKH == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một vé trước khi lập hóa đơn.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String maHD  = txtMaHD.getText().trim();
		// Lấy mã nhân viên thật từ DB dựa trên username
		String maNV  = "NV001";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement("SELECT maNV FROM NhanVien WHERE username = ?")) {
			ps.setString(1, user != null ? user.getTenDangNhap() : "admin");
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) maNV = rs.getString("maNV");
			}
		} catch (SQLException ignored) {}

		String pttt  = cboPhuongThuc.getSelectedItem().toString();
		String kmItem = cboMaKM.getSelectedItem() == null ? "" : cboMaKM.getSelectedItem().toString();
		String maKM  = kmItem.startsWith("--") ? null : kmItem.split(" – ")[0].trim();

		int confirm = JOptionPane.showConfirmDialog(this,
				"Xác nhận lập hóa đơn " + maHD + " cho vé " + selectedMaVe + "?",
				"Xác nhận", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) return;

		String sqlHD = "INSERT INTO HoaDon (maHD, maNV, maKH, maKM, maThue, thoiGian, " +
				"phuongThucThanhToan, ngayThanhToan, trangThaiThanhToan) " +
				"VALUES (?, ?, ?, ?, 'T001', CAST(GETDATE() AS DATE), ?, CAST(GETDATE() AS DATE), 1)";
		String sqlCTHD = "INSERT INTO ChiTietHoaDon (maCTHD, maHD, maVeTau, maDV, soLuong, donGia) VALUES (?, ?, ?, NULL, 1, ?)";
		String sqlVe   = "UPDATE VeTau SET trangThai = 'DA_THANH_TOAN' WHERE maVeTau = ?";

		try (Connection conn = Database.getConnection()) {
			conn.setAutoCommit(false);
			try {
				// 1. Hóa đơn
				try (PreparedStatement ps = conn.prepareStatement(sqlHD)) {
					ps.setString(1, maHD);
					ps.setString(2, maNV);
					ps.setString(3, selectedMaKH);
					ps.setString(4, maKM);
					ps.setString(5, pttt);
					ps.executeUpdate();
				}
				// 2. Chi tiết hóa đơn
				String raw = txtTongTruoc.getText().replace(",", "").replace(" ", "");
				double donGia = Double.parseDouble(raw);
				String maCTHD = "CTHD" + maHD.replace("HD", "") + System.currentTimeMillis() % 1000;
				try (PreparedStatement ps = conn.prepareStatement(sqlCTHD)) {
					ps.setString(1, maCTHD);
					ps.setString(2, maHD);
					ps.setString(3, selectedMaVe);
					ps.setDouble(4, donGia);
					ps.executeUpdate();
				}
				// 3. Cập nhật trạng thái vé
				try (PreparedStatement ps = conn.prepareStatement(sqlVe)) {
					ps.setString(1, selectedMaVe);
					ps.executeUpdate();
				}
				conn.commit();
				JOptionPane.showMessageDialog(this,
						"✔  Lập hóa đơn thành công!\nMã HD: " + maHD + "\nTổng thanh toán: " + txtTongSau.getText() + " đ",
						"Thành công", JOptionPane.INFORMATION_MESSAGE);
				// Reset
				selectedMaVe = null; selectedMaKH = null;
				nạpVeChoThanhToan(); tạoMaHDMoi();
				txtMaKH.setText(""); txtTongTruoc.setText("0"); txtTongSau.setText("0");
			} catch (Exception ex) {
				conn.rollback();
				JOptionPane.showMessageDialog(this, "Lỗi ghi CSDL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Không kết nối được DB: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Tạo mã hóa đơn mới theo định dạng HD + YYYYMMDD + STT */
	private void tạoMaHDMoi() {
		String ngay = LocalDate.now().toString().replace("-", "");
		String sql  = "SELECT COUNT(*) AS cnt FROM HoaDon WHERE maHD LIKE ?";
		int stt = 1;
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, "HD" + ngay + "%");
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) stt = rs.getInt("cnt") + 1;
			}
		} catch (SQLException ignored) { }
		txtMaHD.setText(String.format("HD%s%02d", ngay, stt));
	}

	// ── Helpers ─────────────────────────────────────────────────────────────
	private void themDong(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
		gbc.gridy = row;
		gbc.gridx = 0; gbc.weightx = 0.36;
		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.font(Font.BOLD, 12));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);
		form.add(lbl, gbc);
		gbc.gridx = 1; gbc.weightx = 0.64;
		field.setPreferredSize(new Dimension(220, 34));
		form.add(field, gbc);
	}

	private void styleInput(JTextField field) {
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER),
				new EmptyBorder(6, 8, 6, 8)));
	}
}
