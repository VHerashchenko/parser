package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;

@Data
@Getter
@AllArgsConstructor
public class EntityFinalObj {
    private Integer productId;
    private String productName;
    private String productBrand;
    private HashSet<String> productColors;
    private Double productPrice;
}
