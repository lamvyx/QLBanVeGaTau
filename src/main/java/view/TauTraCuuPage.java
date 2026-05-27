package view;

import controller.TauController;
import entity.Tau;
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

public class TauTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = Color.decode("#2A5ACB");

	private final TauController tauController = new TauController();
	private JTextField txtTimKiem;
	private JComboBox<String> cbLoc;
	private JTable tableTau;
	private DefaultTableModel model;

	public TauTraCuuPage() {
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

		JLabel title = new JLabel("Tra cứu đầu tàu");
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
		panel.add(taoLabel("Mã tàu, tên tàu"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		txtTimKiem = taoTextField();
		txtTimKiem.setToolTipText("Nhập mã tàu hoặc tên tàu để tìm kiếm");
		panel.add(txtTimKiem, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0;
		panel.add(taoLabel("Lọc theo"), gbc);

		gbc.gridx = 3;
		cbLoc = new JComboBox<>(new String[] { "Tất cả", "Mã tàu", "Tên tàu" });
		cbLoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbLoc.setPreferredSize(new Dimension(190, 36));
		cbLoc.addActionListener(e -> taiDuLieuBang());
		panel.add(cbLoc, gbc);

		return panel;
	}

	private JScrollPane taoBang() {
		String[] columns = { "#", "Mã tàu", "Tên đầu tàu", "Số toa kéo tối đa" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableTau = new JTable(model);
		tableTau.setRowHeight(42);
		tableTau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableTau.setGridColor(Color.decode("#E4EBF3"));
		tableTau.getTableHeader().setBackground(MAU_CHINH);
		tableTau.getTableHeader().setForeground(Color.WHITE);
		tableTau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(tableTau);
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
		String keyword = txtTimKiem == null ? "" : txtTimKiem.getText().trim();
		String loc = cbLoc == null ? "Tất cả" : String.valueOf(cbLoc.getSelectedItem());

		List<Tau> ds;
		if (keyword.isEmpty()) {
			ds = tauController.timKiemTau(null, null);
		} else if ("Mã tàu".equals(loc)) {
			ds = tauController.timKiemTau(keyword, null);
		} else if ("Tên tàu".equals(loc)) {
			ds = tauController.timKiemTau(null, keyword);
		} else {
			ds = tauController.timKiemTau(null, keyword);
			List<Tau> theoMa = tauController.timKiemTau(keyword, null);
			for (Tau tau : theoMa) {
				if (ds.stream().noneMatch(item -> item.getMaTau().equals(tau.getMaTau()))) {
					ds.add(tau);
				}
			}
		}

		for (int i = 0; i < ds.size(); i++) {
			Tau tau = ds.get(i);
			model.addRow(new Object[] { i + 1, tau.getMaTau(), tau.getTenTau(), tau.getSoLuongToa() });
		}
	}
}
