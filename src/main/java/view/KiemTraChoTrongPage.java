package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import controller.ChuyenTauController;
import controller.HoaDonController;
import controller.ToaController;
import entity.ChuyenTau;
import entity.Toa;

public class KiemTraChoTrongPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_TEXT = AppTheme.TEXT_PRIMARY;
	private static final Color MAU_XANH = Color.decode("#22C55E");
	private static final Color MAU_DO = Color.decode("#EF4444");
	private static final Color MAU_DANG_CHON = Color.decode("#3B82F6");

	private final JLabel lblSoChoTrong = new JLabel("0");
	private final JLabel lblSoChoDat = new JLabel("0");
	private final JLabel lblSoChoDangChon = new JLabel("0");
	private final JLabel lblToaDangXem = new JLabel("Chưa chọn toa");
	private final JLabel lblNgayChieu = new JLabel("-");
	private final JLabel lblGioChieu = new JLabel("-");
	private final JLabel lblTrangThai = new JLabel("Vui lòng chọn chuyến tàu và toa");
	
	// Thống kê toàn chuyến
	private final JLabel lblTongGheChuyen = new JLabel("0");
	private final JLabel lblChoDaDatChuyen = new JLabel("0");
	private final JLabel lblTiLeTrong = new JLabel("0%");

	private JComboBox<ChuyenTauOption> cboChuyenTau;
	private JComboBox<ToaOption> cboToaTau;
	private JPanel seatGridContainer;
	
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final ToaController toaController = new ToaController();
	private final HoaDonController hoaDonController = new HoaDonController();
	
	private final List<ChuyenTauOption> dsChuyenTau = new ArrayList<>();
	private final Map<String, List<ToaOption>> toaTheoChuyen = new HashMap<>();
	private final Set<String> bookedSeats = new LinkedHashSet<>();
	
	private String selectedMaCT;
	private String selectedMaToa;
	private int currentSeatCount = 0;
	private String selectedSeat = "";

	public KiemTraChoTrongPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
		taiDuLieuBanDau();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.CARD_BG);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
		header.setPreferredSize(new Dimension(0, 62));

		JLabel title = new JLabel("Kiểm tra chỗ trống");
		title.setFont(AppTheme.font(Font.BOLD, 20));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		title.setBorder(new EmptyBorder(0, 16, 0, 0));
		header.add(title, BorderLayout.WEST);

		JLabel subtitle = new JLabel("Xem sơ đồ ghế còn trống theo chuyến tàu và toa");
		subtitle.setFont(AppTheme.font(Font.PLAIN, 12));
		subtitle.setForeground(AppTheme.TEXT_MUTED);
		subtitle.setBorder(new EmptyBorder(0, 0, 0, 16));
		header.add(subtitle, BorderLayout.EAST);
		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(12, 12));
		content.setOpaque(false);
		content.setBorder(new EmptyBorder(12, 12, 12, 12));
		content.add(taoBoLoc(), BorderLayout.NORTH);
		content.add(taoThanChinh(), BorderLayout.CENTER);
		return content;
	}

	private JPanel taoBoLoc() {
		JPanel filter = new JPanel(new GridLayout(1, 2, 12, 12));
		filter.setOpaque(false);
		filter.add(createFilterCard());
		filter.add(createSummaryCard());
		return filter;
	}

	private JPanel createFilterCard() {
		JPanel card = new JPanel(new GridBagLayout());
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;

		cboChuyenTau = new JComboBox<>();
		cboChuyenTau.addActionListener(e -> {
			ChuyenTauOption ct = (ChuyenTauOption) cboChuyenTau.getSelectedItem();
			if (ct != null) {
				selectedMaCT = ct.maCT;
				lblNgayChieu.setText(ct.thoiGianKhoiHanh.split(" ")[0]);
				lblGioChieu.setText(ct.thoiGianKhoiHanh.split(" ").length > 1 ? ct.thoiGianKhoiHanh.split(" ")[1] : "-");
				taiToaTheoChuyen(ct);
			}
		});
		addField(card, gbc, 0, 0, "Chuyến tàu", cboChuyenTau);

		cboToaTau = new JComboBox<>();
		cboToaTau.addActionListener(e -> {
			ToaOption toa = (ToaOption) cboToaTau.getSelectedItem();
			if (toa != null) {
				selectedMaToa = toa.maToa;
				currentSeatCount = toa.soGhe;
				lblToaDangXem.setText(toa.toString());
				capNhatGheDaDat();
				refreshSeatGrid();
			}
		});
		addField(card, gbc, 1, 0, "Toa tàu", cboToaTau);
		addField(card, gbc, 0, 1, "Ngày chạy", new JComboBox<>(new String[] {
			LocalDate.now().toString(),
			"2026-04-03",
			"2026-04-04"
		}));
		JPanel actionWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		actionWrap.setOpaque(false);
		JButton btnTim = new JButton("Kiểm tra");
		AppTheme.stylePrimaryButton(btnTim);
		btnTim.addActionListener(e -> capNhatTrangThai());
		JButton btnMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnMoi);
		btnMoi.addActionListener(e -> resetSeats());
		actionWrap.add(btnTim);
		actionWrap.add(btnMoi);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		card.add(actionWrap, gbc);
		return card;
	}

	private void addField(JPanel parent, GridBagConstraints gbc, int row, int col, String label, java.awt.Component field) {
		int base = col * 2;
		gbc.gridx = base;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.font(Font.BOLD, 13));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);
		parent.add(lbl, gbc);

		gbc.gridx = base + 1;
		gbc.weightx = 0.3;
		field.setFont(AppTheme.font(Font.PLAIN, 13));
		field.setPreferredSize(new Dimension(220, 30));
		parent.add(field, gbc);
	}

	private JPanel createSummaryCard() {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Tổng hợp chỗ trống");
		title.setFont(AppTheme.font(Font.BOLD, 15));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		card.add(title, BorderLayout.NORTH);

		JPanel statGrid = new JPanel(new GridLayout(1, 3, 8, 8));
		statGrid.setOpaque(false);
		statGrid.add(createMiniStat("Trống", lblSoChoTrong, MAU_XANH));
		statGrid.add(createMiniStat("Đã đặt", lblSoChoDat, MAU_DO));
		statGrid.add(createMiniStat("Đang chọn", lblSoChoDangChon, MAU_DANG_CHON));
		card.add(statGrid, BorderLayout.CENTER);

		JPanel trainStats = new JPanel(new GridLayout(3, 1, 4, 4));
		trainStats.setOpaque(false);
		trainStats.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		trainStats.add(createTrainStatRow("Tổng ghế toàn tàu:", lblTongGheChuyen));
		trainStats.add(createTrainStatRow("Tổng ghế đã đặt:", lblChoDaDatChuyen));
		trainStats.add(createTrainStatRow("Tỷ lệ trống:", lblTiLeTrong));
		
		JPanel info = new JPanel(new GridBagLayout());
		info.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 0, 4, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		addInfo(info, gbc, 0, "Toa:", lblToaDangXem);
		addInfo(info, gbc, 1, "Ngày:", lblNgayChieu);
		addInfo(info, gbc, 2, "Giờ:", lblGioChieu);
		addInfo(info, gbc, 3, "Trạng thái:", lblTrangThai);
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setOpaque(false);
		southPanel.add(trainStats, BorderLayout.NORTH);
		southPanel.add(info, BorderLayout.CENTER);
		
		card.add(southPanel, BorderLayout.SOUTH);
		return card;
	}

	private JPanel createTrainStatRow(String label, JLabel value) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.font(Font.PLAIN, 12));
		lbl.setForeground(AppTheme.TEXT_MUTED);
		row.add(lbl, BorderLayout.WEST);
		value.setFont(AppTheme.font(Font.BOLD, 12));
		value.setForeground(AppTheme.TEXT_PRIMARY);
		row.add(value, BorderLayout.EAST);
		return row;
	}

	private JPanel createMiniStat(String label, JLabel value, Color color) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(248, 250, 252));
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#E4EBF3")),
			new EmptyBorder(10, 10, 10, 10)
		));
		JLabel title = new JLabel(label, SwingConstants.CENTER);
		title.setFont(AppTheme.font(Font.PLAIN, 12));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		JLabel val = value;
		val.setHorizontalAlignment(SwingConstants.CENTER);
		val.setFont(AppTheme.font(Font.BOLD, 20));
		val.setForeground(color);
		panel.add(title, BorderLayout.NORTH);
		panel.add(val, BorderLayout.CENTER);
		return panel;
	}

	private void addInfo(JPanel parent, GridBagConstraints gbc, int row, String label, JLabel value) {
		gbc.gridx = 0;
		gbc.gridy = row;
		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.font(Font.PLAIN, 12));
		lbl.setForeground(AppTheme.TEXT_PRIMARY);
		parent.add(lbl, gbc);
		gbc.gridx = 1;
		value.setFont(AppTheme.font(Font.BOLD, 12));
		value.setForeground(AppTheme.TEXT_PRIMARY);
		value.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(value, gbc);
	}

	private JPanel taoThanChinh() {
		JPanel main = new JPanel(new GridLayout(1, 2, 12, 12));
		main.setOpaque(false);
		main.add(createSeatCard());
		main.add(createDetailCard());
		return main;
	}

	private JPanel createSeatCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Sơ đồ ghế - Toa 2 (Ghế mềm)");
		title.setFont(AppTheme.font(Font.BOLD, 15));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		card.add(title, BorderLayout.NORTH);

		JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
		legend.setOpaque(false);
		legend.add(legendItem(MAU_XANH, "Trống"));
		legend.add(legendItem(MAU_DO, "Đã đặt"));
		legend.add(legendItem(MAU_DANG_CHON, "Đang chọn"));
		card.add(legend, BorderLayout.BEFORE_FIRST_LINE);

		JPanel grid = new JPanel(new BorderLayout(0, 10));
		grid.setOpaque(false);
		grid.setPreferredSize(new Dimension(0, 300));
		seatGridContainer = grid;
		grid.add(createSeatGrid(), BorderLayout.CENTER);
		JLabel hint = new JLabel("Ghế xanh là trống, ghế đỏ đã có người đặt");
		hint.setFont(AppTheme.font(Font.ITALIC, 12));
		hint.setForeground(AppTheme.TEXT_MUTED);
		hint.setHorizontalAlignment(SwingConstants.CENTER);
		grid.add(hint, BorderLayout.SOUTH);
		card.add(grid, BorderLayout.CENTER);
		return card;
	}

	private JPanel legendItem(Color color, String label) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		panel.setOpaque(false);
		JPanel box = new JPanel();
		box.setPreferredSize(new Dimension(14, 14));
		box.setBackground(color);
		box.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
		panel.add(box);
		JLabel text = new JLabel(label);
		text.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		text.setForeground(MAU_TEXT);
		panel.add(text);
		return panel;
	}

	private JPanel createSeatGrid() {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setOpaque(false);
		
		int colsCount = 4;
		int rowsCount = Math.max(1, (int) Math.ceil((double) currentSeatCount / colsCount));
		
		int index = 0;
		JPanel seats = new JPanel(new GridLayout(rowsCount, colsCount, 8, 8));
		seats.setOpaque(false);
		
		for (int r = 0; r < rowsCount; r++) {
			for (int c = 0; c < colsCount; c++) {
				if (index >= currentSeatCount) break;
				
				String seatCode = String.valueOf((char) ('A' + c)) + String.format("%02d", r + 1);
				JButton seat = new JButton(seatCode);
				seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
				seat.setFocusPainted(false);
				seat.setPreferredSize(new Dimension(60, 40));
				
				String finalCode = chuanHoaMaGhe(seatCode);
				if (bookedSeats.contains(finalCode)) {
					seat.setBackground(new Color(255, 235, 235));
					seat.setForeground(MAU_DO);
					seat.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
				} else {
					seat.setBackground(Color.WHITE);
					seat.setForeground(MAU_TEXT);
					seat.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
				}
				
				seat.addActionListener(e -> {
					if (!bookedSeats.contains(finalCode)) {
						selectedSeat = seatCode;
						lblTrangThai.setText("Ghế " + selectedSeat + " đang khả dụng");
						lblSoChoDangChon.setText("1");
						// Reset other selections
						for (java.awt.Component comp : seats.getComponents()) {
							if (comp instanceof JButton btn) {
								String btnCode = chuanHoaMaGhe(btn.getText());
								if (!bookedSeats.contains(btnCode)) {
									btn.setBackground(Color.WHITE);
									btn.setForeground(MAU_TEXT);
									btn.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
								}
							}
						}
						seat.setBackground(MAU_DANG_CHON);
						seat.setForeground(Color.WHITE);
						seat.setBorder(BorderFactory.createLineBorder(MAU_DANG_CHON.darker(), 2));
					}
				});
				seats.add(seat);
				index++;
			}
		}
		
		wrap.add(new JScrollPane(seats), BorderLayout.CENTER);
		return wrap;
	}

	private void taiDuLieuBanDau() {
		dsChuyenTau.clear();
		for (ChuyenTau ct : chuyenTauController.timKiemChuyenTau("")) {
			dsChuyenTau.add(new ChuyenTauOption(ct.getMaCT(), ct.getMaTau(), ct.getMaTuyenTau(), ct.getNgayKhoiHanh().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
		}
		cboChuyenTau.removeAllItems();
		for (ChuyenTauOption opt : dsChuyenTau) {
			cboChuyenTau.addItem(opt);
		}

		toaTheoChuyen.clear();
		List<Toa> dsToa = toaController.timKiemToa(null);
		for (Toa toa : dsToa) {
			toaTheoChuyen.computeIfAbsent(toa.getMaTau(), k -> new ArrayList<>()).add(new ToaOption(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe()));
		}
		
		if (cboChuyenTau.getItemCount() > 0) {
			cboChuyenTau.setSelectedIndex(0);
		}
	}

	private void taiToaTheoChuyen(ChuyenTauOption ct) {
		cboToaTau.removeAllItems();
		List<ToaOption> toas = toaTheoChuyen.getOrDefault(ct.maTau, Collections.emptyList());
		for (ToaOption toa : toas) {
			cboToaTau.addItem(toa);
		}
		if (cboToaTau.getItemCount() > 0) {
			cboToaTau.setSelectedIndex(0);
		}
	}

	private void capNhatGheDaDat() {
		bookedSeats.clear();
		if (selectedMaCT != null && selectedMaToa != null) {
			Set<String> daDat = hoaDonController.layGheDaDat(selectedMaCT, selectedMaToa);
			for (String s : daDat) {
				bookedSeats.add(chuanHoaMaGhe(s));
			}
		}
		lblSoChoDat.setText(String.valueOf(bookedSeats.size()));
		lblSoChoTrong.setText(String.valueOf(currentSeatCount - bookedSeats.size()));
		lblSoChoDangChon.setText("0");
		lblTrangThai.setText("Đang xem " + currentSeatCount + " ghế của " + selectedMaToa);
		
		capNhatThongKeToanChuyen();
	}

	private String chuanHoaMaGhe(String raw) {
		if (raw == null) return "";
		String value = raw.trim().toUpperCase();
		// Format ColumnRow (A01, B05...)
		if (value.matches("[A-Z]\\d+")) {
			char cot = value.charAt(0);
			int row = Integer.parseInt(value.substring(1));
			return cot + String.format("%02d", row);
		}
		// Format RowColumn (5A, 12B...)
		if (value.matches("\\d+[A-Z]")) {
			char cot = value.charAt(value.length() - 1);
			int row = Integer.parseInt(value.substring(0, value.length() - 1));
			return cot + String.format("%02d", row);
		}
		// Standard numeric (07, 15...)
		if (value.matches("\\d+")) {
			return String.format("%02d", Integer.parseInt(value));
		}
		return value;
	}

	private void capNhatThongKeToanChuyen() {
		ChuyenTauOption ct = (ChuyenTauOption) cboChuyenTau.getSelectedItem();
		if (ct == null) return;
		
		int tongGhe = 0;
		int tongDaDat = 0;
		
		List<ToaOption> dsToa = toaTheoChuyen.getOrDefault(ct.maTau, Collections.emptyList());
		for (ToaOption toa : dsToa) {
			tongGhe += toa.soGhe;
			Set<String> daDat = hoaDonController.layGheDaDat(ct.maCT, toa.maToa);
			tongDaDat += daDat.size();
		}
		
		lblTongGheChuyen.setText(String.valueOf(tongGhe));
		lblChoDaDatChuyen.setText(String.valueOf(tongDaDat));
		if (tongGhe > 0) {
			double phanTramTrong = ((double)(tongGhe - tongDaDat) / tongGhe) * 100;
			lblTiLeTrong.setText(String.format("%.1f%%", phanTramTrong));
		} else {
			lblTiLeTrong.setText("0%");
		}
	}

	private void refreshSeatGrid() {
		if (seatGridContainer != null) {
			seatGridContainer.removeAll();
			seatGridContainer.add(createSeatGrid(), BorderLayout.CENTER);
			seatGridContainer.revalidate();
			seatGridContainer.repaint();
		}
	}

	private static class ChuyenTauOption {
		String maCT, maTau, maTuyen, thoiGianKhoiHanh;
		ChuyenTauOption(String maCT, String maTau, String maTuyen, String thoiGian) {
			this.maCT = maCT; this.maTau = maTau; this.maTuyen = maTuyen; this.thoiGianKhoiHanh = thoiGian;
		}
		@Override public String toString() { return maCT + " (" + thoiGianKhoiHanh + ")"; }
	}

	private static class ToaOption {
		String maToa, loaiToa; int soGhe;
		ToaOption(String maToa, String loai, int ghe) {
			this.maToa = maToa; this.loaiToa = loai; this.soGhe = ghe;
		}
		@Override public String toString() { return maToa + " - " + loaiToa + " (" + soGhe + " ghế)"; }
	}

	private JPanel createDetailCard() {
		JPanel card = new JPanel(new BorderLayout(0, 12));
		card.setBackground(AppTheme.CARD_BG);
		card.setBorder(AppTheme.cardBorder());

		JLabel title = new JLabel("Chi tiết ghế trống");
		title.setFont(AppTheme.font(Font.BOLD, 15));
		title.setForeground(AppTheme.TEXT_PRIMARY);
		card.add(title, BorderLayout.NORTH);

		JPanel detail = new JPanel(new GridBagLayout());
		detail.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 0, 8, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		addDetailRow(detail, gbc, 0, "Toa đang xem", lblToaDangXem);
		addDetailRow(detail, gbc, 1, "Ngày", lblNgayChieu);
		addDetailRow(detail, gbc, 2, "Giờ", lblGioChieu);
		addDetailRow(detail, gbc, 3, "Trạng thái", lblTrangThai);
		addDetailRow(detail, gbc, 4, "Ghế trống", lblSoChoTrong);
		addDetailRow(detail, gbc, 5, "Ghế đã đặt", lblSoChoDat);
		addDetailRow(detail, gbc, 6, "Ghế đang chọn", lblSoChoDangChon);
		card.add(detail, BorderLayout.CENTER);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		actions.setOpaque(false);
		JButton btnGoiY = new JButton("Gợi ý ghế khác");
		btnGoiY.setBackground(Color.WHITE);
		btnGoiY.setForeground(MAU_TEXT);
		btnGoiY.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnGoiY.setFocusPainted(false);
		btnGoiY.setBorder(BorderFactory.createLineBorder(Color.decode("#C8D6E5")));
		btnGoiY.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btnGoiY.addActionListener(e -> lblTrangThai.setText("Gợi ý ghế trống đang được hiển thị"));
		actions.add(btnGoiY);
		card.add(actions, BorderLayout.SOUTH);
		return card;
	}

	private void addDetailRow(JPanel parent, GridBagConstraints gbc, int row, String label, JLabel value) {
		gbc.gridx = 0;
		gbc.gridy = row;
		JLabel lbl = new JLabel(label + ":");
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lbl.setForeground(MAU_TEXT);
		parent.add(lbl, gbc);
		gbc.gridx = 1;
		value.setFont(new Font("Segoe UI", Font.BOLD, 12));
		value.setForeground(MAU_TEXT);
		value.setHorizontalAlignment(SwingConstants.RIGHT);
		parent.add(value, gbc);
	}

	private void capNhatTrangThai() {
		capNhatGheDaDat();
		refreshSeatGrid();
		lblTrangThai.setText("Cập nhật dữ liệu lúc: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
	}

	private void resetSeats() {
		lblSoChoTrong.setText("26");
		lblSoChoDat.setText("14");
		lblSoChoDangChon.setText("1");
		lblTrangThai.setText("Đang kiểm tra chỗ trống");
		selectedSeat = "E03";
	}
}
