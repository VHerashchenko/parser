package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameObj {
    @JsonProperty("values")
    private ValueName valuesName;
}
