package andrei.productsservice.export;

import andrei.productsservice.model.Product;

import java.util.List;

public class CsvProductExporter extends ProductExporter {

    @Override
    protected String buildHeader() {
        return "id,name,description,price,shopId\n";
    }

    @Override
    protected String buildBody(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append(p.getId()).append(',')
              .append(escapeCsv(p.getName())).append(',')
              .append(escapeCsv(p.getDescription())).append(',')
              .append(p.getPrice()).append(',')
              .append(p.getShopId()).append('\n');
        }
        return sb.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
