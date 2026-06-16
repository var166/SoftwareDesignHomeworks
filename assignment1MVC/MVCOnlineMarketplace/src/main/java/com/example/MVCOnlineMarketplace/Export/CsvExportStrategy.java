package com.example.MVCOnlineMarketplace.Export;

import com.example.MVCOnlineMarketplace.Dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvExportStrategy implements ExportStrategy {

    @Override
    public String export(List<ProductDto> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,description,price,shopId\n");
        for (ProductDto p : products) {
            sb.append(p.getId()).append(",")
              .append(escapeCsv(p.getName())).append(",")
              .append(escapeCsv(p.getDescription())).append(",")
              .append(p.getPrice()).append(",")
              .append(p.getShopId()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getFormat() {
        return "csv";
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
