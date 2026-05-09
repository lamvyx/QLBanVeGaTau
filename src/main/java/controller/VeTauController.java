package controller;

import entity.LichSuVeDTO;
import service.VeTauService;
import java.util.List;

public class VeTauController {
    private final VeTauService veTauService = new VeTauService();

    public List<LichSuVeDTO> layLichSuVe(String maKH) {
        if (maKH != null) {
            maKH = maKH.trim();
        }
        return veTauService.layLichSuVe(maKH);
    }
}
