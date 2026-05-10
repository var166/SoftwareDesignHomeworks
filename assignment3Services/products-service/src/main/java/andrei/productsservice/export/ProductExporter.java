package andrei.productsservice.export;

import andrei.productsservice.model.Product;

import java.util.List;

public abstract class ProductExporter {

    // Template method — defines the algorithm skeleton
    public final String export(List<Product> products) {
        String header = buildHeader();
        String body   = buildBody(products);
        String footer = buildFooter();
        return assemble(header, body, footer);
    }

    protected abstract String buildHeader();
    protected abstract String buildBody(List<Product> products);

    protected String buildFooter() {
        return "";
    }

    protected String assemble(String header, String body, String footer) {
        return header + body + footer;
    }
}
