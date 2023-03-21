import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

public class WalmartScraper {
    public static void main(String[] args) {
        String url = "https://www.walmart.com/browse/electronics/dell-gaming-laptops/3944_3951_7052607_1849032_4519159";
        String[] headers = { "Product Name", "Product Price", "Item Number/SKU/Product Code", "Model Number", "Product Category", "Product Description" };
        String[][] data = new String[10][6];
        int row = 0;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements products = doc.select("div.search-result-gridview-item");

            for (Element product : products) {
                if (row == 10) {
                    break;
                }

                String name = product.select("a.product-title-link").text();
                String price = product.select("span.price-group").text();
                String itemNumber = product.select("div.search-result-productimage.gridview a").attr("href").split("/")[3];
                String modelNumber = product.select("span.model-number").text();
                String category = product.select("span.category").text();
                String description = product.select("div.search-result-productdescription").text();

                data[row][0] = name;
                data[row][1] = price;
                data[row][2] = itemNumber;
                data[row][3] = modelNumber;
                data[row][4] = category;
                data[row][5] = description;

                row++;
            }

            CSVWriter writer = new CSVWriter(new FileWriter("walmart_products.csv"));
            writer.writeNext(headers);
            writer.writeAll(data);
            writer.close();

            System.out.println("Product details exported to walmart_products.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
