package andrei.productsservice.export;

import andrei.productsservice.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class JsonProductExporter extends ProductExporter {

    @Override
    protected String buildHeader() {
        return "[";
    }

    @Override
    protected String buildBody(List<Product> products) {
        return products.stream()
                .map(p -> String.format(
                        "{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"price\":%s,\"shopId\":%d}",
                        p.getId(),
                        escape(p.getName()),
                        escape(p.getDescription()),
                        p.getPrice().toPlainString(),
                        p.getShopId()
                ))
                .collect(Collectors.joining(","));
    }

    @Override
    protected String buildFooter() {
        return "]";
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
