package cmart.util;

import cmart.model.CartItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple text encoding for held carts (no external JSON dependency).
 * One line: productName|barcode|qty|unitPrice|totalPrice
 */
public final class CartCodec {

    private static final String SEP = "|";

    private CartCodec() {
    }

    public static String encode(List<CartItem> lines) {
        StringBuilder sb = new StringBuilder();
        if (lines == null) {
            return "";
        }
        for (CartItem line : lines) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(safe(line.getProductName())).append(SEP)
                    .append(safe(line.getBarcode())).append(SEP)
                    .append(safe(line.getQty())).append(SEP)
                    .append(safe(line.getUnitPrice())).append(SEP)
                    .append(safe(line.getTotalPrice()));
        }
        return sb.toString();
    }

    public static List<CartItem> decode(String payload) {
        List<CartItem> lines = new ArrayList<CartItem>();
        if (payload == null || payload.trim().isEmpty()) {
            return lines;
        }
        String[] rows = payload.split("\\r?\\n");
        for (String row : rows) {
            if (row.trim().isEmpty()) {
                continue;
            }
            String[] parts = row.split("\\|", -1);
            if (parts.length < 5) {
                continue;
            }
            CartItem item = new CartItem();
            item.setProductName(parts[0]);
            item.setBarcode(parts[1]);
            item.setQty(parts[2]);
            item.setUnitPrice(parts[3]);
            item.setTotalPrice(parts[4]);
            lines.add(item);
        }
        return lines;
    }

    private static String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/").replace("\n", " ").replace("\r", " ");
    }
}
