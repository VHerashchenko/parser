import com.gargoylesoftware.htmlunit.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Parser {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String itemsPerPage = "450";

        int iterations = 1;
        boolean requestedIterations = false;

        System.out.println("How many items do u want to get? Write a natural number between 1 and 100. Items will be 450 * x");
        System.out.println("Please enter \"0\" if you want to get all (It takes some time)");
        System.out.println("(There are not any validations, enter correct please)");
        System.out.println("x: ");

        int enter = Integer.parseInt(reader.readLine());
        if(enter == 0){
            requestedIterations = true;
        }
        else if (enter > 1 && enter < 101){
            iterations = enter;
        }

        ProductInformation productInformation = new ProductInformation();

        for(int pageNumber = 1; pageNumber <= iterations; ++pageNumber){
            String url = "https://api-cloud.aboutyou.de/v1/products?with=attributes%3Akey%28brand%7CbrandLogo%7CbrandAlignment%7Cname%7CquantityPerPack%7CplusSize%7CcolorDetail%7CsponsorBadge%7CsponsoredType%7CmaternityNursing%7Cexclusive%7Cgenderage%7CspecialSizesProduct%7CmaterialStyle%7CsustainabilityIcons%7CassortmentType%7CdROPS%29%2CadvancedAttributes%3Akey%28materialCompositionTextile%7Csiblings%29%2Cvariants%2Cvariants.attributes%3Akey%28shopSize%7CcategoryShopFilterSizes%7Ccup%7Ccupsize%7CvendorSize%7Clength%7Cdimension3%7CsizeType%7Csort%29%2Cimages.attributes%3Alegacy%28false%29%3Akey%28imageNextDetailLevel%7CimageBackground%7CimageFocus%7CimageGender%7CimageType%7CimageView%29%2CpriceRange&filters%5Bcategory%5D=20290&sortDir=desc&sortScore=category_scores&sortChannel=sponsored_web_default&page="
                    + pageNumber + "&perPage="
                    + itemsPerPage + "&campaignKey=px&shopId=605";
            String response = getResponseString(url);

            productInformation.entitiesEnter(response);
            productInformation.convertEntities();

            if(pageNumber == 1 && requestedIterations){
                iterations = productInformation.getIterationAmount();
            }
        }

        productInformation.createJsonFile();

        System.out.println("Amount of extracted items: " + (Integer.parseInt(itemsPerPage) * iterations));
        System.out.println("Amount of HTTP Requests: " + iterations);

    }

    private static String getResponseString(String url) throws IOException {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        final var page = client.getPage(new URL(url));

        return page.getWebResponse().getContentAsString();
    }
}
