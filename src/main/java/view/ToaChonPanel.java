package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

public class ToaChonPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color MAU_RANH = Color.decode("#DDF7E3");
	private static final Color MAU_RANH_CHU = Color.decode("#176B3A");
	private static final Color MAU_BAN = Color.decode("#FCE1E1");
	private static final Color MAU_BAN_CHU = Color.decode("#A12C2C");
	private static final Color MAU_DA_CHON = Color.decode("#D7E7FF");
	private static final Color MAU_DA_CHON_CHU = Color.decode("#184A90");
	private static final Dimension CARD_SIZE = new Dimension(150, 86);

	private final JLabel lblTieuDe = new JLabel("Chọn toa cho chuyến");
	private final JLabel lblMoTa = new JLabel("Toa rảnh màu xanh, toa đã có lịch màu đỏ.");
	private final JPanel gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
	private final List<CoachCard> cards = new ArrayList<>();
	private int gioiHanChon = Integer.MAX_VALUE;
	private Consumer<Set<String>> selectionListener;

	public ToaChonPanel() {
		setLayout(new BorderLayout(0, 12));
		setOpaque(false);

		JPanel top = new JPanel(new BorderLayout(0, 4));
		top.setOpaque(false);
		lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTieuDe.setForeground(Color.decode("#2B4B74"));
		lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblMoTa.setForeground(Color.decode("#6B7A90"));
		top.add(lblTieuDe, BorderLayout.NORTH);
		top.add(lblMoTa, BorderLayout.CENTER);

		JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		legend.setOpaque(false);
		legend.add(taoLegend("Toa rảnh", MAU_RANH, MAU_RANH_CHU));
		legend.add(taoLegend("Đã lên lịch / đang chạy", MAU_BAN, MAU_BAN_CHU));
		legend.add(taoLegend("Đang chọn", MAU_DA_CHON, MAU_DA_CHON_CHU));
		top.add(legend, BorderLayout.SOUTH);

		gridPanel.setOpaque(false);

		add(top, BorderLayout.NORTH);
		add(gridPanel, BorderLayout.CENTER);
	}

	public void setTieuDe(String tieuDe) {
		lblTieuDe.setText(tieuDe);
	}

	public void setMoTa(String moTa) {
		lblMoTa.setText(moTa);
	}

	public void setGioiHanChon(int gioiHanChon) {
		this.gioiHanChon = gioiHanChon <= 0 ? Integer.MAX_VALUE : gioiHanChon;
	}

	public void setSelectionListener(Consumer<Set<String>> selectionListener) {
		this.selectionListener = selectionListener;
	}

	public void setDanhSachToa(List<CoachCardData> danhSach) {
		cards.clear();
		gridPanel.removeAll();
		if (danhSach != null) {
			for (CoachCardData data : danhSach) {
				CoachCard card = new CoachCard(data);
				cards.add(card);
				gridPanel.add(card);
			}
		}
		revalidate();
		repaint();
		notifySelectionChanged();
	}

	public void setSelectedToa(Set<String> maToaSet) {
		Set<String> values = maToaSet == null ? Set.of() : maToaSet;
		for (CoachCard card : cards) {
			card.setSelected(values.contains(card.data.maToa) && card.data.available);
			card.capNhatMau();
		}
		notifySelectionChanged();
	}

	public Set<String> getSelectedToa() {
		Set<String> result = new LinkedHashSet<>();
		for (CoachCard card : cards) {
			if (card.isSelected() && card.data.available) {
				result.add(card.data.maToa);
			}
		}
		return result;
	}

	public int getSelectedCount() {
		return getSelectedToa().size();
	}

	public void setSelectionEnabled(boolean enabled) {
		for (CoachCard card : cards) {
			card.setInteractionEnabled(enabled);
		}
	}

	private JPanel taoLegend(String text, Color bg, Color fg) {
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(bg);
		label.setForeground(fg);
		label.setFont(new Font("Segoe UI", Font.BOLD, 12));
		label.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(bg.darker()),
				new EmptyBorder(5, 10, 5, 10)));
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(label, BorderLayout.CENTER);
		return wrapper;
	}

	private void notifySelectionChanged() {
		if (selectionListener != null) {
			selectionListener.accept(getSelectedToa());
		}
	}

	public static class CoachCardData {
		public final String maToa;
		public final String loaiToa;
		public final int soGhe;
		public final String moTa;
		public final boolean available;

		public CoachCardData(String maToa, String loaiToa, int soGhe, String moTa, boolean available) {
			this.maToa = maToa;
			this.loaiToa = loaiToa;
			this.soGhe = soGhe;
			this.moTa = moTa;
			this.available = available;
		}
	}

	private class CoachCard extends JToggleButton {
		private static final long serialVersionUID = 1L;
		private final CoachCardData data;

		CoachCard(CoachCardData data) {
			this.data = data;
			setLayout(new BorderLayout());
			setText("<html><div style='text-align:center'><b>" + data.maToa + "</b><br/>"
					+ data.loaiToa + "<br/>Ghế: " + data.soGhe + "<br/>" + data.moTa + "</div></html>");
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
			setFocusPainted(false);
			setFont(new Font("Segoe UI", Font.BOLD, 12));
			setPreferredSize(CARD_SIZE);
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#C8D6E5")),
					new EmptyBorder(7, 6, 7, 6)));
			setEnabled(data.available);
			addActionListener(e -> {
				if (isSelected() && getSelectedCount() > gioiHanChon) {
					setSelected(false);
				}
				capNhatTatCaMau();
				notifySelectionChanged();
			});
			capNhatMau();
		}

		private void capNhatMau() {
			if (!data.available) {
				setBackground(MAU_BAN);
				setForeground(MAU_BAN_CHU);
				return;
			}
			if (isSelected()) {
				setBackground(MAU_DA_CHON);
				setForeground(MAU_DA_CHON_CHU);
				return;
			}
			setBackground(MAU_RANH);
			setForeground(MAU_RANH_CHU);
		}

		private void capNhatTatCaMau() {
			for (CoachCard card : cards) {
				card.capNhatMau();
			}
		}

		private void setInteractionEnabled(boolean enabled) {
			setEnabled(enabled && data.available);
		}
	}
}
