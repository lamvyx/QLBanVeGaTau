package view;

import connectDB.Database;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class KhachHangTraCuuPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;

	private DefaultTableModel model;
	private JTable table;
	private JTextField txtTimKiem;

	public KhachHangTraCuuPage() {
		setLayout(new BorderLayout(0, 12));
		setBackground(AppTheme.PAGE_BG);
		setBorder(AppTheme.pagePadding());

		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
		napDuLieuTuDB();
	}

	private JPanel taoHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);

		JLabel title = new JLabel("Tra cứu khách hàng");
		title.setFont(AppTheme.font(Font.BOLD, 30));
		title.setForeground(MAU_CHINH);
		header.add(title, BorderLayout.WEST);

		return header;
	}

	private JPanel taoContent() {
		JPanel content = new JPanel(new BorderLayout(0, 15));
		content.setOpaque(false);

		// Phần bảng kết quả
		String[] columns = { "Mã KH", "Họ và tên", "Số điện thoại", "CCCD", "Địa chỉ", "Email", "Loại KH" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		table = new JTable(model);
		AppTheme.styleTable(table);
		table.setRowHeight(40);
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		// Thanh tìm kiếm
		JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
		searchPanel.setBackground(AppTheme.CARD_BG);
		searchPanel.setBorder(AppTheme.cardBorder());
		
		JLabel lblSearch = new JLabel("Nhập tên, SĐT hoặc CCCD:");
		lblSearch.setFont(AppTheme.font(Font.BOLD, 13));
		searchPanel.add(lblSearch, BorderLayout.WEST);
		
		txtTimKiem = new JTextField();
		txtTimKiem.setFont(AppTheme.font(Font.PLAIN, 13));
		txtTimKiem.addActionListener(e -> {
			String text = txtTimKiem.getText().trim();
			if (text.length() == 0) sorter.setRowFilter(null);
			else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		});
		searchPanel.add(txtTimKiem, BorderLayout.CENTER);
		
		JButton btnLamMoi = new JButton("Làm mới");
		AppTheme.styleSecondaryButton(btnLamMoi);
		btnLamMoi.addActionListener(e -> { txtTimKiem.setText(""); sorter.setRowFilter(null); napDuLieuTuDB(); });
		searchPanel.add(btnLamMoi, BorderLayout.EAST);

		content.add(searchPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
		content.add(scrollPane, BorderLayout.CENTER);

		return content;
	}

	private void napDuLieuTuDB() {
		model.setRowCount(0);
		String sql = "SELECT maKH, tenKH, sdt, CCCD, diaChi, email, loaiKH FROM KhachHang";
		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				model.addRow(new Object[] {
					rs.getString("maKH"),
					rs.getString("tenKH"),
					rs.getString("sdt"),
					rs.getString("CCCD"),
					rs.getString("diaChi"),
					rs.getString("email"),
					rs.getBoolean("loaiKH") ? "Thành viên" : "Vãng lai"
				});
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu khách hàng: " + ex.getMessage());
		}
	}
}
