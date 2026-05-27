package view;

import controller.ChuyenTauController;
import controller.TauController;
import controller.ToaController;
import dao.TuyenTau_DAO;
import entity.ChuyenTau;
import entity.Tau;
import entity.Toa;
import entity.TuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import service.ChuyenTauService.KetQuaXuLy;

public class ChuyenTauThemPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");
	private static final Dimension BUTTON_SIZE = new Dimension(150, 40);

	private final ChuyenTauController chuyenTauController = new ChuyenTauController();
	private final TauController tauController = new TauController();
	private final ToaController toaController = new ToaController();
	private final TuyenTau_DAO tuyenTauDAO = new TuyenTau_DAO();

	private JComboBox<TauOption> cbTau;
	private JComboBox<TuyenOption> cbTuyenTau;
	private JSpinner spnKhoiHanh;
	private JTextField txtTrangThai;
	private JTextField txtGioiHanKeo;
	private JTextField txtSoToaDaChon;
	private JTextField txtTongGheDuKien;
	private ToaChonPanel toaChonPanel;

	public ChuyenTauThemPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoNoiDung(), BorderLayout.CENTER);

		taiDanhSachTau();
		taiDanhSachTuyen();
		capNhatDanhSachToa();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Thêm chuyến tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		JLabel sub = new JLabel("Chọn đầu tàu, tuyến chạy và các toa đang rảnh cho chuyến mới.");
		sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		sub.setForeground(Color.decode("#6B7A90"));
		header.add(sub, BorderLayout.SOUTH);
		return header;
	}

	private JPanel taoNoiDung() {
		JPanel content = new JPanel(new BorderLayout(0, 14));
		content.setOpaque(false);
		content.add(taoFormThongTin(), BorderLayout.NORTH);
		content.add(taoPanelToa(), BorderLayout.CENTER);
		return content;
	}

	private JPanel taoFormThongTin() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(18, 18, 18, 18)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(taoLabel("Đầu tàu *"), gbc);
		gbc.gridx = 1;
		cbTau = new JComboBox<>();
		cbTau.setPreferredSize(new Dimension(290, 36));
		cbTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbTau.addActionListener(e -> capNhatDanhSachToa());
		panel.add(cbTau, gbc);

		gbc.gridx = 2;
		panel.add(taoLabel("Tuyến tàu *"), gbc);
		gbc.gridx = 3;
		cbTuyenTau = new JComboBox<>();
		cbTuyenTau.setPreferredSize(new Dimension(290, 36));
		cbTuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel.add(cbTuyenTau, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(taoLabel("Khởi hành *"), gbc);
		gbc.gridx = 1;
		spnKhoiHanh = taoDateTimeSpinner();
		spnKhoiHanh.addChangeListener(e -> capNhatDanhSachToa());
		panel.add(spnKhoiHanh, gbc);

		gbc.gridx = 2;
		panel.add(taoLabel("Trạng thái"), gbc);
		gbc.gridx = 3;
		txtTrangThai = taoFieldKhoa(ChuyenTau.HIEN_THI_DA_LEN_LICH);
		panel.add(txtTrangThai, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(taoLabel("Giới hạn kéo toa"), gbc);
		gbc.gridx = 1;
		txtGioiHanKeo = taoFieldKhoa("0 toa");
		panel.add(txtGioiHanKeo, gbc);

		gbc.gridx = 2;
		panel.add(taoLabel("Số toa đã chọn"), gbc);
		gbc.gridx = 3;
		txtSoToaDaChon = taoFieldKhoa("0 toa");
		panel.add(txtSoToaDaChon, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(taoLabel("Tổng ghế dự kiến"), gbc);
		gbc.gridx = 1;
		txtTongGheDuKien = taoFieldKhoa("0 ghế");
		panel.add(txtTongGheDuKien, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 4;
		JLabel note = new JLabel("Màu xanh: toa rảnh có thể gán. Màu đỏ: toa đã được lên lịch hoặc đang chạy.");
		note.setFont(new Font("Segoe UI", Font.ITALIC, 13));
		note.setForeground(Color.decode("#6B7A90"));
		panel.add(note, gbc);

		gbc.gridy = 5;
		JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
		buttons.setOpaque(false);
		buttons.add(taoNutChinh("Tạo chuyến", e -> xuLyThemChuyenTau()));
		buttons.add(taoNutPhu("Làm mới", e -> lamMoiForm()));
		panel.add(buttons, gbc);

		return panel;
	}

	private JScrollPane taoPanelToa() {
		toaChonPanel = new ToaChonPanel();
		toaChonPanel.setTieuDe("Chọn toa cho chuyến tàu");
		toaChonPanel.setSelectionListener(ignored -> capNhatThongTinTongHop());
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(Color.WHITE);
		wrapper.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(18, 18, 18, 18)));
		wrapper.add(toaChonPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(wrapper);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		return scrollPane;
	}

	private JLabel taoLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		label.setForeground(Color.decode("#2B4B74"));
		return label;
	}

	private JTextField taoFieldKhoa(String text) {
		JTextField field = new JTextField(text);
		field.setEditable(false);
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBackground(Color.decode("#F6F8FB"));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(8, 8, 8, 8)));
		return field;
	}

	private JSpinner taoDateTimeSpinner() {
		JSpinner spinner = new JSpinner(new SpinnerDateModel());
		spinner.setValue(new Date());
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy HH:mm");
		spinner.setEditor(editor);
		spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		spinner.setPreferredSize(new Dimension(290, 36));
		return spinner;
	}

	private JButton taoNutChinh(String text, java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setBackground(MAU_CHINH);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(new EmptyBorder(9, 22, 9, 22));
		button.setPreferredSize(BUTTON_SIZE);
		button.addActionListener(action);
		return button;
	}

	private JButton taoNutPhu(String text, java.awt.event.ActionListener action) {
		JButton button = new JButton(text);
		button.setBackground(Color.WHITE);
		button.setForeground(Color.decode("#2B4B74"));
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(9, 22, 9, 22)));
		button.setPreferredSize(BUTTON_SIZE);
		button.addActionListener(action);
		return button;
	}

	private void taiDanhSachTau() {
		cbTau.removeAllItems();
		cbTau.addItem(new TauOption(null, "-- Chọn đầu tàu --", 0));
		for (Tau tau : tauController.timKiemTau(null, null)) {
			cbTau.addItem(new TauOption(tau.getMaTau(), tau.getTenTau(), tau.getSoLuongToa()));
		}
	}

	private void taiDanhSachTuyen() {
		cbTuyenTau.removeAllItems();
		cbTuyenTau.addItem(new TuyenOption(null, "-- Chọn ga đi --", "-- Chọn ga đến --", 0));
		for (TuyenTau tuyenTau : tuyenTauDAO.layTatCaTuyenTau()) {
			cbTuyenTau.addItem(new TuyenOption(tuyenTau.getMaTT(), tuyenTau.getMaGaDi(), tuyenTau.getMaGaDen(),
					tuyenTau.getKhoangCach()));
		}
	}

	private void capNhatDanhSachToa() {
		TauOption tau = (TauOption) cbTau.getSelectedItem();
		int gioiHan = tau == null ? 0 : tau.soLuongToa;
		txtGioiHanKeo.setText(gioiHan + " toa");
		toaChonPanel.setGioiHanChon(gioiHan);

		LocalDateTime thoiGian = layThoiGianKhoiHanh();
		Set<String> availableIds = new LinkedHashSet<>();
		for (Toa toa : toaController.layToaRanhTheoThoiDiem(thoiGian, null)) {
			availableIds.add(toa.getMaToa());
		}

		List<ToaChonPanel.CoachCardData> cardData = new ArrayList<>();
		for (Toa toa : toaController.timKiemToa(null)) {
			boolean available = availableIds.contains(toa.getMaToa());
			String moTa = available ? "Sẵn sàng" : "Đã xếp chuyến";
			cardData.add(new ToaChonPanel.CoachCardData(toa.getMaToa(), toa.getLoaiToa(), toa.getSoGhe(), moTa, available));
		}
		toaChonPanel.setDanhSachToa(cardData);
		capNhatThongTinTongHop();
	}

	private void capNhatThongTinTongHop() {
		Set<String> daChon = toaChonPanel.getSelectedToa();
		txtSoToaDaChon.setText(daChon.size() + " toa");
		int tongGhe = 0;
		for (String maToa : daChon) {
			Toa toa = toaController.timKiemToa(maToa).stream().findFirst().orElse(null);
			if (toa != null) {
				tongGhe += toa.getSoGhe();
			}
		}
		txtTongGheDuKien.setText(tongGhe + " ghế");
	}

	private LocalDateTime layThoiGianKhoiHanh() {
		Date date = (Date) spnKhoiHanh.getValue();
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	private void xuLyThemChuyenTau() {
		TauOption tau = (TauOption) cbTau.getSelectedItem();
		TuyenOption tuyen = (TuyenOption) cbTuyenTau.getSelectedItem();
		if (tau == null || tau.maTau == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn đầu tàu.");
			return;
		}
		if (tuyen == null || tuyen.maTT == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn tuyến tàu.");
			return;
		}

		List<String> maToaList = new ArrayList<>(toaChonPanel.getSelectedToa());
		if (maToaList.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một toa rảnh cho chuyến.");
			return;
		}

		KetQuaXuLy ketQua = chuyenTauController.themChuyenTau(tau.maTau, tuyen.maTT, layThoiGianKhoiHanh(), maToaList);
		JOptionPane.showMessageDialog(this,
				ketQua.thongBao + (ketQua.thanhCong ? " (" + ketQua.maThamChieu + ")" : ""),
				ketQua.thanhCong ? "Thành công" : "Lỗi",
				ketQua.thanhCong ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
		if (ketQua.thanhCong) {
			lamMoiForm();
		}
	}

	private void lamMoiForm() {
		if (cbTau.getItemCount() > 0) {
			cbTau.setSelectedIndex(0);
		}
		if (cbTuyenTau.getItemCount() > 0) {
			cbTuyenTau.setSelectedIndex(0);
		}
		spnKhoiHanh.setValue(new Date());
		txtTrangThai.setText(ChuyenTau.HIEN_THI_DA_LEN_LICH);
		capNhatDanhSachToa();
	}
}
