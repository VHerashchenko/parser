package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    @JsonProperty("id")
    private Integer entityId;

    @JsonProperty("attributes")
    private Attributes attributes;

    private AdvancedAttributes advancedAttributes;

    private PriceRange priceRange;

}
