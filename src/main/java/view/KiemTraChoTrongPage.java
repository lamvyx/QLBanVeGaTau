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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class KiemTraChoTrongPage extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_CHINH = AppTheme.PRIMARY;
	private static final Color MAU_TEXT = AppTheme.TEXT_PRIMARY;
	private static final Color MAU_XANH = Color.decode("#22C55E");
	private static final Color MAU_DO = Color.decode("#EF4444");
	private static final Color MAU_DANG_CHON = Color.decode("#3B82F6");

	private final JLabel lblSoChoTrong = new JLabel("26");
	private final JLabel lblSoChoDat = new JLabel("14");
	private final JLabel lblSoChoDangChon = new JLabel("1");
	private final JLabel lblToaDangXem = new JLabel("Toa 2 - Ghế mềm - 40 ghế");
	private final JLabel lblNgayChieu = new JLabel("03/04/2026");
	private final JLabel lblGioChieu = new JLabel("19:00");
	private final JLabel lblTrangThai = new JLabel("Đang kiểm tra chỗ trống");

	private String selectedSeat = "E03";

	public KiemTraChoTrongPage() {
		setLayout(new BorderLayout());
		setBackground(AppTheme.PAGE_BG);
		add(taoHeader(), BorderLayout.NORTH);
		add(taoContent(), BorderLayout.CENTER);
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

		addField(card, gbc, 0, 0, "Chuyến tàu", new JComboBox<>(new String[] {
			"SE1-030426 - Sài Gòn - Hà Nội - 19:00",
			"SE2-030426 - Sài Gòn - Đà Nẵng - 07:00",
			"TN1-030426 - Sài Gòn - Nha Trang - 06:00"
		}));
		addField(card, gbc, 1, 0, "Toa tàu", new JComboBox<>(new String[] {
			"Toa 1 - Ghế cứng - 40 ghế",
			"Toa 2 - Ghế mềm - 40 ghế",
			"Toa 3 - Nằm mềm - 24 ghế"
		}));
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

		JPanel info = new JPanel(new GridBagLayout());
		info.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 0, 4, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		addInfo(info, gbc, 0, "Toa:", lblToaDangXem);
		addInfo(info, gbc, 1, "Ngày:", lblNgayChieu);
		addInfo(info, gbc, 2, "Giờ:", lblGioChieu);
		addInfo(info, gbc, 3, "Trạng thái:", lblTrangThai);
		card.add(info, BorderLayout.SOUTH);
		return card;
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
		grid.add(createSeatGrid(), BorderLayout.CENTER);
		JLabel hint = new JLabel("Chọn ghế trống để xem vị trí còn khả dụng");
		hint.setFont(AppTheme.font(Font.ITALIC, 12));
		hint.setForeground(AppTheme.TEXT_MUTED);
		hint.setHorizontalAlignment(SwingConstants.CENTER);
		grid.add(hint, BorderLayout.SOUTH);
		card.add(new JScrollPane(grid), BorderLayout.CENTER);
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
		JPanel cols = new JPanel(new GridLayout(1, 4, 8, 8));
		cols.setOpaque(false);
		for (String col : new String[] { "A", "B", "C", "D" }) {
			JLabel lbl = new JLabel(col, SwingConstants.CENTER);
			lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
			lbl.setForeground(MAU_TEXT);
			cols.add(lbl);
		}
		wrap.add(cols, BorderLayout.NORTH);

		JPanel seats = new JPanel(new GridLayout(8, 4, 8, 8));
		seats.setOpaque(false);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 4; col++) {
				JButton seat = new JButton(String.format("%02d", row + 1));
				seat.setFont(new Font("Segoe UI", Font.BOLD, 12));
				seat.setFocusPainted(false);
				seat.setPreferredSize(new Dimension(42, 34));
				seat.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
				seat.setBackground(Color.WHITE);
				seat.setForeground(MAU_TEXT);
				if ((row == 0 && (col == 1 || col == 3)) || (row == 1 && col == 2) || (row == 3 && col == 1) || (row == 5 && col != 0) || (row == 6 && (col == 0 || col == 2 || col == 3)) || (row == 7 && col == 1)) {
					seat.setBackground(new Color(255, 235, 235));
					seat.setForeground(MAU_DO);
					seat.setBorder(BorderFactory.createLineBorder(MAU_DO, 2));
				}
				if (row == 2 && col == 2) {
					seat.setBackground(MAU_DANG_CHON);
					seat.setForeground(Color.WHITE);
					seat.setBorder(BorderFactory.createLineBorder(MAU_DANG_CHON.darker(), 2));
					selectedSeat = "E03";
				}
				seat.addActionListener(e -> {
					if (!seat.getBackground().equals(new Color(255, 235, 235))) {
						selectedSeat = "E" + seat.getText();
						lblTrangThai.setText("Ghế " + selectedSeat + " đang khả dụng");
						lblSoChoDangChon.setText("1");
						for (java.awt.Component component : seats.getComponents()) {
							if (component instanceof JButton other && !other.getBackground().equals(new Color(255, 235, 235))) {
								other.setBackground(Color.WHITE);
								other.setForeground(MAU_TEXT);
								other.setBorder(BorderFactory.createLineBorder(MAU_XANH, 2));
							}
						}
						seat.setBackground(MAU_DANG_CHON);
						seat.setForeground(Color.WHITE);
						seat.setBorder(BorderFactory.createLineBorder(MAU_DANG_CHON.darker(), 2));
					}
				});
				seats.add(seat);
			}
		}
		wrap.add(seats, BorderLayout.CENTER);
		return wrap;
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
		lblTrangThai.setText("Đang xem chỗ trống của " + lblToaDangXem.getText());
		lblNgayChieu.setText("03/04/2026");
		lblGioChieu.setText("19:00");
	}

	private void resetSeats() {
		lblSoChoTrong.setText("26");
		lblSoChoDat.setText("14");
		lblSoChoDangChon.setText("1");
		lblTrangThai.setText("Đang kiểm tra chỗ trống");
		selectedSeat = "E03";
	}
}
