package view;

import controller.ChuyenTauController;
import entity.ChuyenTau;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class ChuyenTauTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#4682A9");

	private JTextField txtTimKiem;
	private JComboBox<String> cbSapXep;
	private JTable tableChuyenTau;
	private DefaultTableModel model;
	private final ChuyenTauController chuyenTauController = new ChuyenTauController();

	public ChuyenTauTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoSearchPanel(), BorderLayout.WEST);
		add(taoTablePanel(), BorderLayout.CENTER);
		caiDatTimKiem();
		taiDuLieuBang();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		JLabel title = new JLabel("Tra cứu chuyến tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoSearchPanel() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));
		searchPanel.setPreferredSize(new Dimension(250, 200));

		JLabel lblTimKiem = new JLabel("Tìm kiếm:");
		lblTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
		searchPanel.add(lblTimKiem);

		txtTimKiem = new JTextField();
		txtTimKiem.setPreferredSize(new Dimension(200, 32));
		txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
			new EmptyBorder(6, 8, 6, 8)
		));
		searchPanel.add(txtTimKiem);

		JLabel lblSapXep = new JLabel("Sắp xếp:");
		lblSapXep.setFont(new Font("Segoe UI", Font.BOLD, 12));
		searchPanel.add(lblSapXep);

		cbSapXep = new JComboBox<>();
		cbSapXep.addItem("Tất cả trạng thái");
		cbSapXep.addItem("Lên lịch");
		cbSapXep.addItem("Đang chạy");
		cbSapXep.addItem("Hoàn thành");
		cbSapXep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		cbSapXep.setPreferredSize(new Dimension(200, 32));
		searchPanel.add(cbSapXep);

		return searchPanel;
	}

	private JPanel taoTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
			new EmptyBorder(12, 14, 12, 14)
		));

		String[] columns = { "STT", "Mã chuyến", "Tàu", "Ga đi", "Ga đến", "Khởi hành", "Đến nơi", "Trạng thái"};
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableChuyenTau = new JTable(model);
		tableChuyenTau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableChuyenTau.setRowHeight(32);
		tableChuyenTau.setShowGrid(true);
		tableChuyenTau.setGridColor(new Color(220, 220, 220));
		tableChuyenTau.setBackground(Color.WHITE);
		tableChuyenTau.getTableHeader().setBackground(MAU_CHINH);
		tableChuyenTau.getTableHeader().setForeground(Color.WHITE);
		tableChuyenTau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
		tableChuyenTau.getColumnModel().getColumn(7).setCellRenderer(new ChuyenTauBadgeRenderer());

		JScrollPane scrollPane = new JScrollPane(tableChuyenTau);
		scrollPane.setPreferredSize(new Dimension(1000, 400));
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		return tablePanel;
	}

	private void caiDatTimKiem() {
		txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				taiDuLieuBang();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				taiDuLieuBang();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				taiDuLieuBang();
			}
		});
	}

	private void taiDuLieuBang() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		String keyword = txtTimKiem == null ? null : txtTimKiem.getText();
		List<ChuyenTau> ds = chuyenTauController.timKiemChuyenTau(keyword);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		int stt = 1;
		for (ChuyenTau ct : ds) {
			String ngayGio = ct.getNgayKhoiHanh() == null ? "" : ct.getNgayKhoiHanh().format(dtf);
			String gaDi = "";
			String gaDen = "";
			try {
				dao.TuyenTau_DAO dao = new dao.TuyenTau_DAO();
				entity.TuyenTau tt = dao.timTheoMa(ct.getMaTuyenTau());
				if (tt != null) {
					gaDi = tt.getMaGaDi() == null ? "" : tt.getMaGaDi();
					gaDen = tt.getMaGaDen() == null ? "" : tt.getMaGaDen();
				}
			} catch (Exception ignore) {
			}
			model.addRow(new Object[] { stt++, ct.getMaCT(), ct.getMaTau(), gaDi, gaDen, ngayGio, "", ct.isTrangThai() ? "Hoạt động" : "Ngừng" });
		}
	}

	private static class ChuyenTauBadgeRenderer extends javax.swing.table.DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			javax.swing.JLabel label = new javax.swing.JLabel(String.valueOf(value), javax.swing.JLabel.CENTER);
			label.setOpaque(true);
			label.setFont(new Font("Segoe UI", Font.BOLD, 12));
			label.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 6, 2, 6));

			String val = String.valueOf(value);
			if ("Hoạt động".equals(val)) {
				label.setBackground(Color.decode("#D1FAE5")); // Light green
				label.setForeground(Color.decode("#047857")); // Dark green
			} else if ("Ngừng".equals(val)) {
				label.setBackground(Color.decode("#FEE2E2")); // Light red
				label.setForeground(Color.decode("#991B1B")); // Dark red
			} else {
				label.setBackground(Color.WHITE);
				label.setForeground(Color.decode("#1E2D3D"));
			}

			if (isSelected) {
				label.setBackground(label.getBackground().darker());
			}
			return label;
		}
	}
}
