package andrei.productsservice.export;

import andrei.productsservice.model.Product;

import java.util.List;

public class XmlProductExporter extends ProductExporter {

    @Override
    protected String buildHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<products>\n";
    }

    @Override
    protected String buildBody(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        for (Product p : products) {
            sb.append("  <product>\n")
              .append("    <id>").append(p.getId()).append("</id>\n")
              .append("    <name>").append(escapeXml(p.getName())).append("</name>\n")
              .append("    <description>").append(escapeXml(p.getDescription())).append("</description>\n")
              .append("    <price>").append(p.getPrice().toPlainString()).append("</price>\n")
              .append("    <shopId>").append(p.getShopId()).append("</shopId>\n")
              .append("  </product>\n");
        }
        return sb.toString();
    }

    @Override
    protected String buildFooter() {
        return "</products>";
    }

    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
