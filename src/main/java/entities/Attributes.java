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
public class Attributes {
    @JsonProperty("name")
    private NameObj name;

    private ColorDetail colorDetail;

    private Brand brand;

}
