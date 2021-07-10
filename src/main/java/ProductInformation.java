import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.*;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.*;

@NoArgsConstructor
public class ProductInformation {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ArrayList<EntityFinalObj> entities = new ArrayList<>();

    private ResponseObj fineObject;

    public void entitiesEnter(String response) throws JsonProcessingException {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        fineObject = objectMapper.readValue(response, ResponseObj.class);
    }

    public void convertEntities(){
        for(Entity entity : fineObject.getEntities()){

            Integer entityId = entity.getEntityId();

            String entityName = entity
                    .getAttributes()
                    .getName()
                    .getValuesName()
                    .getName();

            String entityBrand = entity
                    .getAttributes()
                    .getBrand()
                    .getValues()
                    .getBrand();

            HashSet<String> entityColors = new HashSet<>(collisionDetector(
                            combineColors(entity)));

//            all we need /100 cause it is integer in ValuePrice
            Double entityPrice = Double.valueOf(
                    entity.getPriceRange().getValuePrice().getPrice())
                    / 100;

            entities.add(new EntityFinalObj(entityId, entityName, entityBrand, entityColors, entityPrice));
        }
    }

    private HashSet<String> combineColors(Entity currentEntity){
        HashSet<String> createdColorsSet = new HashSet<>();


//        main color for entity

        ColorDetail mainPageColorDetail = currentEntity.getAttributes().getColorDetail();

        if(mainPageColorDetail != null) {

            List<ValueColor> mainPageColorImage = mainPageColorDetail.getValues();

            String mainColor = colorBuilder(mainPageColorImage);
            createdColorsSet.add(mainColor);
        }

//        related colors
        Siblings siblings = currentEntity.getAdvancedAttributes().getSiblings();

        if(siblings != null) {

            List<FieldSet> siblingsColorsHolder = siblings.getValues().get(0).getFieldSet().get(0);

            for (FieldSet fieldSet : siblingsColorsHolder)
                if (!fieldSet.isSoldOut()) {
                    String siblingColor = colorBuilder(fieldSet.getColorDetail());
                    createdColorsSet.add(siblingColor);
                }

        }
        return createdColorsSet;
    }

    private String colorBuilder(List<ValueColor> array){
        String returnedString = "";

        if(array != null) {
            StringBuilder tempColors = new StringBuilder();
            for (int i = 0; i < array.size(); ++i) {
                if (i == 0) {
                    tempColors = new StringBuilder(array.get(i).getColor());
                } else {
                    tempColors.append("/").append(array.get(i).getColor());
                }
            }
            returnedString = tempColors.toString();
        }
        return returnedString;
    }



    private HashSet<String> collisionDetector(HashSet<String> productPrice){
        ArrayList<String> setInList = new ArrayList<>(productPrice);
        ArrayList<ArrayList<String>> sortedList = new ArrayList<>();

        HashSet<String> returnedSetColors = new HashSet<>();

        for (String s : setInList) {
            sortedList.add(new ArrayList<>(Arrays.asList(s.split("/"))));
        }

        for(ArrayList<String> array : sortedList){
            array.sort(Comparator.naturalOrder());

            StringBuilder tempSortedColors = new StringBuilder();
            for(int i = 0; i < array.size(); ++i){
                if(i == 0){
                    tempSortedColors = new StringBuilder(array.get(i));
                }
                else{
                    tempSortedColors.append("/").append(array.get(i));
                }
            }
            returnedSetColors.add(tempSortedColors.toString());
        }
        return returnedSetColors;
    }

    public void createJsonFile() throws IOException {
        String json = objectMapper.writeValueAsString(entities);

        objectMapper.writeValue(new File("aboutYou.json"),
                json.replace("\"", ""));
    }

    public ArrayList<EntityFinalObj> getEntities(){
        return entities;
    }

    public Integer getIterationAmount(){
        return fineObject.getPagination().getLast();
    }
}

