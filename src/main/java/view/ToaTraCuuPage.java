package view;

import controller.ToaController;
import entity.Toa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class ToaTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	private final ToaController toaController = new ToaController();
	private JTextField txtTimKiem;
	private JComboBox<String> cbLoaiToa;
	private JTable tableToa;
	private DefaultTableModel model;

	public ToaTraCuuPage() {
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F0F5F9"));
		setBorder(new EmptyBorder(14, 14, 14, 14));

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
		caiDatTimKiem();
		taiDuLieuBang();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(12, 14, 12, 14)));

		JLabel title = new JLabel("Tra cứu toa tàu");
		title.setFont(new Font("Segoe UI", Font.BOLD, 24));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);
		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 14));
		content.setBackground(Color.WHITE);
		content.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(14, 14, 14, 14)));
		content.add(taoBoLoc(), BorderLayout.NORTH);
		content.add(taoBang(), BorderLayout.CENTER);
		return content;
	}

	private JPanel taoBoLoc() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#DCE3EC")),
				new EmptyBorder(14, 14, 14, 14)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(taoLabel("Mã toa, loại toa"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		txtTimKiem = taoTextField();
		txtTimKiem.setToolTipText("Nhập mã toa hoặc loại toa để tìm kiếm");
		panel.add(txtTimKiem, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0;
		panel.add(taoLabel("Loại toa"), gbc);

		gbc.gridx = 3;
		cbLoaiToa = new JComboBox<>(new String[] {
				"Tất cả", "Ghế ngồi", "Giường nằm", "Giường nằm khoang VIP", "Toa hàng" });
		cbLoaiToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbLoaiToa.setPreferredSize(new Dimension(220, 36));
		cbLoaiToa.addActionListener(e -> taiDuLieuBang());
		panel.add(cbLoaiToa, gbc);

		return panel;
	}

	private JScrollPane taoBang() {
		String[] columns = { "#", "Mã toa", "Loại toa", "Số ghế", "Trạng thái" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableToa = new JTable(model);
		tableToa.setRowHeight(42);
		tableToa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableToa.setGridColor(Color.decode("#E4EBF3"));
		tableToa.getTableHeader().setBackground(MAU_CHINH);
		tableToa.getTableHeader().setForeground(Color.WHITE);
		tableToa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(tableToa);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#DCE3EC")));
		return scrollPane;
	}

	private JLabel taoLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 13));
		label.setForeground(Color.decode("#2B4B74"));
		return label;
	}

	private JTextField taoTextField() {
		JTextField field = new JTextField();
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setPreferredSize(new Dimension(0, 36));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
				new EmptyBorder(6, 8, 6, 8)));
		return field;
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
		model.setRowCount(0);
		String keyword = txtTimKiem == null ? "" : txtTimKiem.getText().trim().toLowerCase();
		String loaiLoc = cbLoaiToa == null ? "Tất cả" : String.valueOf(cbLoaiToa.getSelectedItem());

		List<Toa> ds = toaController.timKiemToa(null);
		int stt = 1;
		for (Toa toa : ds) {
			boolean khopTuKhoa = keyword.isEmpty()
					|| (toa.getMaToa() != null && toa.getMaToa().toLowerCase().contains(keyword))
					|| (toa.getLoaiToa() != null && toa.getLoaiToa().toLowerCase().contains(keyword));
			boolean khopLoai = "Tất cả".equals(loaiLoc) || loaiLoc.equals(toa.getLoaiToa());
			if (!khopTuKhoa || !khopLoai) {
				continue;
			}
			model.addRow(new Object[] {
					stt++,
					toa.getMaToa(),
					toa.getLoaiToa(),
					toa.getSoGhe(),
					toa.isTrangThai() ? "Sẵn sàng" : "Bảo trì"
			});
		}
	}
}
